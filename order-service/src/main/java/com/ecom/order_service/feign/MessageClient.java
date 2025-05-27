package com.ecom.order_service.feign;

import com.ecom.order_service.dto.MessageDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "MESSAGING-SERVICE")
public interface MessageClient {
    @PostMapping("/message/publish")
    public ResponseEntity<String> sendMessage(@RequestBody MessageDto order);
}
