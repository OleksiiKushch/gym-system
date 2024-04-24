package org.example.config.security;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

public record JwtAuthenticationDetails(String jwtToken, WebAuthenticationDetails webAuthenticationDetails) {
}
