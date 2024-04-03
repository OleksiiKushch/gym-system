package org.example.dto.form;

import org.example.entity.TrainingTypeEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class RegistrationTrainerFormUnitTest {

    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Doe";
    private static final String PASSWORD = "123456";
    private static final String CONFIRM_PASSWORD = "123456";
    private static final String SPECIALIZATION = TrainingTypeEnum.YOGA.name();

    private static final String EXPECTED_RESULT =
            "RegistrationTrainerForm{firstName=John, lastName=Doe, password=****, confirmPassword=****, specialization=YOGA}";

    @Spy
    RegistrationTrainerForm testInstance = new RegistrationTrainerForm(
            FIRST_NAME, LAST_NAME, PASSWORD, CONFIRM_PASSWORD, SPECIALIZATION);

    @Test
    void shouldNotDisplayPasswords_whenToString() {
        String actualResult = testInstance.toString();

        assertEquals(EXPECTED_RESULT, actualResult);
    }
}