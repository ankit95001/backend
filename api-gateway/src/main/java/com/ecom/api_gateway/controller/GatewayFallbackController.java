package com.ecom.api_gateway.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class GatewayFallbackController {

    @GetMapping("/fallback/profile")
    public ResponseEntity<String> profileFallback() {
        return ResponseEntity.ok("Profile service is currently unavailable.");
    }

    @GetMapping("/fallback/product")
    public ResponseEntity<String> productFallback() {
        return ResponseEntity.ok("Product service is currently unavailable.");
    }

    @GetMapping("/fallback/cart")
    public ResponseEntity<String> cartFallback() {
        return ResponseEntity.ok("Cart service is currently unavailable.");
    }

    @GetMapping("/fallback/order")
    public ResponseEntity<String> orderFallback() {
        return ResponseEntity.ok("Order service is currently unavailable.");
    }

    @GetMapping("/fallback/payment")
    public ResponseEntity<String> paymentFallback() {
        return ResponseEntity.ok("Payment service is currently unavailable.");
    }
}

