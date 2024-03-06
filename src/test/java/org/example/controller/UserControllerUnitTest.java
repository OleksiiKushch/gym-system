package org.example.controller;

import org.example.dto.form.ChangePasswordForm;
import org.example.dto.form.LoginForm;
import org.example.facade.UserFacade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class UserControllerUnitTest {

    private static final String TEST_USERNAME = "John.Doe";
    private static final String TEST_PASSWORD = "password";
    private static final String TEST_NEW_PASSWORD = "new_password";
    private static final String TEST_AUTHORIZATION_TOKEN = "authorization_token";

    @InjectMocks
    UserController testInstance;

    @Mock
    UserFacade userFacade;
    @Mock
    ModelMapper modelMapper;

    @Mock
    LoginForm loginForm;
    @Mock
    ChangePasswordForm changePasswordForm;
    @Mock
    BindingResult result;

    @Mock
    List<FieldError> errors;

    @Test
    void shouldLoginUser_whenNoErrors() {
        doReturn(false).when(result).hasErrors();
        doReturn(TEST_USERNAME).when(loginForm).getUsername();
        doReturn(TEST_PASSWORD).when(loginForm).getPassword();
        doReturn(TEST_AUTHORIZATION_TOKEN).when(userFacade).login(TEST_USERNAME, TEST_PASSWORD);

        var actualResult = testInstance.loginUser(loginForm, result);

        assertNotNull(actualResult);
        assertEquals(HttpStatus.OK, actualResult.getStatusCode());
        assertEquals(TEST_AUTHORIZATION_TOKEN, actualResult.getBody());
    }

    @Test
    void shouldDoesntLoginUser_whenSomeErrors() {
        doReturn(true).when(result).hasErrors();
        doReturn(errors).when(result).getFieldErrors();

        var actualResult = testInstance.loginUser(loginForm, result);

        assertNotNull(actualResult);
        assertEquals(HttpStatus.BAD_REQUEST, actualResult.getStatusCode());
        assertEquals(errors, actualResult.getBody());
    }

    @Test
    void shouldLogoutUser() {
        doNothing().when(userFacade).logout(TEST_AUTHORIZATION_TOKEN);

        var actualResult = testInstance.logoutUser(TEST_AUTHORIZATION_TOKEN);

        assertNotNull(actualResult);
        assertEquals(HttpStatus.NO_CONTENT, actualResult.getStatusCode());
    }

    @Test
    void shouldChangePassword_whenNoErrors() {
        doReturn(false).when(result).hasErrors();
        doReturn(TEST_PASSWORD).when(changePasswordForm).getCurrentPassword();
        doReturn(TEST_NEW_PASSWORD).when(changePasswordForm).getNewPassword();
        doNothing().when(userFacade).changePassword(TEST_USERNAME, TEST_PASSWORD, TEST_NEW_PASSWORD);

        var actualResult = testInstance.changePassword(TEST_AUTHORIZATION_TOKEN, TEST_USERNAME, changePasswordForm, result);

        assertNotNull(actualResult);
        assertEquals(HttpStatus.NO_CONTENT, actualResult.getStatusCode());
    }

    @Test
    void shouldDoesntChangePassword_whenSomeErrors() {
        doReturn(true).when(result).hasErrors();
        doReturn(errors).when(result).getFieldErrors();

        var actualResult = testInstance.changePassword(TEST_AUTHORIZATION_TOKEN, TEST_USERNAME, changePasswordForm, result);

        assertNotNull(actualResult);
        assertEquals(HttpStatus.BAD_REQUEST, actualResult.getStatusCode());
        assertEquals(errors, actualResult.getBody());
    }

    @Test
    void shouldToggleUserActivation() {
        doNothing().when(userFacade).toggleUserActivation(TEST_USERNAME);

        var actualResult = testInstance.toggleUserActivation(TEST_AUTHORIZATION_TOKEN, TEST_USERNAME);

        assertNotNull(actualResult);
        assertEquals(HttpStatus.NO_CONTENT, actualResult.getStatusCode());
    }

}