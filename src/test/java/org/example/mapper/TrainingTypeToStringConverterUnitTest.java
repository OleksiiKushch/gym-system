package org.example.mapper;

import org.example.entity.TrainingType;
import org.example.entity.TrainingTypeEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class TrainingTypeToStringConverterUnitTest {

    @InjectMocks
    TrainingTypeToStringConverter testInstance;

    @Mock
    TrainingType trainingType;

    @Test
    void shouldConvert() {
        doReturn(TrainingTypeEnum.BOXING).when(trainingType).getName();

        String actualResult = testInstance.convert(trainingType);

        assertEquals(TrainingTypeEnum.BOXING.name(), actualResult);
    }

    @Test
    void shouldReturnNull_whenSourceIsNull() {
        String actualResult = testInstance.convert((TrainingType) null);

        assertNull(actualResult);
    }

}