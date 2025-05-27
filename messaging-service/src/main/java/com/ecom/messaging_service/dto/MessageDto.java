package com.ecom.messaging_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {
    private String email;
    private String subject;
    private String message;
}
