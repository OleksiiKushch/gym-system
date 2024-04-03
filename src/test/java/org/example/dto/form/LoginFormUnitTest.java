package org.example.dto.form;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class LoginFormUnitTest {

    private static final String TEST_USERNAME = "John.Doe";
    private static final String TEST_PASSWORD = "123456";

    private static final String EXPECTED_RESULT = "LoginForm{username=John.Doe, password=****}";

    @Spy
    LoginForm testInstance = new LoginForm(TEST_USERNAME, TEST_PASSWORD);

    @Test
    void shouldNotDisplayPasswords_whenToString() {
        String actualResult = testInstance.toString();

        assertEquals(EXPECTED_RESULT, actualResult);
    }
}