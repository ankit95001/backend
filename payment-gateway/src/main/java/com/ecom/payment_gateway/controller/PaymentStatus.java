package com.ecom.payment_gateway.controller;

import com.ecom.payment_gateway.service.StripeService;
import com.stripe.Stripe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PaymentStatus {

    @Autowired
    StripeService stripeService;

    @GetMapping("/paymentStatus")
    public ResponseEntity<String> checkPaymentStatus(@RequestParam("session_id") String sessionId) {
        return stripeService.getPaymentStatus(sessionId);
    }
}
