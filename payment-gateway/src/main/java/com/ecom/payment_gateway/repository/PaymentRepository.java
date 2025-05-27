package com.ecom.payment_gateway.repository;

import com.ecom.payment_gateway.dto.PaymentDetails;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends MongoRepository<PaymentDetails, String> {
    Optional<PaymentDetails> findBySessionId(String sessionId);
}

