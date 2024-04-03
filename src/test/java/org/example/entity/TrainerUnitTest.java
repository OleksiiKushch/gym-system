package org.example.entity;

import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainerUnitTest {

    private static final String TEST_ID = "1";
    private static final String TEST_FIRST_NAME = "John";
    private static final String TEST_LAST_NAME = "Doe";
    private static final String TEST_USERNAME = "John.Doe";
    private static final String TEST_PASSWORD = "password";
    private static final String TEST_IS_ACTIVE = "true";
    private static final String TEST_SPECIALIZATION = "specialization";

    @Spy
    Trainer testInstance;

    @Mock
    CSVRecord csvRecord;

    @Test
    void shouldInitByCsvRecord() {
        when(csvRecord.get(0)).thenReturn(TEST_ID);
        when(csvRecord.get(1)).thenReturn(TEST_FIRST_NAME);
        when(csvRecord.get(2)).thenReturn(TEST_LAST_NAME);
        when(csvRecord.get(3)).thenReturn(TEST_USERNAME);
        when(csvRecord.get(4)).thenReturn(TEST_PASSWORD);
        when(csvRecord.get(5)).thenReturn(TEST_IS_ACTIVE);
        when(csvRecord.get(6)).thenReturn(TEST_SPECIALIZATION);

        testInstance.initByCsvRecord(csvRecord);

        assertEquals(TEST_ID, testInstance.getUserId().toString());
        assertEquals(TEST_FIRST_NAME, testInstance.getFirstName());
        assertEquals(TEST_LAST_NAME, testInstance.getLastName());
        assertEquals(TEST_USERNAME, testInstance.getUsername());
        assertEquals(TEST_PASSWORD, testInstance.getPassword());
        assertEquals(TEST_IS_ACTIVE, String.valueOf(testInstance.isActive()));
        assertEquals(TEST_SPECIALIZATION, testInstance.getSpecialization());
    }
}