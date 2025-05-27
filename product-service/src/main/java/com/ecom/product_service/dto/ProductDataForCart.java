package com.ecom.product_service.dto;

import lombok.*;
import org.bson.types.ObjectId;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDataForCart {
    private String ProductId;
    private String name;
    private double price;
    private String image;
}
