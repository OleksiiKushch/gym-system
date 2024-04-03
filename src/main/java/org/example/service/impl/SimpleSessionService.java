package org.example.service.impl;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.User;
import org.example.service.SessionService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

import static org.example.constants.GeneralConstants.CURRENT_USER_ATTR;
import static org.example.constants.GeneralConstants.PUT_USER_TO_SESSION_LOG_MSG;

@Slf4j
@Getter
@Service
public class SimpleSessionService implements SessionService {

    private final Map<String, Object> simpleSession;

    public SimpleSessionService(Map<String, Object> simpleSession) {
        this.simpleSession = simpleSession;
    }

    @Override
    public Optional<?> getAttribute(String key) {
        return Optional.ofNullable(getSimpleSession().get(key));
    }

    @Override
    public void setAttribute(String key, Object value) {
        getSimpleSession().put(key, value);
    }

    @Override
    public void removeAttribute(String key) {
        getSimpleSession().remove(key);
    }

    @Override
    public Optional<User> getCurrentUser() {
        return getAttribute(CURRENT_USER_ATTR)
                .filter(User.class::isInstance)
                .map(User.class::cast);
    }

    @Override
    public void setCurrentUser(User user) {
        setAttribute(CURRENT_USER_ATTR, user);
        log.info(PUT_USER_TO_SESSION_LOG_MSG, user.getUsername());
    }
}
