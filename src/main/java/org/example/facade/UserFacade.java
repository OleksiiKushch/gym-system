package org.example.facade;

public interface UserFacade {

    String login(String username, String password);
    void logout(String token);
    boolean authorizationCurrentUser(String token);
    void changePassword(String username, String currentPassword, String newPassword);
    void toggleUserActivation(String username);
}
