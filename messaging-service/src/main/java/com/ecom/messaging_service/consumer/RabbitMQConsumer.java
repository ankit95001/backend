package com.ecom.messaging_service.consumer;

import com.ecom.messaging_service.dto.MessageDto;
import com.ecom.messaging_service.service.SendEmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQConsumer.class);

    @Autowired
    private SendEmailService sendEmailService;


//    @RabbitListener(queues = {"${rabbitmq.queue.name}"})
//    public void consume(String message){
////        sendEmailService.sendEmail("ankit95001kumar@gmail.com",message,"Test subject");
//        LOGGER.info(String.format("Received message -> %s",message));
//
//    }
    @RabbitListener(queues = {"${rabbitmq.queue.name}"})
    public void consume(MessageDto message){
        System.out.println(message.getEmail());
        System.out.println(message.getSubject());
        System.out.println(message.getMessage());
        sendEmailService.sendEmail(message.getEmail(), message.getMessage(), message.getSubject());

    }
}
