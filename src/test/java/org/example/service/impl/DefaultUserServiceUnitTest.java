package org.example.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.example.dao.UserDao;
import org.example.entity.Trainee;
import org.example.entity.Trainer;
import org.example.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultUserServiceUnitTest {

    private static final String NEW_USER_FIRST_NAME = "John";
    private static final String NEW_USER_LAST_NAME = "Doe";
    private static final String USER_1_USERNAME = "Alex.Bush";
    private static final String USER_2_USERNAME = "John.Doe";
    private static final String USER_3_USERNAME = "John.Doe1";
    private static final String PASSWORD = "password";
    private static final String SOME_PASSWORD = "some_password";
    private static final String EXPECTED_USERNAME_WITOUT_SERIAL_NUMBER = "John.Doe";
    private static final String EXPECTED_USERNAME_WITH_SERIAL_NUMBER_1 = "John.Doe1";
    private static final String EXPECTED_USERNAME_WITH_SERIAL_NUMBER_2 = "John.Doe2";
    private static final String RANDOM_PASSWORD_LENGTH_FIELD_NAME = "randomPasswordLength";
    private static final String PASSWORD_ALLOWED_CHARACTERS_FIELD_NAME = "passwordAllowedCharacters";
    private static final int EXPECTED_PASSWORD_LENGTH = 10;
    private static final String PASSWORD_ALLOWED_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%&*()";

    @InjectMocks
    DefaultUserService testInstance;

    @Mock
    UserDao userDao;

    @Mock
    User newUser;
    @Mock
    Trainee user1;
    @Mock
    Trainee user2;
    @Mock
    Trainer user3;

    @Test
    void shouldCalculateUsername_whenItsUniqComboOfFirstAndLastName() {
        prepareNewUser();
        when(user1.getUsername()).thenReturn(USER_1_USERNAME);
        when(userDao.findAll()).thenReturn(List.of(user1));

        String actualResult = testInstance.calculateUsername(newUser);

        assertEquals(EXPECTED_USERNAME_WITOUT_SERIAL_NUMBER, actualResult);
    }

    @Test
    void shouldCalculateUsername_whenItsNotUniqComboOfFirstAndLastName() {
        prepareNewUser();
        when(user1.getUsername()).thenReturn(USER_1_USERNAME);
        when(user2.getUsername()).thenReturn(USER_2_USERNAME);
        when(userDao.findAll()).thenReturn(List.of(user1, user2));

        String actualResult = testInstance.calculateUsername(newUser);

        assertEquals(EXPECTED_USERNAME_WITH_SERIAL_NUMBER_1, actualResult);
    }

    @Test
    void shouldCalculateUsername_whenItsNotUniqComboOfFirstAndLastNameForDiffTypeOfUsers() {
        prepareNewUser();
        when(user1.getUsername()).thenReturn(USER_1_USERNAME);
        when(user2.getUsername()).thenReturn(USER_2_USERNAME);
        when(user3.getUsername()).thenReturn(USER_3_USERNAME);
        when(userDao.findAll()).thenReturn(List.of(user1, user2, user3));

        String actualResult = testInstance.calculateUsername(newUser);

        assertEquals(EXPECTED_USERNAME_WITH_SERIAL_NUMBER_2, actualResult);
    }

    /**
     * This is relevant when some users have been deleted and the serial numbers are not sequential.
     * For example: John.Doe, John.Doe1, John.Doe3
     * Expected result: John.Doe4
     * Note: An algorithm that simply counts the number of such usernames and adds this count as a serial number, should be a failure.
     */
    @Test
    void shouldCalculateUsername_whenItsNotUniqComboOfFirstAndLastNameAndSerialNumbersArentConsecutive() {
        prepareNewUser();
        when(user1.getUsername()).thenReturn(USER_1_USERNAME);
        when(user3.getUsername()).thenReturn(USER_3_USERNAME);
        when(userDao.findAll()).thenReturn(List.of(user1, user3));

        String actualResult = testInstance.calculateUsername(newUser);

        assertEquals(EXPECTED_USERNAME_WITH_SERIAL_NUMBER_2, actualResult);
    }

    @Test
    void generatedPasswordShouldNotBeBlank() {
        ReflectionTestUtils.setField(testInstance, RANDOM_PASSWORD_LENGTH_FIELD_NAME, EXPECTED_PASSWORD_LENGTH);
        ReflectionTestUtils.setField(testInstance, PASSWORD_ALLOWED_CHARACTERS_FIELD_NAME, PASSWORD_ALLOWED_CHARACTERS);

        String actualResult = testInstance.generateRandomPassword();

        assertTrue(StringUtils.isNotBlank(actualResult));
    }

    @Test
    void generatedPasswordShouldBeExpectedCharactersLong() {
        ReflectionTestUtils.setField(testInstance, RANDOM_PASSWORD_LENGTH_FIELD_NAME, EXPECTED_PASSWORD_LENGTH);
        ReflectionTestUtils.setField(testInstance, PASSWORD_ALLOWED_CHARACTERS_FIELD_NAME, PASSWORD_ALLOWED_CHARACTERS);

        String actualResult = testInstance.generateRandomPassword();

        assertEquals(EXPECTED_PASSWORD_LENGTH, actualResult.length());
    }

    @Test
    void shouldGetUserByUsername() {
        when(userDao.findByUsername(USER_1_USERNAME)).thenReturn(java.util.Optional.of(user1));

        var actualResult = testInstance.getUserForUsername(USER_1_USERNAME);

        assertTrue(actualResult.isPresent());
        assertEquals(user1, actualResult.get());
    }

    @Test
    void shouldAuthenticateUser_whenPasswordsMatch() {
        when(user1.getPassword()).thenReturn(PASSWORD);
        when(userDao.findByUsername(USER_1_USERNAME)).thenReturn(Optional.of(user1));

        var actualResult = testInstance.authenticateUser(USER_1_USERNAME, PASSWORD);

        assertTrue(actualResult.isPresent());
        assertEquals(user1, actualResult.get());
    }

    @Test
    void shouldDoesntAuthenticateUser_whenPasswordsDontMatch() {
        when(user1.getPassword()).thenReturn(PASSWORD);
        when(userDao.findByUsername(USER_1_USERNAME)).thenReturn(Optional.of(user1));

        var actualResult = testInstance.authenticateUser(USER_1_USERNAME, SOME_PASSWORD);

        assertTrue(actualResult.isEmpty());
    }

    @Test
    void shouldDoesntAuthenticateUser_whenUserNotFound() {
        when(userDao.findByUsername(USER_1_USERNAME)).thenReturn(Optional.empty());

        var actualResult = testInstance.authenticateUser(USER_1_USERNAME, PASSWORD);

        assertTrue(actualResult.isEmpty());
    }

    @Test
    void shouldUpdateUser() {
        testInstance.updateUser(user1);

        verify(userDao).save(user1);
    }

    void prepareNewUser() {
        when(newUser.getFirstName()).thenReturn(NEW_USER_FIRST_NAME);
        when(newUser.getLastName()).thenReturn(NEW_USER_LAST_NAME);
    }
}