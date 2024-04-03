package org.example.dto.form;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ChangePasswordFormUnitTest {

    private static final String CURRENT_PASSWORD = "123456";
    private static final String NEW_PASSWORD = "654321";
    private static final String CONFIRM_PASSWORD = "654321";

    private static final String EXPECTED_RESULT = "ChangePasswordForm{currentPassword=****, newPassword=****, confirmPassword=****}";

    @Spy
    ChangePasswordForm testInstance = new ChangePasswordForm(CURRENT_PASSWORD, NEW_PASSWORD, CONFIRM_PASSWORD);

    @Test
    void shouldNotDisplayPasswords_whenToString() {
        String actualResult = testInstance.toString();

        assertEquals(EXPECTED_RESULT, actualResult);
    }
}