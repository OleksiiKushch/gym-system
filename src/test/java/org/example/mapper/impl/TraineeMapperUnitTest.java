package org.example.mapper.impl;

import org.example.dto.TraineeDto;
import org.example.entity.Trainee;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TraineeMapperUnitTest {

    private static final Integer TEST_ID = 1;
    private static final String TEST_FIRST_NAME = "John";
    private static final String TEST_LAST_NAME = "Doe";
    private static final String TEST_PASSWORD = "password";
    private static final String TEST_DATE_OF_BIRTHDAY = "1990-01-01";
    private static final String TEST_ADDRESS = "address";

    @Spy
    TraineeMapper testInstance;

    @Mock
    TraineeDto traineeDto;

    @Test
    void shouldMap() {
        prepareTraineeDto();

        Trainee actualResult = testInstance.map(traineeDto);

        assertEquals(TEST_ID, actualResult.getUserId());
        assertEquals(TEST_FIRST_NAME, actualResult.getFirstName());
        assertEquals(TEST_LAST_NAME, actualResult.getLastName());
        assertEquals(TEST_PASSWORD, actualResult.getPassword());
        assertEquals(TEST_DATE_OF_BIRTHDAY, actualResult.getDateOfBirthday().toString());
        assertEquals(TEST_ADDRESS, actualResult.getAddress());
    }

    @Test
    void shouldMap_whenDateOfBirthdayIsNull() {
        prepareTraineeDto();
        when(traineeDto.getDateOfBirthday()).thenReturn(null);

        Trainee actualResult = testInstance.map(traineeDto);

        assertEquals(TEST_ID, actualResult.getUserId());
        assertEquals(TEST_FIRST_NAME, actualResult.getFirstName());
        assertEquals(TEST_LAST_NAME, actualResult.getLastName());
        assertEquals(TEST_PASSWORD, actualResult.getPassword());
        assertNull(actualResult.getDateOfBirthday());
        assertEquals(TEST_ADDRESS, actualResult.getAddress());
    }

    @Test
    void shouldReturnJustNewTrainee_whenSourceIsNull() {
        Trainee actualResult = testInstance.map(null);

        assertNull(actualResult.getUserId());
        assertNull(actualResult.getFirstName());
        assertNull(actualResult.getLastName());
        assertNull(actualResult.getPassword());
        assertNull(actualResult.getDateOfBirthday());
        assertNull(actualResult.getAddress());
    }

    void prepareTraineeDto() {
        when(traineeDto.getId()).thenReturn(TEST_ID);
        when(traineeDto.getFirstName()).thenReturn(TEST_FIRST_NAME);
        when(traineeDto.getLastName()).thenReturn(TEST_LAST_NAME);
        when(traineeDto.getPassword()).thenReturn(TEST_PASSWORD);
        when(traineeDto.getDateOfBirthday()).thenReturn(TEST_DATE_OF_BIRTHDAY);
        when(traineeDto.getAddress()).thenReturn(TEST_ADDRESS);
    }
}