package org.example.service;

import org.example.entity.User;

import java.util.Optional;

public interface SessionService {

    Optional<?> getAttribute(String key);
    void setAttribute(String key, Object value);
    void removeAttribute(String key);
    Optional<User> getCurrentUser();
    void setCurrentUser(User user);
}
