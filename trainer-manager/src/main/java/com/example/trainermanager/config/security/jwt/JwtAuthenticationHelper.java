package com.example.trainermanager.config.security.jwt;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationHelper {

    private static final String AUTHORIZATION_HEADER_KEY = "Authorization";
    private static final String AUTHORIZATION_HEADER_VALUE_PREFIX = "Bearer ";

    public String getAuthenticationHeader(HttpServletRequest request) {
        return request.getHeader(AUTHORIZATION_HEADER_KEY);
    }

    public String extractTokenFromHeader(String authHeader) {
        return authHeader.substring(AUTHORIZATION_HEADER_VALUE_PREFIX.length());
    }

    public boolean isNotBearerAuthentication(String authHeader) {
        return !authHeader.startsWith(AUTHORIZATION_HEADER_VALUE_PREFIX);
    }
}
