package org.example.mapper;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class StringToLocalDateConverterUnitTest {

    private static final String TEST_DATE = "2021-01-01";

    @InjectMocks
    StringToLocalDateConverter testInstance;

    @Mock
    DateTimeFormatter defaultDateFormatter;

    @Test
    void shouldConvert() {
        LocalDate expectedResult = LocalDate.parse(TEST_DATE, defaultDateFormatter);

        LocalDate actualResult = testInstance.convert(TEST_DATE);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    void shouldReturnNull_whenSourceIsNull() {
        LocalDate actualResult = testInstance.convert((String) null);

        assertNull(actualResult);
    }

    @Test
    void shouldReturnNull_whenSourceIsEmpty() {
        LocalDate actualResult = testInstance.convert(StringUtils.EMPTY);

        assertNull(actualResult);
    }
}