package org.example.service.impl;

import org.example.dao.TraineeDao;
import org.example.entity.Trainee;
import org.example.entity.Trainer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultTraineeServiceUnitTest {

    private static final int ID = 1;
    private static final String USERNAME = "John.Doe";

    @InjectMocks
    DefaultTraineeService testInstance;

    @Mock
    TraineeDao traineeDao;

    @Mock
    Trainee trainee;
    @Mock
    List<Trainer> trainerList;

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

    @Test
    void shouldGetTraineeForUsername() {
        when(traineeDao.findByUsername(USERNAME)).thenReturn(Optional.of(trainee));

        Optional<Trainee> actualResult = testInstance.getTraineeForUsername(USERNAME);

        assertTrue(actualResult.isPresent());
        assertEquals(trainee, actualResult.get());
    }

    @Test
    void shouldGetFullTraineeForUsername() {
        when(traineeDao.findWithTrainingsByUsername(USERNAME)).thenReturn(Optional.of(trainee));

        Optional<Trainee> actualResult = testInstance.getFullTraineeForUsername(USERNAME);

        assertTrue(actualResult.isPresent());
        assertEquals(trainee, actualResult.get());
    }

    @Test
    void shouldUpdateTrainersList() {
        testInstance.updateTrainersList(USERNAME, trainerList);

        verify(traineeDao).updateTrainersList(USERNAME, trainerList);
    }

    @Test
    void shouldDeleteTraineeForUsername() {
        testInstance.deleteTraineeForUsername(USERNAME);

        verify(traineeDao).removeByUsername(USERNAME);
    }
}