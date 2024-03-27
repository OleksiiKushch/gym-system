package org.example.config.security.bruteforceprevent;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import jakarta.annotation.Nonnull;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Getter
@Service
public class LoginAttemptService {

    private static final int INDEX_OF_FIRST_ARGUMENT = 0;
    private static final String DELIMETER = ",";
    private static final String X_FORWARDED_FOR_HEADER_PROPERTY = "X-Forwarded-For";

    private LoadingCache<String, Integer> attemptsCache;

    @Value("${application.security.bruteforceprevent.login.max-attempts}")
    private int maxAttempts;

    @Value("${application.security.bruteforceprevent.login.block-time}")
    private long blockTime;

    private HttpServletRequest request;

    @PostConstruct
    public void init() {
        attemptsCache = CacheBuilder.newBuilder().expireAfterWrite(getBlockTime(), TimeUnit.MILLISECONDS).build(new CacheLoader<>() {

            @Override
            public @Nonnull Integer load(@Nonnull String key) {
                return 0;
            }
        });
    }

    public void loginFailed(String key) {
        int attempts;
        try {
            attempts = getAttemptsCache().get(key);
        } catch (ExecutionException e) {
            attempts = 0;
        }
        getAttemptsCache().put(key, ++attempts);
    }

    public boolean isBlocked() {
        try {
            return getAttemptsCache().get(getClientIP()) >= getMaxAttempts();
        } catch (ExecutionException e) {
            return false;
        }
    }

    public String retrieveXForwardedForPropertyFromHeader(HttpServletRequest request) {
        return request.getHeader(X_FORWARDED_FOR_HEADER_PROPERTY);
    }

    /**
     * Get the client IP address.
     * Format of the X-Forwarded-For header is:<br/>
     * X-Forwarded-For: [clientIpAddress], proxy1, proxy2
     */
    public String retrieveClientIdFromXForwardedProperty(String xfHeader) {
        return xfHeader.split(DELIMETER)[INDEX_OF_FIRST_ARGUMENT];
    }

    private String getClientIP() {
        String xfHeader = retrieveXForwardedForPropertyFromHeader(getRequest());
        if (xfHeader == null || xfHeader.isEmpty() || !xfHeader.contains(getRequest().getRemoteAddr())) {
            return getRequest().getRemoteAddr();
        }
        return retrieveClientIdFromXForwardedProperty(xfHeader);
    }

    @Autowired
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }
}
