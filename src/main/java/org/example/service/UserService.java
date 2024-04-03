package org.example.service;

import org.example.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    String calculateUsername(User user);
    String generateRandomPassword();
    Optional<User> getUserForUsername(String username);
    List<User> getAllUsers();
    Optional<User> authenticateUser(String username, String password);
    void updateUser(User user);
}
