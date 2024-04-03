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

    private static final int ID = 1;

    @InjectMocks
    DefaultTraineeService testInstance;

    @Mock
    TraineeDao traineeDao;

    @Mock
    Trainee trainee;

    @Test
    void shouldCreateTrainee() {
        testInstance.createTrainee(trainee);

        verify(traineeDao).insert(trainee);
    }

    @Test
    void shouldUpdateTrainee() {
        testInstance.updateTrainee(trainee);

        verify(traineeDao).update(trainee);
    }

    @Test
    void shouldDeleteTrainee_whenTraineeIsNotActive() {
        when(trainee.getUserId()).thenReturn(ID);

        testInstance.deleteTrainee(trainee);

        verify(traineeDao).remove(ID);
    }

    @Test
    void shouldGetTraineeForId() {
        when(traineeDao.findById(ID)).thenReturn(Optional.of(trainee));

        Optional<Trainee> actualResult = testInstance.getTraineeForId(ID);

        assertTrue(actualResult.isPresent());
        assertEquals(trainee, actualResult.get());
    }
}