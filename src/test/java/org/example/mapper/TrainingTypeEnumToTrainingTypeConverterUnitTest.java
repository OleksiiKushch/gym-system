package org.example.mapper;

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
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class TrainingTypeEnumToTrainingTypeConverterUnitTest {

    private static final String EXPECTED_EXCEPTION_MESSAGE = "Training type with name: BOXING does not exist";

    @InjectMocks
    TrainingTypeEnumToTrainingTypeConverter testInstance;

    @Mock
    TrainingTypeService trainingTypeService;
    @Mock
    TrainingType trainingType;

    @Test
    void shouldConvert() {
        doReturn(Optional.of(trainingType)).when(trainingTypeService).getTrainingTypeForName(TrainingTypeEnum.BOXING);

        TrainingType actualResult = testInstance.convert(TrainingTypeEnum.BOXING);

        assertEquals(trainingType, actualResult);
    }

    @Test
    void shouldThrowException_whenTrainingTypeNotFound() {
        doReturn(Optional.empty()).when(trainingTypeService).getTrainingTypeForName(TrainingTypeEnum.BOXING);

        Exception exception = assertThrows(AppException.class, () ->
                testInstance.convert(TrainingTypeEnum.BOXING));

        assertEquals(EXPECTED_EXCEPTION_MESSAGE, exception.getMessage());
    }

    @Test
    void shouldReturnNull_whenSourceIsNull() {
        TrainingType actualResult = testInstance.convert((TrainingTypeEnum) null);

        assertNull(actualResult);
    }

}