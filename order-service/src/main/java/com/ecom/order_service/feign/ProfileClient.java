package com.ecom.order_service.feign;

import com.ecom.order_service.dto.UserDetailsDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "PROFILE-SERVICE")
public interface ProfileClient {
    @GetMapping("/profile/{userId}")
    public UserDetailsDTO getUserProfile(@PathVariable Long userId);
}
