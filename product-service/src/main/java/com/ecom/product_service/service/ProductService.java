package com.ecom.product_service.service;

import com.ecom.product_service.dto.ProductDataForCart;
import com.ecom.product_service.entity.Product;
import com.ecom.product_service.exception.CustomException;
import com.ecom.product_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final SequenceGenerator sg;

    public List<Product> getAllProducts() {
        List<Product> productList = productRepository.findAll();
        if(productList.isEmpty()){
            throw new CustomException("No product found!",204);
        }

        return productList;
    }

    public List<Product> getProductsByCategory(String category) {
        List<Product> productList = productRepository.findByCategory(category);
        if(productList.isEmpty()){
            throw new NoSuchElementException("No product found for this category!");
        }
        return productList;
    }

    public Product getProductById(String productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("Product not found"));
    }

    public String saveProduct(Product product) {

        try{
            product.setProductId(sg.getSequenceNumber("PRODUCT_SEQUENCE"));
            productRepository.save(product);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
        return "Product saved successfully!";
    }

    public Product updateProduct(String productId, Product updatedProduct) {
        Product existingProduct = getProductById(productId);

        existingProduct.setName(updatedProduct.getName());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setCategory(updatedProduct.getCategory());
        existingProduct.setBrand(updatedProduct.getBrand());
        existingProduct.setStockQuantity(updatedProduct.getStockQuantity());
        existingProduct.setImage(updatedProduct.getImage());

        return productRepository.save(existingProduct);
    }

    public void deleteProduct(String productId) {
        if (!productRepository.existsById(productId)) {
            throw new NoSuchElementException("Product with ID " + productId + " not found");
        }
        productRepository.deleteById(productId);
    }

    public ProductDataForCart cartProduct(String productId) {
        Product product = getProductById(productId);
        return new ProductDataForCart(productId, product.getName(), product.getPrice(),product.getImage());
    }

    public void addQuantity(String productId, int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be greater than 0");
        Product existingProduct = getProductById(productId);
        existingProduct.setStockQuantity(existingProduct.getStockQuantity() + quantity);
        productRepository.save(existingProduct);
    }

    public void removeQuantity(String productId, int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be greater than 0");
        Product existingProduct = getProductById(productId);
        if (existingProduct.getStockQuantity() < quantity) {
            throw new IllegalArgumentException("Insufficient stock");
        }
        existingProduct.setStockQuantity(existingProduct.getStockQuantity() - quantity);
        productRepository.save(existingProduct);
    }

    public boolean isStockAvailable(String productId, int requiredQuantity) {
        Product product = productRepository
                .findById(productId)
                .orElseThrow(() -> new NoSuchElementException("Product not found"));
        return product.getStockQuantity() >= requiredQuantity;
    }
}