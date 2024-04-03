package org.example.controller;

import org.example.dto.TrainingTypeDto;
import org.example.facade.TrainingTypeFacade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class TrainingTypeControllerUnitTest {

    @InjectMocks
    TrainingTypeController testInstance;

    @Mock
    TrainingTypeFacade trainingTypeFacade;

    @Mock
    List<TrainingTypeDto> trainingTypeDtoList;

    @Test
    void shouldGetTrainingTypes() {
        doReturn(trainingTypeDtoList).when(trainingTypeFacade).getTrainingTypes();

        var actualResult = testInstance.getTrainingTypes();

        assertNotNull(actualResult);
        assertEquals(HttpStatus.OK, actualResult.getStatusCode());
        assertEquals(trainingTypeDtoList, actualResult.getBody());
    }

}