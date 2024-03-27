package org.example.facade.impl;

import org.example.config.security.jwt.JwtManager;
import org.example.entity.User;
import org.example.exception.AppException;
import org.example.exception.NotFoundException;
import org.example.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.example.constants.GeneralConstants.PASSWORD_DOES_NOT_MUTCH_EXCEPTION_MSG;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultUserFacadeUnitTest {

    private static final String TEST_USERNAME = "John.Doe";
    private static final String TEST_PASSWORD = "password";
    private static final String TEST_NEW_PASSWORD = "newPassword";
    private static final String ENCODED_NEW_PASSWORD = "encodedNewPassword";
    private static final String TEST_SOME_PASSWORD = "somePassword";
    private static final String TEST_AUTORIZATION_TOKEN = "testToken";

    private static final String USER_NOT_FOUND_EXPECTED_EXCEPTION_MSG =
            "User with username '" + TEST_USERNAME + "' not found";

    @InjectMocks
    DefaultUserFacade testInstance;

    @Mock
    UserService userService;
    @Mock
    JwtManager jwtManager;
    @Mock
    AuthenticationManager authenticationManager;
    @Mock
    PasswordEncoder passwordEncoder;

    @Captor
    ArgumentCaptor<UsernamePasswordAuthenticationToken> usernamePasswordAuthenticationTokenArgumentCaptor;

    @Mock
    User user;
    @Mock
    Authentication authentication;

    @Test
    void shouldLogin() {
        when(authenticationManager.authenticate(usernamePasswordAuthenticationTokenArgumentCaptor.capture())).thenReturn(authentication);
        when(userService.getUserForUsername(TEST_USERNAME)).thenReturn(Optional.of(user));
        when(jwtManager.generateToken(user)).thenReturn(TEST_AUTORIZATION_TOKEN);

        String actualResult = testInstance.login(TEST_USERNAME, TEST_PASSWORD);

        assertEquals(TEST_USERNAME, usernamePasswordAuthenticationTokenArgumentCaptor.getValue().getPrincipal());
        assertEquals(TEST_PASSWORD, usernamePasswordAuthenticationTokenArgumentCaptor.getValue().getCredentials());
        assertEquals(TEST_AUTORIZATION_TOKEN, actualResult);
    }

    @Test
    void shouldChangePassword() {
        when(userService.getUserForUsername(TEST_USERNAME)).thenReturn(Optional.of(user));
        when(user.getPassword()).thenReturn(TEST_PASSWORD);
        when(passwordEncoder.matches(TEST_PASSWORD, TEST_PASSWORD)).thenReturn(true);
        when(passwordEncoder.encode(TEST_NEW_PASSWORD)).thenReturn(ENCODED_NEW_PASSWORD);

        testInstance.changePassword(TEST_USERNAME, TEST_PASSWORD, TEST_NEW_PASSWORD);

        verify(user).setPassword(ENCODED_NEW_PASSWORD);
        verify(userService).updateUser(user);
    }

    @Test
    void shouldDontChangePassword_whenUserNotFound() {
        when(userService.getUserForUsername(TEST_USERNAME)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NotFoundException.class, () ->
                testInstance.changePassword(TEST_USERNAME, TEST_SOME_PASSWORD, TEST_NEW_PASSWORD));

        assertEquals(USER_NOT_FOUND_EXPECTED_EXCEPTION_MSG, exception.getMessage());
    }

    @Test
    void shouldDontChangePassword_whenCurrentAndNewPasswordDoesNotMatch() {
        when(userService.getUserForUsername(TEST_USERNAME)).thenReturn(Optional.of(user));
        when(user.getPassword()).thenReturn(TEST_PASSWORD);
        when(passwordEncoder.matches(TEST_SOME_PASSWORD, TEST_PASSWORD)).thenReturn(false);

        Exception exception = assertThrows(AppException.class, () ->
                testInstance.changePassword(TEST_USERNAME, TEST_SOME_PASSWORD, TEST_NEW_PASSWORD));

        assertEquals(PASSWORD_DOES_NOT_MUTCH_EXCEPTION_MSG, exception.getMessage());
    }

    @Test
    void shouldToggleUserActivation_whenCurrentStatusIsTrue() {
        when(userService.getUserForUsername(TEST_USERNAME)).thenReturn(Optional.of(user));
        when(user.getIsActive()).thenReturn(true);

        testInstance.toggleUserActivation(TEST_USERNAME);

        verify(user).setActive(false);
        verify(userService).updateUser(user);
    }

    @Test
    void shouldToggleUserActivation_whenCurrentStatusIsFalse() {
        when(userService.getUserForUsername(TEST_USERNAME)).thenReturn(Optional.of(user));
        when(user.getIsActive()).thenReturn(false);

        testInstance.toggleUserActivation(TEST_USERNAME);

        verify(user).setActive(true);
        verify(userService).updateUser(user);
    }

    @Test
    void toggleUserActivation_shouldThrowException_when() {
        when(userService.getUserForUsername(TEST_USERNAME)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NotFoundException.class, () ->
                testInstance.toggleUserActivation(TEST_USERNAME));

        assertEquals(USER_NOT_FOUND_EXPECTED_EXCEPTION_MSG, exception.getMessage());
    }
}