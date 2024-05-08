package com.demo.gateway.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RoutingValidator {
    public static final List<String> publicRoutes = List.of(
            "/api/auth/register",
            "/api/auth/token",
            "/api/auth/validate",
            "/eureka"
    );

    public Predicate<ServerHttpRequest> isSecured =
            request -> publicRoutes
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));
}
