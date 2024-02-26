package org.example.config.security.jwt;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import static org.example.constants.GeneralConstants.AUTHORIZATION_HEADER_KEY;
import static org.example.constants.GeneralConstants.AUTHORIZATION_HEADER_VALUE_PREFIX;

@Component
public class JwtAuthenticationHelper {

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
