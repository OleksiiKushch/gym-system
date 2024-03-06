package org.example.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StringLocalDateValidatorUnitTest {

    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String VALID_DATE = "2022-12-12";
    private static final String INVALID_FORMAT_DATE = "2022/12/12";
    private static final String INVALID_DATE_LEAP_YEAR = "2022-02-29";
    private static final String INVALID_DATE = "2022-02-31";

    private static final String INVALID_FORMAT_EXPECTED_ERROR_MSG =
            "The value '2022/12/12' does not match the expected date format. Please ensure the date follows the format 'yyyy-MM-dd'.";
    private static final String INVALID_DATE_LEAP_YEAR_EXPECTED_ERROR_MSG =
            "Text '2022-02-29' could not be parsed: Invalid date 'February 29' as '2022' is not a leap year";
    private static final String INVALID_DATE_EXPECTED_ERROR_MSG =
            "Text '2022-02-31' could not be parsed: Invalid date 'FEBRUARY 31'";

    @InjectMocks
    StringLocalDateValidator testInstance;

    @Mock
    StringLocalDate stringLocalDate;

    @Mock
    DateTimeFormatter dateFormatter;
    @Mock
    ConstraintValidatorContext context;
    @Mock
    TemporalAccessor temporalAccessor;
    @Mock
    ConstraintValidatorContext.ConstraintViolationBuilder builder;

    @BeforeEach
    void setUp() {
        doReturn(DATE_FORMAT).when(stringLocalDate).dateFormat();
        testInstance.initialize(stringLocalDate);
    }

    @Test
    void initialize_shouldUseDateFormatFromAnnotation_whenPresent() {
        testInstance.initialize(stringLocalDate);
    }

    @Test
    void initialize_shouldUseDefaultDateFormat_whenNotPresentInAnnotation() {
        doReturn(null).when(stringLocalDate).dateFormat();

        testInstance.initialize(stringLocalDate);
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
    void shouldReturnTrue_whenValueIsValidDate() {
        boolean actualResult = testInstance.isValid(VALID_DATE, context);

        assertTrue(actualResult);
    }

    @Test
    void shouldReturnFalse_whenValueIsInvalidFormatDate() {
        doReturn(builder).when(context).buildConstraintViolationWithTemplate(INVALID_FORMAT_EXPECTED_ERROR_MSG);

        boolean actualResult = testInstance.isValid(INVALID_FORMAT_DATE, context);

        verify(context).disableDefaultConstraintViolation();
        verify(builder).addConstraintViolation();
        assertFalse(actualResult);
    }

    @Test
    void shouldReturnFalse_whenValueIsInvalidDate1() {
        doReturn(builder).when(context).buildConstraintViolationWithTemplate(INVALID_DATE_LEAP_YEAR_EXPECTED_ERROR_MSG);

        boolean actualResult = testInstance.isValid(INVALID_DATE_LEAP_YEAR, context);

        verify(context).disableDefaultConstraintViolation();
        verify(builder).addConstraintViolation();
        assertFalse(actualResult);
    }

    @Test
    void shouldReturnFalse_whenValueIsInvalidDate2() {
        doReturn(builder).when(context).buildConstraintViolationWithTemplate(INVALID_DATE_EXPECTED_ERROR_MSG);

        boolean actualResult = testInstance.isValid(INVALID_DATE, context);

        verify(context).disableDefaultConstraintViolation();
        verify(builder).addConstraintViolation();
        assertFalse(actualResult);
    }

}