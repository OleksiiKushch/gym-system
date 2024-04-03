package org.example.mapper.impl;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class StringToLocalDateMapperUnitTest {

    private static final String TEST_DATE = "2021-12-12";

    @Spy
    StringToLocalDateMapper testInstance;

    @Test
    void shouldMap() {
        var actualResult = testInstance.map(TEST_DATE);

        assertNotNull(actualResult);
    }

    @Test
    void shouldReturnNull_whenSourceIsEmptyLine() {
        var actualResult = testInstance.map(StringUtils.EMPTY);

        assertNull(actualResult);
    }

    @Test
    void shouldReturnNull_whenSourceIsNull() {
        var actualResult = testInstance.map(null);

        assertNull(actualResult);
    }
}