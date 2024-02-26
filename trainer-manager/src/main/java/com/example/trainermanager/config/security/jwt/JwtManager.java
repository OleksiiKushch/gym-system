package com.example.trainermanager.config.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Slf4j
@Getter
@Service
public class JwtManager {

    private static final String TOKEN_HAS_EXPIRED_MSG = "Token has expired";

    @Value("${application.security.jwt.secret-key}")
    private String jwtSecretKey;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    public void checkToken(String token) {
        try {
            if (isTokenExpired(token)) {
                throw new JwtException(TOKEN_HAS_EXPIRED_MSG);
            }
        } catch (JwtException exception) {
            log.info(exception.getMessage());
            throw new JwtException(exception.getMessage());
        }
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
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
