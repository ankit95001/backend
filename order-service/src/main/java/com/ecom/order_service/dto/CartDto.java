package com.ecom.order_service.dto;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {

    @Id
    private ObjectId cartId; // cart id

    private Long userId; // reference to the user

    private List<CartItemDto> items;


    private double totalPrice;

    private LocalDateTime lastUpdated;
}
