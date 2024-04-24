package com.example.trainermanager.config.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Slf4j
@Getter
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtManager jwtManager;
    private final JwtAuthenticationHelper jwtAuthenticationHelper;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String authHeader = getJwtAuthenticationHelper().getAuthenticationHeader(request);
        if (Objects.isNull(authHeader) || getJwtAuthenticationHelper().isNotBearerAuthentication(authHeader)) {
            filterChain.doFilter(request, response);
            return;
        }
        if (Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
            String jwt = getJwtAuthenticationHelper().extractTokenFromHeader(authHeader);
            getJwtManager().checkToken(jwt);
            SecurityContextHolder.getContext().setAuthentication(new JwtAuthentication(null));
        }
        filterChain.doFilter(request, response);
    }
}
