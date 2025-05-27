package com.ecom.payment_gateway.dto;

import lombok.*;

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
}
