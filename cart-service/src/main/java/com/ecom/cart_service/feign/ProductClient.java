package com.ecom.cart_service.feign;

import com.ecom.cart_service.dto.ProductDataForCart;
//import com.ecom.cart_service.fallback.ProductClientFallback;
import org.bson.types.ObjectId;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "PRODUCT-SERVICE")
public interface ProductClient {

    @GetMapping("/products/productForCart/{id}")
    public ResponseEntity<ProductDataForCart> productDataForCart(@PathVariable String id);

    @GetMapping("/products/checkStock/{productId}")
    public ResponseEntity<Boolean> checkStock(@PathVariable String productId, @RequestParam int quantity);

    @PutMapping("/products/removeStock/{productId}")
    public ResponseEntity<String> removeStock(@PathVariable String productId, @RequestParam int quantity);

    @PutMapping("/products/addStock/{productId}")
    public ResponseEntity<String> addStock(@PathVariable String productId, @RequestParam int quantity);
}
