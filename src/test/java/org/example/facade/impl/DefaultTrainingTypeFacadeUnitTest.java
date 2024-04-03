package org.example.facade.impl;

import org.example.dto.TrainingTypeDto;
import org.example.entity.TrainingType;
import org.example.service.TrainingTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultTrainingTypeFacadeUnitTest {

    @InjectMocks
    DefaultTrainingTypeFacade testInstance;

    @Mock
    TrainingTypeService trainingTypeService;
    @Mock
    ModelMapper modelMapper;

    @Mock
    TrainingType trainingType1;
    @Mock
    TrainingTypeDto trainingTypeDto1;
    @Mock
    TrainingType trainingType2;
    @Mock
    TrainingTypeDto trainingTypeDto2;

    List<TrainingType> trainingTypes;

    @BeforeEach
    void setUp() {
        trainingTypes = List.of(trainingType1, trainingType2);
    }

    @Test
    void shouldGetListOfTrainingTypes() {
        when(trainingTypeService.getAllTrainingTypes()).thenReturn(trainingTypes);
        when(modelMapper.map(trainingType1, TrainingTypeDto.class)).thenReturn(trainingTypeDto1);
        when(modelMapper.map(trainingType2, TrainingTypeDto.class)).thenReturn(trainingTypeDto2);

        List<TrainingTypeDto> actualResult = testInstance.getTrainingTypes();

        assertEquals(trainingTypes.size(), actualResult.size());
        assertTrue(actualResult.contains(trainingTypeDto1));
        assertTrue(actualResult.contains(trainingTypeDto2));
    }
}