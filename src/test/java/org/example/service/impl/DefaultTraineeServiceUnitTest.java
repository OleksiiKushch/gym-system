package org.example.service.impl;

import org.example.dao.TraineeDao;
import org.example.entity.Trainee;
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
class DefaultTraineeServiceUnitTest {

    private static final String USERNAME = "John.Doe";

    @InjectMocks
    DefaultTraineeService testInstance;

    @Mock
    TraineeDao traineeDao;

    @Mock
    Trainee trainee;

    @Test
    void shouldCreateTrainee() {
        testInstance.createTrainee(trainee);

        verify(traineeDao).save(trainee);
    }

    @Test
    void shouldUpdateTrainee() {
        testInstance.updateTrainee(trainee);

        verify(traineeDao).save(trainee);
    }

    @Test
    void shouldDeleteTrainee_whenTraineeIsNotActive() {
        testInstance.deleteTrainee(trainee);

        verify(traineeDao).delete(trainee);
    }

    @Test
    void shouldGetTraineeForUsername() {
        when(traineeDao.findByUsername(USERNAME)).thenReturn(Optional.of(trainee));

        Optional<Trainee> actualResult = testInstance.getTraineeForUsername(USERNAME);

        assertTrue(actualResult.isPresent());
        assertEquals(trainee, actualResult.get());
    }
}