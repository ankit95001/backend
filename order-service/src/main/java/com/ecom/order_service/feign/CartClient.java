package com.ecom.order_service.feign;

import com.ecom.order_service.dto.CartDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "CART-SERVICE")
public interface CartClient {
    @GetMapping("/cart/{userId}")
    public CartDto viewCart(@PathVariable Long userId);

    @DeleteMapping("cart/{userId}/clear")
    public String clearCart(@PathVariable Long userId);
}

