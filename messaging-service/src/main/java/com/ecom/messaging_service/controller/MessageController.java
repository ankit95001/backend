package com.ecom.messaging_service.controller;

import com.ecom.messaging_service.dto.MessageDto;
import com.ecom.messaging_service.publisher.RabbitMQProducer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/message")
public class MessageController {
    private RabbitMQProducer producer;


    public MessageController(RabbitMQProducer producer) {
        this.producer = producer;
    }

    @GetMapping("/publish")
    public ResponseEntity<String> sendMessage(@RequestParam("message") String message){
        producer.sendMessage(message);
        return ResponseEntity.ok("Message sent to RabbbitMQ");
    }

    @PostMapping("/publish")
    public ResponseEntity<String> sendMessage(@RequestBody MessageDto order){
        producer.sendMessage(order);
        return ResponseEntity.ok("Json message sent to RabbitMQ...");
    }

}
