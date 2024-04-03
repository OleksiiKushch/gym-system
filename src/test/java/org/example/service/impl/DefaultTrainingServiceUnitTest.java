package org.example.service.impl;

import org.example.dao.TrainingDao;
import org.example.entity.Training;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultTrainingServiceUnitTest {

    private static final String TRAINING_NAME = "Test Training";

    @InjectMocks
    DefaultTrainingService testInstance;

    @Mock
    TrainingDao trainingDao;

    @Mock
    Training training;

    @Test
    void shouldCreateTraining() {
        testInstance.createTraining(training);

        verify(trainingDao).insert(training);
    }
    
    @Test
    void shouldGetTrainingForName() {
        when(trainingDao.findByName(TRAINING_NAME)).thenReturn(Optional.of(training));

        Optional<Training> actualResult = testInstance.getTrainingForName(TRAINING_NAME);

        assertTrue(actualResult.isPresent());
        assertEquals(training, actualResult.get());
    }
}