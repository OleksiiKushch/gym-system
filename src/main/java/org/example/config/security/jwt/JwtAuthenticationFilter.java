package org.example.config.security.jwt;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.example.constants.PersistenceLayerConstants.TOKENS_VERIFICATION_COUNT_METRIC_NAME;
import static org.example.constants.PersistenceLayerConstants.USERNAME_PARAM;

@Slf4j
@Getter
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtManager jwtManager;
    private final JwtAuthenticationHelper jwtAuthenticationHelper;
    private final MeterRegistry meterRegistry;

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
            Optional<UserDetails> userDetailsOptional = getJwtManager().isTokenValid(jwt);
            if (userDetailsOptional.isPresent()) {
                SecurityContextHolder.getContext().setAuthentication(prepareAuthToken(userDetailsOptional.get(), request));
                registerAuthorizedRequest(jwt);
            }
        }
        filterChain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken prepareAuthToken(UserDetails userDetails, HttpServletRequest request) {
        var authToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return authToken;
    }

    private void registerAuthorizedRequest(String token) {
        meterRegistry.counter(TOKENS_VERIFICATION_COUNT_METRIC_NAME,
                        List.of(Tag.of(USERNAME_PARAM, token)))
                .increment();
    }
}
