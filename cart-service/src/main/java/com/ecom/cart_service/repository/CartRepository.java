package com.ecom.cart_service.repository;

import com.ecom.cart_service.entity.Cart;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends MongoRepository<Cart, ObjectId> {
    Optional<Cart> findByUserId(Long userId);
}

