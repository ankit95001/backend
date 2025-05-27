package com.ecom.order_service.config;



import com.ecom.order_service.entity.UserContext;
import com.ecom.order_service.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserContext userContext;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                String email = jwtUtil.extractEmail(token);
                String role = jwtUtil.extractRole(token);
                Long userId = jwtUtil.extractUserId(token);

                System.out.println("Token validated successfully");
                System.out.println("Email: " + email);
                System.out.println("Role: " + role);
                System.out.println("userId: " + userId);
                userContext.setEmail(email);
                userContext.setRole(role);
                userContext.setUserId(userId);

                String source = request.getHeader("X-Source");
                if (!"api-gateway".equalsIgnoreCase(source)) {
                    System.out.println("❌ Request not from API Gateway. Rejecting.");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }

            } catch (ExpiredJwtException e) {
                System.out.println("❌ Token expired.");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            } catch (Exception e) {
                System.out.println("❌ Invalid token.");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        } else {
            System.out.println("❌ Missing or malformed Authorization header.");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(request, response);
    }
}

