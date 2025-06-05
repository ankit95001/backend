package com.ecom.order_service.service;

import com.ecom.order_service.domain.OrderStatus;
import com.ecom.order_service.dto.*;
import com.ecom.order_service.entity.Order;
import com.ecom.order_service.exception.*;
import com.ecom.order_service.feign.CartClient;
import com.ecom.order_service.feign.MessageClient;
import com.ecom.order_service.feign.PaymentClient;
import com.ecom.order_service.feign.ProfileClient;
import com.ecom.order_service.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class OrderService {

    @Autowired private ProfileClient profileClient;
    @Autowired private CartClient cartClient;
    @Autowired private PaymentClient paymentClient;
    @Autowired private OrderRepository orderRepository;
    @Autowired private MessageClient messageClient;

    private UserForOrder userForOrder;

    public StripeResponse placeOrder(Long userId) {
        try {
            // 1. Validate userId
            if (userId == null || userId <= 0) {
                throw new IllegalArgumentException("Invalid user ID.");
            }

            // 2. Fetch user details
            UserDetailsDTO userDetails = profileClient.getUserProfile(userId);
            if (userDetails == null || userDetails.getAddresses() == null || userDetails.getAddresses().isEmpty()) {
                throw new UserProfileNotFoundException("User profile or address is missing.");
            }

            userForOrder = new UserForOrder(
                    userDetails.getFullName(),
                    userDetails.getEmail(),
                    userDetails.getMobile(),
                    userDetails.getAddresses()
            );

            // 3. Fetch cart details
            CartDto cart = cartClient.viewCart(userId);
            if (cart == null || cart.getItems() == null || cart.getItems().isEmpty()) {
                throw new CartEmptyException("Cart is empty. Cannot place order.");
            }

            // 4. Create Order object
            Order order = Order.builder()
                    .userId(userId)
                    .userDetails(userForOrder)
                    .cart(cart)
                    .orderDate(LocalDateTime.now())
                    .status(OrderStatus.PENDING.name())
                    .amount(cart.getTotalPrice())
                    .shippingAddress(userDetails.getAddresses().get(0))
                    .build();

            Order savedOrder = orderRepository.save(order);
            log.info("Order created with ID: {}", savedOrder.getOrderId());

            // 5. Prepare and process payment
            ProductRequest paymentRequest = ProductRequest.builder()
                    .amount((long) cart.getTotalPrice())
                    .quantity((long) cart.getItems().size())
                    .name("Order#" + savedOrder.getOrderId())
                    .currency("USD")
                    .build();

            StripeResponse stripeResponse = paymentClient.checkoutProducts(paymentRequest);
            if (stripeResponse == null || stripeResponse.getStatus() == null) {
                throw new PaymentFailedException("Payment gateway failed to return a valid response.");
            }

            savedOrder.setStatus(stripeResponse.getStatus());
            savedOrder.setSessionId(stripeResponse.getSessionId());
            orderRepository.save(savedOrder);

            stripeResponse.setOrderId(savedOrder.getOrderId());

            return stripeResponse;

        } catch (CartEmptyException | UserProfileNotFoundException | IllegalArgumentException ex) {
            log.error("Validation or business error: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Unexpected error during order placement: {}", ex.getMessage(), ex);
            throw new PaymentFailedException("Payment failed or service error: " + ex.getMessage());
        }
    }

    public ResponseEntity<String> sendOrderConfirmation(ObjectId id) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("Order ID cannot be null.");
            }

            Optional<Order> optionalOrder = orderRepository.findById(id);
            if (userForOrder == null || optionalOrder.isEmpty()) {
                throw new OrderNotFoundException("Order not found for confirmation.");
            }

            Order order = optionalOrder.get();
            PaymentDetails paymentDetails = paymentClient.getPaymentDetails(order.getSessionId());
            if (paymentDetails == null) {
                throw new PaymentFailedException("Failed to retrieve payment details.");
            }

            order.setStatus(paymentDetails.getPaymentStatus());
            orderRepository.save(order);

            String message = String.format(
                    "Dear %s,\n\n" +
                            "We’re thrilled to let you know that your order has been received and is now being processed.\n\n" +
                            "Order Summary:\n" +
                            "  • Order ID     : %s\n" +
                            "  • Order Date   : %s\n" +
                            "  • Shipping To  : %s, %s, %s, %s - %s\n" +
                            "  • Total Amount : ₹%.2f\n" +
                            "  • Payment Status: %s\n\n" +
                            "You’ll receive another notification once your package is out for delivery.\n\n" +
                            "Thank you for shopping with us.\n" +
                            "Warm regards,\n" +
                            "E-Shopping zone Team",

                    userForOrder.getFullName(),
                    order.getOrderId().toString(),
                    order.getOrderDate().format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a")),
                    order.getShippingAddress().getLocality(),
                    order.getShippingAddress().getAddress(),
                    order.getShippingAddress().getCity(),
                    order.getShippingAddress().getState(),
                    order.getShippingAddress().getPinCode(),
                    order.getAmount(),
                    "PAID".equalsIgnoreCase(paymentDetails.getPaymentStatus()) ? "Paid" : "Unpaid"
            );
            // 6. Clear cart
            cartClient.clearCart(order.getUserId());
            log.info("Cart cleared for userId: {}", order.getUserId());

            return messageClient.sendMessage(new MessageDto(
                    userForOrder.getFullName(),
                    userForOrder.getEmail(),
                    message));

        } catch (Exception ex) {
            log.error("Failed to send order confirmation: {}", ex.getMessage(), ex);
            throw new MessageSendException("Failed to send order confirmation: " + ex.getMessage());
        }
    }

    public Order getOrderDetails(ObjectId orderId) {
        if (orderId == null) {
            throw new IllegalArgumentException("Order ID cannot be null.");
        }

        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + orderId));
    }

    public List<Order> getOrderHistory(Long userId) {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("Invalid user ID.");
        }

        return orderRepository.findByUserId(userId);
    }

    public Order cancelOrder(ObjectId orderId) {
        Order order = getOrderDetails(orderId);
        order.setStatus(OrderStatus.CANCELLED.name());
        return orderRepository.save(order);
    }

    public boolean updatePaymentStatus(String sessionId, String paymentStatus) {
        if (sessionId == null || sessionId.isEmpty() || paymentStatus == null) {
            throw new IllegalArgumentException("Session ID and payment status must be provided.");
        }

        Optional<Order> optionalOrder = orderRepository.findBySessionId(sessionId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setStatus(paymentStatus);
            orderRepository.save(order);
            return true;
        }

        return false;
    }
}
