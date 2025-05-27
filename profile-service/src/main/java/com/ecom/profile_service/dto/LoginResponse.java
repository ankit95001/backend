package com.ecom.profile_service.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoginResponse {
    private String token;
    private String email;
    private Long id;
    private String role;
}
