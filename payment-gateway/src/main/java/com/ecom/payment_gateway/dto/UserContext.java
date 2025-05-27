package com.ecom.payment_gateway.dto;

import lombok.*;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@Component
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserContext {
    private String email;
    private String role;
    private Long userId;
}

