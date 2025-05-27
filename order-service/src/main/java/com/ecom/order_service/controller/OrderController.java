package com.ecom.order_service.controller;

import com.ecom.order_service.dto.StripeResponse;
import com.ecom.order_service.entity.Order;
import com.ecom.order_service.entity.UserContext;
import com.ecom.order_service.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@Tag(name = "Order Controller", description = "Manages order placement and history")
public class OrderController {

    private final OrderService orderService;
    private final UserContext userContext;

    @Operation(summary = "Place an order from cart")
    @PostMapping("/{userId}/checkout")
    public ResponseEntity<StripeResponse> placeOrder(@PathVariable Long userId) {
        validateUser(userId);
        StripeResponse response = orderService.placeOrder(userId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get order details")
    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrder(@PathVariable String orderId) {
        ObjectId id = parseObjectId(orderId);
        Order order = orderService.getOrderDetails(id);
        return ResponseEntity.ok(order);
    }

    @Operation(summary = "Fetch user order history")
    @GetMapping("/history/{userId}")
    public ResponseEntity<List<Order>> getOrderHistory(@PathVariable Long userId) {
        validateUser(userId);
        return ResponseEntity.ok(orderService.getOrderHistory(userId));
    }

    @Operation(summary = "Cancel order")
    @PutMapping("/cancel/{orderId}")
    public ResponseEntity<Order> cancelOrder(@PathVariable String orderId) {
        ObjectId id = parseObjectId(orderId);
        return ResponseEntity.ok(orderService.cancelOrder(id));
    }

    @Operation(summary = "Update payment status using session ID")
    @PostMapping("/updatePayment")
    public ResponseEntity<String> updatePaymentStatus(@RequestParam String sessionId,
                                                      @RequestParam String paymentStatus) {
        if (sessionId == null || sessionId.isBlank() || paymentStatus == null || paymentStatus.isBlank()) {
            throw new IllegalArgumentException("Session ID and payment status are required");
        }
        boolean updated = orderService.updatePaymentStatus(sessionId, paymentStatus);
        return updated
                ? ResponseEntity.ok("Order payment status updated successfully")
                : ResponseEntity.status(404).body("Order not found for this session ID");
    }

    @Operation(summary = "Send order confirmation email")
    @GetMapping("/sendMessage/{orderId}")
    public ResponseEntity<String> message(@PathVariable String orderId) {
        ObjectId id = parseObjectId(orderId);
        return orderService.sendOrderConfirmation(id);
    }

    private void validateUser(Long userId) {
        if (!userContext.getUserId().equals(userId)) {
            throw new SecurityException("Unauthorized access");
        }
    }

    private ObjectId parseObjectId(String idStr) {
        try {
            return new ObjectId(idStr);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid order ID format");
        }
    }
}
