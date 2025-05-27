package com.ecom.product_service.controller;

import com.ecom.product_service.entity.Product;
import com.ecom.product_service.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequestMapping("/openProduct")
public class OpenProductController {

    @Autowired
    ProductService productService;

    @GetMapping("/all")
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }
    @Operation(summary = "Fetch products by category", description = "Accessible by anyone")
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable String category) {

        return ResponseEntity.ok(productService.getProductsByCategory(category));
    }
}
