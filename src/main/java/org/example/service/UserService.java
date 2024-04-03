package org.example.service;

import org.example.entity.User;

import java.util.List;

public interface UserService {

    String calculateUsername(User user);
    String generateRandomPassword();
    List<User> getAllUsers();
}
