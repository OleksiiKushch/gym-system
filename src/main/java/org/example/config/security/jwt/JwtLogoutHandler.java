package org.example.config.security.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Getter
@RequiredArgsConstructor
@Component
public class JwtLogoutHandler implements LogoutHandler {

    private final JwtManager jwtManager;
    private final JwtAuthenticationHelper jwtAuthenticationHelper;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String authHeader = getJwtAuthenticationHelper().getAuthenticationHeader(request);
        if (Objects.isNull(authHeader) || getJwtAuthenticationHelper().isNotBearerAuthentication(authHeader)) {
            return;
        }
        String token = getJwtAuthenticationHelper().extractTokenFromHeader(authHeader);
        if (getJwtManager().isTokenValid(token).isPresent()) {
            getJwtManager().invalidateToken(token);
        }
    }
}
