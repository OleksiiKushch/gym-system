package org.example.service.impl;

import org.example.dao.TrainerDao;
import org.example.entity.Trainer;
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
class DefaultTrainerServiceUnitTest {

    private static final int ID = 1;
    private static final String USERNAME = "John.Doe";

    @InjectMocks
    DefaultTrainerService testInstance;
    
    @Mock
    TrainerDao trainerDao;
    
    @Mock
    Trainer trainer;

    @Test
    void shouldCreateTrainer() {
        testInstance.createTrainer(trainer);

        verify(trainerDao).insert(trainer);
    }

    @Test
    void shouldUpdateTrainer() {
        testInstance.updateTrainer(trainer);

        verify(trainerDao).update(trainer);
    }

    @Test
    void shouldGetTrainerForId() {
        when(trainerDao.findById(ID)).thenReturn(Optional.of(trainer));

        Optional<Trainer> actualResult = testInstance.getTrainerForId(ID);

        assertTrue(actualResult.isPresent());
        assertEquals(trainer, actualResult.get());
    }

    @Test
    void shouldGetTrainerForUsername() {
        when(trainerDao.findByUsername(USERNAME)).thenReturn(Optional.of(trainer));

        Optional<Trainer> actualResult = testInstance.getTrainerForUsername(USERNAME);

        assertTrue(actualResult.isPresent());
        assertEquals(trainer, actualResult.get());
    }

    @Test
    void shouldGetFullTrainerForUsername() {
        when(trainerDao.findWithTrainingsByUsername(USERNAME)).thenReturn(Optional.of(trainer));

        Optional<Trainer> actualResult = testInstance.getFullTrainerForUsername(USERNAME);

        assertTrue(actualResult.isPresent());
        assertEquals(trainer, actualResult.get());
    }

    @Test
    void shouldGetAllTrainersThatNotAssignedOnTrainee() {
        testInstance.getAllTrainersThatNotAssignedOnTrainee(USERNAME);

        verify(trainerDao).findAllThatNotAssignedOnTrainee(USERNAME);
    }
}