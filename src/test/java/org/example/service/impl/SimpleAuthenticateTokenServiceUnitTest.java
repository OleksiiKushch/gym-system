package org.example.service.impl;

import org.example.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SimpleAuthenticateTokenServiceUnitTest {

    private static final String TEST_USERNAME = "John.Doe";
    private static final String DELIMETER = "_";
    private static final String SOME_TEST_TOKEN = "John.Doe_1234-5678-90ab-cdef";

    @InjectMocks
    SimpleAuthenticateTokenService testInstance;

    @Mock
    Set<String> tokenStore;

    @Mock
    User user;

    @Test
    void shouldGenerateToken() {
        doReturn(TEST_USERNAME).when(user).getUsername();

        String actualResult = testInstance.generate(user);

        verify(tokenStore).add(actualResult);
        assertTrue(actualResult.startsWith(TEST_USERNAME + DELIMETER));
    }

    @Test
    void shouldVerifyToken() {
        doReturn(true).when(tokenStore).contains(SOME_TEST_TOKEN);

        boolean actualResult = testInstance.verify(SOME_TEST_TOKEN);

        assertTrue(actualResult);
    }

    @Test
    void shouldDeactivateToken() {
        testInstance.deactivate(SOME_TEST_TOKEN);

        verify(tokenStore).remove(SOME_TEST_TOKEN);
    }
}