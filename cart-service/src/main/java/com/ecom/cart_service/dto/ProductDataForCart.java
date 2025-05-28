package com.ecom.cart_service.dto;

import lombok.*;
import org.bson.types.ObjectId;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDataForCart {
    private String ProductId;
    private String name;
    private double price;
    private String image;
}
