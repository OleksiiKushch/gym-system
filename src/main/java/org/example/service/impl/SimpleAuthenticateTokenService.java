package org.example.service.impl;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.User;
import org.example.service.AuthenticateTokenService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.example.constants.GeneralConstants.DEACTIVATE_TOKEN_LOG_MSG;
import static org.example.constants.GeneralConstants.NEW_AUTHORIZATION_TOKEN_LOG_MSG;
import static org.example.constants.GeneralConstants.STORED_TOKENS_LOG_MSG;
import static org.example.constants.PersistenceLayerConstants.NUMBER_OF_ACTIVE_TOKENS_METRIC_NAME;
import static org.example.constants.PersistenceLayerConstants.TOKENS_VERIFICATION_COUNT_METRIC_NAME;
import static org.example.constants.PersistenceLayerConstants.USERNAME_PARAM;

@Slf4j
@Getter
@Service
public class SimpleAuthenticateTokenService implements AuthenticateTokenService {

    private static final String DELIMETER = "_";

    private final MeterRegistry meterRegistry;
    private final Set<String> tokenStore;

    public SimpleAuthenticateTokenService(Set<String> tokenStore, MeterRegistry meterRegistry) {
        this.tokenStore = tokenStore;
        this.meterRegistry = meterRegistry;
        this.meterRegistry.gauge(NUMBER_OF_ACTIVE_TOKENS_METRIC_NAME, tokenStore, Set::size);
    }

    @Override
    public String generate(User user) {
        String token = formToken(user);
        log.info(NEW_AUTHORIZATION_TOKEN_LOG_MSG, token);
        getTokenStore().add(token);
        log.info(STORED_TOKENS_LOG_MSG, getTokenStore());
        return token;
    }

    @Override
    public boolean verify(String token) {
        boolean tokenExists = getTokenStore().contains(token);
        if (tokenExists) {
            meterRegistry.counter(TOKENS_VERIFICATION_COUNT_METRIC_NAME,
                            List.of(Tag.of(USERNAME_PARAM, token.split(DELIMETER)[0])))
                    .increment();
        }
        return tokenExists;
    }

    @Override
    public void deactivate(String token) {
        log.info(DEACTIVATE_TOKEN_LOG_MSG, token);
        getTokenStore().remove(token);
        log.info(STORED_TOKENS_LOG_MSG, getTokenStore());
    }

    private String formToken(User user) {
        return user.getUsername() + DELIMETER + UUID.randomUUID();
    }
}
