package org.example.facade.impl;

import org.example.entity.User;
import org.example.exception.AppException;
import org.example.exception.NotFoundException;
import org.example.service.AuthenticateTokenService;
import org.example.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.example.constants.GeneralConstants.PASSWORD_DOES_NOT_MUTCH_EXCEPTION_MSG;
import static org.example.constants.GeneralConstants.USERNAME_OR_PASSWORD_DO_NOT_MUTCH_EXCEPTION_MSG;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultUserFacadeUnitTest {

    private static final String TEST_USERNAME = "John.Doe";
    private static final String TEST_PASSWORD = "password";
    private static final String TEST_NEW_PASSWORD = "newPassword";
    private static final String TEST_SOME_PASSWORD = "somePassword";
    private static final String TEST_AUTORIZATION_TOKEN = "testToken";

    private static final String USER_NOT_FOUND_EXPECTED_EXCEPTION_MSG =
            "User with username '" + TEST_USERNAME + "' not found";

    @InjectMocks
    DefaultUserFacade testInstance;

    @Mock
    UserService userService;
    @Mock
    AuthenticateTokenService authenticateTokenService;

    @Mock
    User user;

    @Test
    void shouldLogin() {
        when(userService.authenticateUser(TEST_USERNAME, TEST_PASSWORD)).thenReturn(Optional.of(user));
        when(authenticateTokenService.generate(user)).thenReturn(TEST_AUTORIZATION_TOKEN);

        String actualResult = testInstance.login(TEST_USERNAME, TEST_PASSWORD);

        assertEquals(TEST_AUTORIZATION_TOKEN, actualResult);
    }

    @Test
    void login_shouldThrowException_whenUserCredentialsAreInvalid() {
        when(userService.authenticateUser(TEST_USERNAME, TEST_PASSWORD)).thenReturn(Optional.empty());

        Exception exception = assertThrows(AppException.class, () ->
                testInstance.login(TEST_USERNAME, TEST_PASSWORD));

        assertEquals(USERNAME_OR_PASSWORD_DO_NOT_MUTCH_EXCEPTION_MSG, exception.getMessage());
    }

    @Test
    void shouldLogout() {
        testInstance.logout(TEST_AUTORIZATION_TOKEN);

        verify(authenticateTokenService).deactivate(TEST_AUTORIZATION_TOKEN);
    }

    @Test
    void authorizationCurrentUser_shouldReturnTrue_whenUserDataMatch() {
        when(authenticateTokenService.verify(TEST_AUTORIZATION_TOKEN)).thenReturn(true);

        boolean actualResult = testInstance.authorizationCurrentUser(TEST_AUTORIZATION_TOKEN);

        assertTrue(actualResult);
    }

    @Test
    void authorizationCurrentUser_shouldReturnFalse_whenUserDataDidNotMatch() {
        when(authenticateTokenService.verify(TEST_AUTORIZATION_TOKEN)).thenReturn(false);

        boolean actualResult = testInstance.authorizationCurrentUser(TEST_AUTORIZATION_TOKEN);

        assertFalse(actualResult);
    }

    @Test
    void shouldChangePassword() {
        when(userService.getUserForUsername(TEST_USERNAME)).thenReturn(Optional.of(user));
        when(user.getPassword()).thenReturn(TEST_PASSWORD);

        testInstance.changePassword(TEST_USERNAME, TEST_PASSWORD, TEST_NEW_PASSWORD);

        verify(user).setPassword(TEST_NEW_PASSWORD);
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