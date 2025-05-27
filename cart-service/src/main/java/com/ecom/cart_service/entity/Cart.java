package com.ecom.cart_service.entity;



import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWarDeployment;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Cart {

    @Id
    private ObjectId cartId;

    @NotNull(message = "User ID cannot be null")
    @Min(value = 1, message = "User ID must be greater than 0")
    private Long userId;

    @Valid
    @NotNull(message = "Items list cannot be null")
    private List<@Valid CartItem> items;

    @Min(value = 0, message = "Total price cannot be negative")
    private double totalPrice;

    @NotNull(message = "Last updated time cannot be null")
    private LocalDateTime lastUpdated;
}

