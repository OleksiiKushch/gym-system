package org.example.service;

import org.example.entity.User;

public interface AuthenticateTokenService {

    String generate(User user);
    boolean verify(String user);
    void deactivate(String token);
}
