package org.example.mapper.impl;

import org.example.dto.TrainingDto;
import org.example.entity.Training;
import org.example.entity.TrainingType;
import org.example.entity.TrainingTypeEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainingMapperUnitTest {

    private static final Integer TEST_ID = 1;
    private static final String TEST_TRAINING_NAME = "Extra cardio training";
    private static final String TEST_TRAINING_TYPE = TrainingTypeEnum.CARDIO.name();
    private static final Integer TEST_TRAINING_DURATION = 45;
    private static final String TEST_TRAINING_DATE = "2021-12-12";

    @InjectMocks
    TrainingMapper testInstance;

    @Mock
    TrainingTypeMapper trainingTypeMapper;
    @Mock
    StringToLocalDateMapper stringToLocalDateMapper;

    @Mock
    TrainingDto trainingDto;

    @Mock
    TrainingType trainingType;
    @Mock
    LocalDate trainingDate;

    @Test
    void shouldMap() {
        prepareTrainingDto();
        when(trainingTypeMapper.map(TEST_TRAINING_TYPE)).thenReturn(trainingType);
        when(stringToLocalDateMapper.map(TEST_TRAINING_DATE)).thenReturn(trainingDate);

        Training actualResult = testInstance.map(trainingDto);

        assertEquals(TEST_ID, actualResult.getId());
        assertEquals(TEST_TRAINING_NAME, actualResult.getTrainingName());
        assertEquals(TEST_TRAINING_DURATION, actualResult.getTrainingDuration());
        assertEquals(trainingType, actualResult.getTrainingType());
        assertEquals(trainingDate, actualResult.getTrainingDate());
    }

    @Test
    void shouldReturnJustNewTraining_whenSourceIsNull() {
        Training actualResult = testInstance.map(null);

        assertNull(actualResult.getId());
        assertNull(actualResult.getTrainingName());
        assertNull(actualResult.getTrainingDuration());
        assertNull(actualResult.getTrainingType());
        assertNull(actualResult.getTrainingDate());
    }

    void prepareTrainingDto() {
        when(trainingDto.getId()).thenReturn(TEST_ID);
        when(trainingDto.getTrainingName()).thenReturn(TEST_TRAINING_NAME);
        when(trainingDto.getTrainingType()).thenReturn(TEST_TRAINING_TYPE);
        when(trainingDto.getDuration()).thenReturn(TEST_TRAINING_DURATION);
        when(trainingDto.getTrainingDate()).thenReturn(TEST_TRAINING_DATE);
    }
}