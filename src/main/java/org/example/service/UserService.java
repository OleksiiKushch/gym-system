package org.example.service;

import org.example.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {

    String calculateUsername(User user);
    String generateRandomPassword();
    Optional<User> getUserForUsername(String username);
    List<User> getAllUsers();
    void updateUser(User user);
}
