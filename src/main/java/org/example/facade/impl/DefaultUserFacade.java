package org.example.facade.impl;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.example.entity.User;
import org.example.exception.AppException;
import org.example.exception.NotFoundException;
import org.example.facade.UserFacade;
import org.example.service.AuthenticateTokenService;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import static org.example.constants.GeneralConstants.PASSWORD_DOES_NOT_MUTCH_EXCEPTION_MSG;
import static org.example.constants.GeneralConstants.USERNAME_OR_PASSWORD_DO_NOT_MUTCH_EXCEPTION_MSG;
import static org.example.constants.GeneralConstants.USER_NOT_FOUND_EXCEPTION_MSG;

@Getter
@Component
@Primary
public class DefaultUserFacade implements UserFacade {

    private UserService userService;
    private AuthenticateTokenService authenticateTokenService;

    @Override
    public String login(String username, String password) {
        User user = getUserService().authenticateUser(username, password)
                .orElseThrow(() -> new AppException(USERNAME_OR_PASSWORD_DO_NOT_MUTCH_EXCEPTION_MSG));
        return getAuthenticateTokenService().generate(user);
    }

    @Override
    public void logout(String token) {
        getAuthenticateTokenService().deactivate(token);
    }

    @Override
    public boolean authorizationCurrentUser(String token) {
        return getAuthenticateTokenService().verify(token);
    }

    @Override
    public void changePassword(String username, String currentPassword, String newPassword) {
        User user = getUserByUsernameOrThrowException(username);
        if (!user.getPassword().equals(currentPassword)) {
            throw new AppException(PASSWORD_DOES_NOT_MUTCH_EXCEPTION_MSG);
        }
        user.setPassword(newPassword);
        getUserService().updateUser(user);
    }

    @Override
    public void toggleUserActivation(String username) {
        User currentUser = getUserByUsernameOrThrowException(username);
        currentUser.setActive(!currentUser.getIsActive());
        getUserService().updateUser(currentUser);
    }

    protected void processNewUserProfile(User user) {
        user.setActive(Boolean.TRUE);
        user.setUsername(getUserService().calculateUsername(user));
        if (StringUtils.isEmpty(user.getPassword())) {
            user.setPassword(getUserService().generateRandomPassword());
        }
    }

    protected User getUserByUsernameOrThrowException(String username) {
        return getUserService().getUserForUsername(username)
                .orElseThrow(() -> new NotFoundException(formExceptionMessage(USER_NOT_FOUND_EXCEPTION_MSG, username)));
    }

    protected String formExceptionMessage(String message, Object... args) {
        return String.format(message, args);
    }

    @Autowired
    public void setUserService(@Lazy UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setAuthenticateTokenService(@Lazy AuthenticateTokenService authenticateTokenService) {
        this.authenticateTokenService = authenticateTokenService;
    }
}
