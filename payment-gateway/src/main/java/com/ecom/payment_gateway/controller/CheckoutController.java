package com.ecom.payment_gateway.controller;

import com.ecom.payment_gateway.dto.PaymentDetails;
import com.ecom.payment_gateway.dto.ProductRequest;
import com.ecom.payment_gateway.dto.StripeResponse;
import com.ecom.payment_gateway.service.StripeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/checkout")
public class CheckoutController {

    private final StripeService stripeService;

    public CheckoutController(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    @PostMapping("/payment")
    public ResponseEntity<StripeResponse> checkoutProducts(@RequestBody ProductRequest productRequest) {
        StripeResponse response = stripeService.checkoutProducts(productRequest);
        HttpStatus status = response.getStatus().equalsIgnoreCase("FAILED") ? HttpStatus.BAD_REQUEST : HttpStatus.OK;
        return ResponseEntity.status(status).body(response);
    }

    @GetMapping("/checkStatus")
    public ResponseEntity<?> getPaymentDetails(@RequestParam String sessionId) {
        try {
            PaymentDetails paymentDetails = stripeService.getPaymentDetails(sessionId);
            return ResponseEntity.ok(paymentDetails);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
