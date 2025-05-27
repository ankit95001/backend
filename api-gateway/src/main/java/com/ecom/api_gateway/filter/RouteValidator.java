package com.ecom.api_gateway.filter;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.http.server.reactive.ServerHttpRequest;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    private static final List<String> openApiEndpoints = List.of(
            "/auth/**",
            "/eureka/**",
            "/api/**",
            "/openProduct/**"
    );

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints
                    .stream()
                    .noneMatch(uri -> pathMatcher.match(uri, request.getURI().getPath()));
}