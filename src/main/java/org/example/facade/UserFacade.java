package org.example.facade;

public interface UserFacade {

    String login(String username, String password);
    void changePassword(String username, String currentPassword, String newPassword);
    void toggleUserActivation(String username);
}
