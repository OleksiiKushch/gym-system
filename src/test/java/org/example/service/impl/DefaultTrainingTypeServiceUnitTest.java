package org.example.service.impl;

import org.example.dao.TrainingTypeDao;
import org.example.entity.TrainingTypeEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DefaultTrainingTypeServiceUnitTest {

    @InjectMocks
    DefaultTrainingTypeService testInstance;

    @Mock
    TrainingTypeDao trainingTypeDao;

    @Test
    void shouldGetTrainingTypeForName() {
        testInstance.getTrainingTypeForName(TrainingTypeEnum.BOXING);

        verify(trainingTypeDao).findByName(TrainingTypeEnum.BOXING);
    }

    @Test
    void shouldGetAllTrainingTypes() {
        testInstance.getAllTrainingTypes();

        verify(trainingTypeDao).findAll();
    }
}