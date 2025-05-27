package com.ecom.order_service.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientInterceptor implements RequestInterceptor {

    @Autowired
    private HttpServletRequest request;

    @Override
    public void apply(RequestTemplate template) {
        String authHeader = request.getHeader("Authorization");

        // Forward the Authorization header
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            template.header("Authorization", authHeader);
        }

        // Add X-Source header to simulate coming from API Gateway
        template.header("X-Source", "api-gateway");
    }
}
