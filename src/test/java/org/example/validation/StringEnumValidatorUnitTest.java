package org.example.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.example.entity.TrainingTypeEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StringEnumValidatorUnitTest {

    private static final String VALID_VALUE = "BOXING";
    private static final String INVALID_VALUE = "TROLLING";

    private static final String EXPECTED_ERROR_MSG =
            "The value 'TROLLING' is not a valid null. Valid null values are: RUNNING, BOXING.";

    @InjectMocks
    StringEnumValidator testInstance;

    @Spy
    Set<String> acceptedValues = new LinkedHashSet<>(List.of("RUNNING", "BOXING"));

    @Mock
    StringEnum stringEnum;
    @Mock
    ConstraintValidatorContext context;
    @Mock
    ConstraintValidatorContext.ConstraintViolationBuilder builder;

    Class<TrainingTypeEnum> enumClass = TrainingTypeEnum.class;

    @Test
    void shouldInitializeValidator() {
        doReturn(enumClass).when(stringEnum).enumClass();

        testInstance.initialize(stringEnum);

        verify(stringEnum).enumName();
    }

    @Test
    void shouldReturnTrue_whenValueIsEmpty() {
        boolean actualResult = testInstance.isValid(StringUtils.EMPTY, context);

        assertTrue(actualResult);
    }

    @Test
    void shouldReturnTrue_whenValueIsNull() {
        boolean actualResult = testInstance.isValid(null, context);

        assertTrue(actualResult);
    }

    @Test
    void shouldReturnTrue_whenValueAmongAcceptedValues () {
        doReturn(true).when(acceptedValues).contains(VALID_VALUE);

        boolean actualResult = testInstance.isValid(VALID_VALUE, context);

        assertTrue(actualResult);
    }

    @Test
    void shouldReturnFalse_whenValueNotAmongAcceptedValues () {
        doReturn(false).when(acceptedValues).contains(INVALID_VALUE);
        doReturn(builder).when(context).buildConstraintViolationWithTemplate(EXPECTED_ERROR_MSG);

        boolean actualResult = testInstance.isValid(INVALID_VALUE, context);

        verify(context).disableDefaultConstraintViolation();
        verify(builder).addConstraintViolation();
        assertFalse(actualResult);
    }

}