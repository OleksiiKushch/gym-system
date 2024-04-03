package org.example.service.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.User;
import org.example.service.AuthenticateTokenService;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

import static org.example.constants.GeneralConstants.DEACTIVATE_TOKEN_LOG_MSG;
import static org.example.constants.GeneralConstants.NEW_AUTHORIZATION_TOKEN_LOG_MSG;
import static org.example.constants.GeneralConstants.STORED_TOKENS_LOG_MSG;

@Slf4j
@Getter
@RequiredArgsConstructor
@Service
public class SimpleAuthenticateTokenService implements AuthenticateTokenService {

    private static final String DELIMETER = "_";

    private final Set<String> tokenStore;

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
        return getTokenStore().contains(token);
    }

    @Override
    public void deactivate(String token) {
        log.info(DEACTIVATE_TOKEN_LOG_MSG, getTokenStore());
        getTokenStore().remove(token);
    }

    private String formToken(User user) {
        return user.getUsername() + DELIMETER + UUID.randomUUID();
    }
}
