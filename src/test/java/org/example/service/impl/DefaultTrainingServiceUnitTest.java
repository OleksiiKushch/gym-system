package org.example.service.impl;

import org.example.dao.TraineeDao;
import org.example.dao.TrainingDao;
import org.example.dao.TrainingSearchDao;
import org.example.entity.Trainee;
import org.example.entity.Trainer;
import org.example.entity.Training;
import org.example.entity.search.TraineeTrainingsCriteria;
import org.example.entity.search.TrainerTrainingsCriteria;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
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
    TrainingSearchDao trainingSearchDao;
    @Mock
    TraineeDao traineeDao;

    @Mock
    Training training;
    @Mock
    Trainee trainee;
    @Mock
    Trainer trainer;
    @Mock
    TraineeTrainingsCriteria traineeTrainingsCriteria;
    @Mock
    TrainerTrainingsCriteria trainerTrainingsCriteria;

    @Test
    void shouldCreateTraining_whenTrainerNotAssignedForTrainee() {
        when(training.getTrainee()).thenReturn(trainee);
        when(training.getTrainer()).thenReturn(trainer);
        when(trainee.getTrainers()).thenReturn(new ArrayList<>());

        testInstance.createTraining(training);

        verify(trainee).addTrainer(trainer);
        verify(traineeDao).save(trainee);
        verify(trainingDao).save(training);
    }

    @Test
    void shouldCreateTraining_whenTrainerAlreadyAssignedForTrainee() {
        when(training.getTrainee()).thenReturn(trainee);
        when(training.getTrainer()).thenReturn(trainer);
        when(trainee.getTrainers()).thenReturn(List.of(trainer));

        testInstance.createTraining(training);

        verify(trainee, never()).addTrainer(trainer);
        verify(traineeDao, never()).save(trainee);
        verify(trainingDao).save(training);
    }
    
    @Test
    void shouldGetTrainingForName() {
        when(trainingDao.findByTrainingName(TRAINING_NAME)).thenReturn(Optional.of(training));

        Optional<Training> actualResult = testInstance.getTrainingForName(TRAINING_NAME);

        assertTrue(actualResult.isPresent());
        assertEquals(training, actualResult.get());
    }

    @Test
    void shouldGetTraineeTrainings() {
        testInstance.getTraineeTrainings(traineeTrainingsCriteria);

        verify(trainingSearchDao).findTraineeTrainingsByCriteria(traineeTrainingsCriteria);
    }

    @Test
    void shouldGetTrainerTrainings() {
        testInstance.getTrainerTrainings(trainerTrainingsCriteria);

        verify(trainingSearchDao).findTrainerTrainingsByCriteria(trainerTrainingsCriteria);
    }
}