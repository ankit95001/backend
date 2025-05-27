package com.ecom.cart_service.controller;

import com.ecom.cart_service.entity.Cart;
import com.ecom.cart_service.entity.UserContext;
import com.ecom.cart_service.exception.UnauthorizedAccessException;
import com.ecom.cart_service.service.CartService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@Tag(name = "Cart API", description = "Operations related to the shopping cart")
public class CartController {

    private final CartService cartService;
    private final UserContext userContext;

    @Operation(summary = "Add a product to the user's cart")
    @CircuitBreaker(name="productCB", fallbackMethod = "fallbackProductCB")
    @PostMapping("/{userId}/add/{productId}")
    public ResponseEntity<Cart> addProductToCart(
            @Parameter(description = "ID of the user")
            @PathVariable @Min(1) Long userId,

            @Parameter(description = "ID of the product to be added")
            @PathVariable String productId) {

        validateUserAccess(userId);
        Cart updatedCart = cartService.addProductToCart(userId, productId);
        return ResponseEntity.ok(updatedCart);
    }

    @Operation(summary = "Remove a product from the user's cart")
    @DeleteMapping("/{userId}/remove/{productId}")
    public ResponseEntity<Cart> removeProductFromCart(
            @Parameter(description = "ID of the user")
            @PathVariable @Min(1) Long userId,

            @Parameter(description = "ID of the product to be removed")
            @PathVariable String productId) {

        validateUserAccess(userId);
        Cart updatedCart = cartService.removeProductFromCart(userId, productId);
        return ResponseEntity.ok(updatedCart);
    }

    @Operation(summary = "Get the current contents of the user's cart")
    @GetMapping("/{userId}")
    public ResponseEntity<Cart> viewCart(
            @Parameter(description = "ID of the user")
            @PathVariable @Min(1) Long userId) {

        validateUserAccess(userId);
        Cart cart = cartService.getCartDetails(userId);
        return ResponseEntity.ok(cart);
    }

    @Operation(summary = "Clear the user's entire cart")
    @DeleteMapping("/{userId}/clear")
    public ResponseEntity<String> clearCart(
            @Parameter(description = "ID of the user")
            @PathVariable @Min(1) Long userId) {

        validateUserAccess(userId);
        cartService.clearCart(userId);
        return ResponseEntity.ok("Cart cleared successfully.");
    }

    private void validateUserAccess(Long userId) {
        if (userContext.getUserId() == null || !userId.equals(userContext.getUserId())) {
            throw new UnauthorizedAccessException("You are not authorized to access this cart.");
        }
    }
}