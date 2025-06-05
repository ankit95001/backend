package com.ecom.payment_gateway.service;

import com.ecom.payment_gateway.dto.PaymentDetails;
import com.ecom.payment_gateway.dto.ProductRequest;
import com.ecom.payment_gateway.dto.StripeResponse;
import com.ecom.payment_gateway.repository.PaymentRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StripeService {

    @Value("${stripe.secretKey}")
    private String secretKey;

    private final PaymentRepository paymentRepository;

    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
        System.out.println("Stripe key set successfully!");
    }

    public StripeResponse checkoutProducts(ProductRequest productRequest) {
        Stripe.apiKey = secretKey;

        try {
            SessionCreateParams.LineItem.PriceData.ProductData productData =
                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                            .setName(productRequest.getName())
                            .build();

            SessionCreateParams.LineItem.PriceData priceData =
                    SessionCreateParams.LineItem.PriceData.builder()
                            .setCurrency(
                                    productRequest.getCurrency() != null ? productRequest.getCurrency() : "INR")
                            .setUnitAmount((long) (productRequest.getAmount()/89.1*100))
                            .setProductData(productData)
                            .build();

            SessionCreateParams.LineItem lineItem =
                    SessionCreateParams.LineItem.builder()
                            .setQuantity(productRequest.getQuantity())
                            .setPriceData(priceData)
                            .build();

            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl("http://localhost:8087/api/paymentStatus?session_id={CHECKOUT_SESSION_ID}")
                    .setCancelUrl("http://localhost:8080/cancel")
                    .addLineItem(lineItem)
                    .build();

            Session session = Session.create(params);

            return StripeResponse.builder()
                    .status("PENDING")
                    .message("Payment session created")
                    .sessionId(session.getId())
                    .sessionUrl(session.getUrl())
                    .build();

        } catch (StripeException e) {
            // TODO: Log error using proper logger
            return StripeResponse.builder()
                    .status("FAILED")
                    .message("Error creating Stripe session: " + e.getMessage())
                    .build();
        }
    }

    public ResponseEntity<String> getPaymentStatus(String sessionId) {
        try {
            Session session = Session.retrieve(sessionId);
            paymentRepository.save(new PaymentDetails(sessionId, session.getPaymentStatus()));
            return ResponseEntity.ok("Payment status updated successfully");
        } catch (StripeException e) {
            // TODO: Log error using proper logger
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Server error: " + e.getMessage());
        }
    }

    public PaymentDetails getPaymentDetails(String sessionId) {
        Optional<PaymentDetails> details = paymentRepository.findBySessionId(sessionId);
        return details.orElseThrow(() -> new IllegalArgumentException("No payment found with session ID: " + sessionId));
    }
}
