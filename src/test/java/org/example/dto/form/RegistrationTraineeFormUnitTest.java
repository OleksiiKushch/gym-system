package org.example.dto.form;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class RegistrationTraineeFormUnitTest {

    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Doe";
    private static final String PASSWORD = "123456";
    private static final String CONFIRM_PASSWORD = "123456";
    private static final String DATE_OF_BIRTHDAY = "1990-01-01";
    private static final String ADDRESS = "123 Main St";

    private static final String EXPECTED_RESULT =
            "RegistrationTraineeForm{firstName=John, lastName=Doe, password=****, confirmPassword=****, dateOfBirthday=1990-01-01, address=123 Main St}";

    @Spy
    RegistrationTraineeForm testInstance = new RegistrationTraineeForm(
            FIRST_NAME, LAST_NAME, PASSWORD, CONFIRM_PASSWORD, DATE_OF_BIRTHDAY, ADDRESS);

    @Test
    void shouldNotDisplayPasswords_whenToString() {
        String actualResult = testInstance.toString();

        assertEquals(EXPECTED_RESULT, actualResult);
    }
}