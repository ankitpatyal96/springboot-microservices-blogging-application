package com.demo.gateway.filter;

import com.demo.gateway.exceptions.TokenException;
import com.demo.gateway.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

    @Autowired
    private RoutingValidator routingValidator;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public AuthFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if (routingValidator.isSecured.test(exchange.getRequest())) {
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new TokenException("Authorization header is missing");
                }

                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                }
                try {
                    jwtTokenProvider.validateToken(authHeader);
                } catch (Exception e) {
                    throw new TokenException("Invalid Token");
                }
            }
            return chain.filter(exchange);
        });
    }

    public static class Config {

    }
}