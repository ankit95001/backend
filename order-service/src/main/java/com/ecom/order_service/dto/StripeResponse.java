package com.ecom.order_service.dto;

import lombok.*;
import org.bson.types.ObjectId;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class StripeResponse {

    private String status;
    private String message;
    private String sessionId;
    private String sessionUrl;
    private ObjectId orderId;
}
