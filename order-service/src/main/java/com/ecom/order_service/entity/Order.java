package com.ecom.order_service.entity;

import com.ecom.order_service.dto.AddressDTO;
import com.ecom.order_service.dto.CartDto;
import com.ecom.order_service.dto.UserDetailsDTO;
import com.ecom.order_service.dto.UserForOrder;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "orders")
public class Order {

    @Id
    private ObjectId orderId;
    private UserForOrder userDetails;
    private Long userId;
    private CartDto cart;
    private LocalDateTime orderDate;
    private String status;
    private String sessionId;
    private double amount;
    AddressDTO shippingAddress;
}


