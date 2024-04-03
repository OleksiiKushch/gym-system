package org.example.mapper.impl;

import org.apache.commons.lang3.StringUtils;
import org.example.entity.TrainingType;
import org.example.entity.TrainingTypeEnum;
import org.example.exception.AppException;
import org.example.service.TrainingTypeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainingTypeMapperUnitTest {

    private static final String TRAINING_TYPE_STR = "CARDIO";

    private static final String EXPECTED_EXCEPTION_MESSAGE = "Training type with name: CARDIO does not exist";

    @InjectMocks
    TrainingTypeMapper testInstance;

    @Mock
    TrainingTypeService trainingTypeService;

    @Mock
    TrainingType trainingType;

    @Test
    void shouldMap() {
        when(trainingTypeService.getTrainingTypeForName(TrainingTypeEnum.CARDIO)).thenReturn(Optional.of(trainingType));

        TrainingType actualResult = testInstance.map(TRAINING_TYPE_STR);

        assertEquals(trainingType, actualResult);
    }

    @Test
    void shouldReturnNull_whenSourceIsNull() {
        TrainingType actualResult = testInstance.map(null);

        assertNull(actualResult);
    }

    @Test
    void shouldReturnNull_whenSourceIsEmpty() {
        TrainingType actualResult = testInstance.map(StringUtils.EMPTY);

        assertNull(actualResult);
    }

    @Test
    void shouldThrowException_whenSTrainingTypeNotFound() {
        when(trainingTypeService.getTrainingTypeForName(TrainingTypeEnum.CARDIO)).thenReturn(Optional.empty());

        Exception exception = assertThrows(AppException.class, () -> testInstance.map(TRAINING_TYPE_STR));

        assertEquals(EXPECTED_EXCEPTION_MESSAGE, exception.getMessage());
    }
}