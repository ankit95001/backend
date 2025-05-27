package com.ecom.order_service.feign;

import com.ecom.order_service.dto.PaymentDetails;
import com.ecom.order_service.dto.ProductRequest;
import com.ecom.order_service.dto.StripeResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;


@FeignClient(name = "PAYMENT-GATEWAY")
public interface PaymentClient {
    @PostMapping("/checkout/payment")
    public StripeResponse checkoutProducts(@RequestBody ProductRequest productRequest);

    @GetMapping("/checkout/checkStatus")
    public PaymentDetails getPaymentDetails(@RequestParam String sessionId);
}
