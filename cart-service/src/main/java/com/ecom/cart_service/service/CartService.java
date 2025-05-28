package com.ecom.cart_service.service;

import com.ecom.cart_service.dto.ProductDataForCart;
import com.ecom.cart_service.entity.Cart;
import com.ecom.cart_service.entity.CartItem;
import com.ecom.cart_service.exception.CartNotFoundException;
import com.ecom.cart_service.exception.ItemNotFoundException;
import com.ecom.cart_service.exception.ProductNotFoundException;
import com.ecom.cart_service.exception.StockUnavailableException;
import com.ecom.cart_service.feign.ProductClient;
import com.ecom.cart_service.repository.CartRepository;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final ProductClient productClient;
    private final CartRepository cartRepository;

    public Cart addProductToCart(Long userId, String productId) {
        // Get product info
        ProductDataForCart product = getProductDataOrThrow(productId);

        // Get or create cart
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> Cart.builder()
                        .userId(userId)
                        .items(new ArrayList<>())
                        .totalPrice(0)
                        .lastUpdated(LocalDateTime.now())
                        .build());

        List<CartItem> items = cart.getItems();
        Optional<CartItem> existingItem = items.stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst();

        int newQuantity = existingItem.map(CartItem::getQuantity).orElse(0) + 1;

        // Check stock availability from product service
        boolean isAvailable = productClient.checkStock(productId, newQuantity).getBody();
        if (!isAvailable) {
            throw new StockUnavailableException("Quantity not in stock.");
        }

        // Update cart item
        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(newQuantity);
        } else {
            items.add(CartItem.builder()
                    .productId(productId)
                    .quantity(1)
                    .productDetails(product)
                    .build());
        }

        // Update stock in product service
//        productClient.removeStock(productId, 1,"valid");

        // Recalculate total
        cart.setItems(items);
        cart.setTotalPrice(calculateTotal(items));
        cart.setLastUpdated(LocalDateTime.now());

        return cartRepository.save(cart);
    }


    public Cart removeProductFromCart(Long userId, String productId) {
        Cart cart = getCartOrThrow(userId);

        List<CartItem> updatedItems = cart.getItems().stream()
                .filter(item -> !item.getProductId().equals(productId))
                .collect(Collectors.toList());

        cart.setItems(updatedItems);
        cart.setTotalPrice(calculateTotal(updatedItems));
        cart.setLastUpdated(LocalDateTime.now());

        return cartRepository.save(cart);
    }

    public Cart getCartDetails(Long userId) {
        Cart cart = getCartOrThrow(userId);

        List<CartItem> enrichedItems = cart.getItems().stream()
                .map(item -> {
                    ProductDataForCart product = getProductDataOrThrow(item.getProductId());
                    item.setProductDetails(product);
                    return item;
                })
                .collect(Collectors.toList());

        cart.setItems(enrichedItems);
        return cart;
    }

    public void clearCart(Long userId) {
        Cart cart = getCartOrThrow(userId);

        cart.setItems(new ArrayList<>());
        cart.setTotalPrice(0);
        cart.setLastUpdated(LocalDateTime.now());

        cartRepository.save(cart);
    }

    private double calculateTotal(List<CartItem> items) {
        return items.stream()
                .mapToDouble(item -> {
                    ProductDataForCart product = getProductDataOrThrow(item.getProductId());
                    return product.getPrice() * item.getQuantity();
                })
                .sum();
    }

    private ProductDataForCart getProductDataOrThrow(String productId) {
        ResponseEntity<ProductDataForCart> response = productClient.productDataForCart(productId);
        if (response == null || response.getBody() == null) {
            throw new ProductNotFoundException("Product not found with ID: " + productId);
        }
        return response.getBody();
    }

    private Cart getCartOrThrow(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found for user ID: " + userId));
    }

    public void updateQuantity(Long userId, String productId, int quantity) {
        // Get the user's cart
        Cart cart = getCartDetails(userId);

        // Find the item in the cart
        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst();

        if (existingItem.isEmpty()) {
            throw new ItemNotFoundException("Product not found in cart.");
        }

        // Check stock availability
        boolean isAvailable = Boolean.TRUE.equals(productClient.checkStock(productId, quantity).getBody());
        if (!isAvailable) {
            throw new StockUnavailableException("Requested quantity is not in stock.");
        }

        // Update quantity
        int updatedQuantity = existingItem.get().getQuantity()+quantity;
        if(updatedQuantity<=0){
            cart.getItems().remove(existingItem.get());
            return;
        }
        existingItem.get().setQuantity(updatedQuantity);

        // Recalculate total price
        cart.setTotalPrice(calculateTotal(cart.getItems()));
        cart.setLastUpdated(LocalDateTime.now());

        // Save updated cart
        cartRepository.save(cart);
    }
}