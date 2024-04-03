package org.example.mapper.impl;

import org.example.dto.TrainerDto;
import org.example.entity.Trainer;
import org.example.entity.TrainingType;
import org.example.entity.TrainingTypeEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainerMapperUnitTest {

    private static final Integer TEST_ID = 1;
    private static final String TEST_FIRST_NAME = "John";
    private static final String TEST_LAST_NAME = "Doe";
    private static final String TEST_PASSWORD = "password";
    private static final String TEST_SPECIALIZATION_STR = TrainingTypeEnum.CARDIO.name();

    @InjectMocks
    TrainerMapper testInstance;

    @Mock
    TrainingTypeMapper trainingTypeMapper;

    @Mock
    TrainerDto trainerDto;
    @Mock
    TrainingType trainingType;

    @Test
    void shouldMap() {
        prepareTrainerDto();
        when(trainingTypeMapper.map(TEST_SPECIALIZATION_STR)).thenReturn(trainingType);

        Trainer actualResult = testInstance.map(trainerDto);

        assertEquals(TEST_ID, actualResult.getUserId());
        assertEquals(TEST_FIRST_NAME, actualResult.getFirstName());
        assertEquals(TEST_LAST_NAME, actualResult.getLastName());
        assertEquals(TEST_PASSWORD, actualResult.getPassword());
        assertEquals(trainingType, actualResult.getSpecialization());
    }

    @Test
    void shouldReturnJustNewTrainer_whenSourceIsNull() {
        Trainer actualResult = testInstance.map(null);

        assertNull(actualResult.getUserId());
        assertNull(actualResult.getFirstName());
        assertNull(actualResult.getLastName());
        assertNull(actualResult.getPassword());
        assertNull(actualResult.getSpecialization());
    }

    void prepareTrainerDto() {
        when(trainerDto.getId()).thenReturn(TEST_ID);
        when(trainerDto.getFirstName()).thenReturn(TEST_FIRST_NAME);
        when(trainerDto.getLastName()).thenReturn(TEST_LAST_NAME);
        when(trainerDto.getPassword()).thenReturn(TEST_PASSWORD);
        when(trainerDto.getSpecialization()).thenReturn(TEST_SPECIALIZATION_STR);
    }
}