package com.ecom.profile_service.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MessageDto {
    private String email;
    private String subject;
    private String message;
}
