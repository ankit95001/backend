package com.ecom.cart_service.entity;

import com.ecom.cart_service.dto.ProductDataForCart;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.bson.types.ObjectId;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CartItem {

    @NotNull(message = "Product ID cannot be null")
    private String productId;

    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;

    private ProductDataForCart productDetails;
}
