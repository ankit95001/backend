package com.ecom.order_service.repository;

import com.ecom.order_service.entity.Order;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
@Repository
public interface OrderRepository extends MongoRepository<Order, ObjectId> {
    List<Order> findByUserId(Long userId);
    Optional<Order> findBySessionId(String sessionId);
}

