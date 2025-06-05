package com.ecom.order_service.dto;


import lombok.*;
import org.bson.types.ObjectId;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CartItemDto {

    private String productId;

    private int quantity;

    private ProductDto productDetails;

}
