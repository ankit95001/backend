package com.ecom.product_service.entity;

import lombok.*;
import org.bson.BsonInt64;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.*;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Transient
    private static final String SEQUENCE_NAME = "PRODUCT_SEQUENCE";

    @MongoId
    private String productId;

    @NotBlank(message = "Product name is required")
    private String name;

    @NotBlank(message = "Description is required")
    private String description;

    @Positive(message = "Price must be greater than 0")
    private double price;

    @NotBlank(message = "Category is required")
    private String category;

    @NotBlank(message = "Brand is required")
    private String brand;

    @NotNull(message = "stockQuantity is required")
    @Min(value = 1, message = "Stock quantity cannot be negative")
    private Integer stockQuantity;

    @NotBlank(message = "Image url is required")
    private String image;
}