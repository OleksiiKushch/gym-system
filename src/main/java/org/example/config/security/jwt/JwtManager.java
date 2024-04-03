package org.example.config.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.example.dao.RevokedTokenDao;
import org.example.entity.RevokedToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import static org.example.constants.PersistenceLayerConstants.NUMBER_OF_ACTIVE_TOKENS_METRIC_NAME;

@Slf4j
@Getter
@Service
public class JwtManager {

    private static final Object STUB = new Object();

    @Value("${application.security.jwt.secret-key}")
    private String jwtSecretKey;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    private final UserDetailsService userDetailsService;
    private final RevokedTokenDao revokedTokenDao;

    private final MeterRegistry meterRegistry;
    private final AtomicInteger numberOfActiveTokens;

    public JwtManager(UserDetailsService userDetailsService, RevokedTokenDao revokedTokenDao, MeterRegistry meterRegistry) {
        this.userDetailsService = userDetailsService;
        this.revokedTokenDao = revokedTokenDao;
        this.meterRegistry = meterRegistry;
        numberOfActiveTokens = new AtomicInteger();
        this.meterRegistry.gauge(NUMBER_OF_ACTIVE_TOKENS_METRIC_NAME, numberOfActiveTokens);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(null, userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        String result = Jwts
                .builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + getJwtExpiration()))
                .signWith(getSignInKey(), Jwts.SIG.HS256)
                .compact();
        getNumberOfActiveTokens().incrementAndGet();
        return result;
    }

    public void invalidateToken(String token) {
        getRevokedTokenDao().save(RevokedToken.builder()
                        .token(token)
                        .expirationDateTime(extractExpiration(token))
                .build());
        getNumberOfActiveTokens().decrementAndGet();
    }

    public Optional<UserDetails> isTokenValid(String token) {
        try {
            if (!getRevokedTokenDao().existsByToken(token) && !isTokenExpired(token)) {
                return Optional.of(getUserDetailsService().loadUserByUsername(extractUsername(token)));
            }
        } catch (ExpiredJwtException exception) {
            log.info(exception.getMessage());
        }
        return Optional.empty();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(extractAllClaims(token));
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSignInKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(getJwtSecretKey()));
    }
}
