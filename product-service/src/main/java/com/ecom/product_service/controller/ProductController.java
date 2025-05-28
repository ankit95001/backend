package com.ecom.product_service.controller;

import com.ecom.product_service.dto.ProductDataForCart;
import com.ecom.product_service.entity.Product;
import com.ecom.product_service.entity.UserContext;
import com.ecom.product_service.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Tag(name = "Product Controller", description = "Manage product operations")
@Validated
public class ProductController {

    private final ProductService productService;
    private final UserContext userContext;

    private void validateAdminRole() {
        if (!"ADMIN".equalsIgnoreCase(userContext.getRole())) {
            throw new SecurityException("Unauthorized: Admin role required");
        }
    }

    @Operation(summary = "Fetch all products", description = "Accessible by anyone")
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @Operation(summary = "Fetch product for cart", description = "Accessible by anyone")
    @GetMapping("/productForCart/{id}")
    public ResponseEntity<ProductDataForCart> productDataForCart(@PathVariable String id) {
        return ResponseEntity.ok(productService.cartProduct(id));
    }

    @Operation(summary = "Fetch products by category", description = "Accessible by anyone")
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable String category) {

        return ResponseEntity.ok(productService.getProductsByCategory(category));
    }

    @Operation(summary = "Get product details by ID", description = "Accessible by anyone")
    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable String productId) {
        return ResponseEntity.ok(productService.getProductById(productId));
    }

    @Operation(summary = "Add new product", description = "Accessible only by Admin")
    @PostMapping("/add")
    public ResponseEntity<String> addProduct(@Valid @RequestBody Product productRequest)  {
        validateAdminRole();  // Ensure only admins can add a product
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.saveProduct(productRequest));
    }

    @Operation(summary = "Update a product", description = "Accessible only by Admin")
    @PutMapping("/product/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable String productId, @RequestBody Product product) {
        validateAdminRole();
        return ResponseEntity.ok(productService.updateProduct(productId, product));
    }

    @Operation(summary = "Delete a product", description = "Accessible only by Admin")
    @DeleteMapping("/product/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable String productId) {
        validateAdminRole();
        productService.deleteProduct(productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Product deleted successfully");
    }

    @Operation(summary = "Add product stock", description = "Accessible only by Admin")
    @PutMapping("/addStock/{productId}")
    public ResponseEntity<String> addStock(@PathVariable String productId, @RequestParam int quantity) {
        validateAdminRole();
        productService.addQuantity(productId, quantity);
        return ResponseEntity.ok("Stock increased");
    }

    @Operation(summary = "Remove product stock", description = "Accessible only by Admin")
    @PutMapping("/removeStock/{productId}")
    public ResponseEntity<String> removeStock(@PathVariable String productId, @RequestParam int quantity,String valid) {
        validateAdminRole();
        productService.removeQuantity(productId, quantity);
        return ResponseEntity.ok("Stock decreased");
    }

    @Operation(summary = "Check stock availability", description = "Accessible by anyone")
    @GetMapping("/checkStock/{productId}")
    public ResponseEntity<Boolean> checkStock(@PathVariable String productId, @RequestParam int quantity) {
        return ResponseEntity.ok(productService.isStockAvailable(productId, quantity));
    }
}
