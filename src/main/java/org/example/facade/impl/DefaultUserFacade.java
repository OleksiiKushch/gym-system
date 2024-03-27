package org.example.facade.impl;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.example.config.security.jwt.JwtManager;
import org.example.entity.User;
import org.example.exception.AppException;
import org.example.exception.NotFoundException;
import org.example.facade.UserFacade;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import static org.example.constants.GeneralConstants.PASSWORD_DOES_NOT_MUTCH_EXCEPTION_MSG;
import static org.example.constants.GeneralConstants.USER_NOT_FOUND_EXCEPTION_MSG;

@Getter
@Component
@Primary
public class DefaultUserFacade implements UserFacade {

    private UserService userService;
    private JwtManager jwtManager;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;

    @Override
    public String login(String username, String password) {
        getAuthenticationManager().authenticate(createAuthentication(username, password));
        User user = getUserByUsernameOrThrowException(username);
        return getJwtManager().generateToken(user);
    }

    @Override
    public void changePassword(String username, String currentPassword, String newPassword) {
        User user = getUserByUsernameOrThrowException(username);
        if (!getPasswordEncoder().matches(currentPassword, user.getPassword())) {
            throw new AppException(PASSWORD_DOES_NOT_MUTCH_EXCEPTION_MSG);
        }
        user.setPassword(getPasswordEncoder().encode(newPassword));
        getUserService().updateUser(user);
    }

    @Override
    public void toggleUserActivation(String username) {
        User currentUser = getUserByUsernameOrThrowException(username);
        currentUser.setActive(!currentUser.getIsActive());
        getUserService().updateUser(currentUser);
    }

    protected String processNewUserProfile(User user) {
        user.setActive(Boolean.TRUE);
        user.setUsername(getUserService().calculateUsername(user));
        String password = user.getPassword();
        if (StringUtils.isEmpty(password)) {
            password = getUserService().generateRandomPassword();
        }
        user.setPassword(getPasswordEncoder().encode(password));
        return password;
    }

    protected User getUserByUsernameOrThrowException(String username) {
        return getUserService().getUserForUsername(username)
                .orElseThrow(() -> new NotFoundException(formExceptionMessage(USER_NOT_FOUND_EXCEPTION_MSG, username)));
    }

    protected String formExceptionMessage(String message, Object... args) {
        return String.format(message, args);
    }

    private Authentication createAuthentication(String username, String password) {
        return new UsernamePasswordAuthenticationToken(username, password);
    }

    @Autowired
    public void setUserService(@Lazy UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setJwtManager(JwtManager jwtManager) {
        this.jwtManager = jwtManager;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }
}
