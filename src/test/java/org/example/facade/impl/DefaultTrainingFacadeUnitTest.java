package org.example.facade.impl;

import org.example.dto.TrainingDto;
import org.example.entity.Trainee;
import org.example.entity.Trainer;
import org.example.entity.Training;
import org.example.exception.AppException;
import org.example.service.TraineeService;
import org.example.service.TrainerService;
import org.example.service.TrainingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultTrainingFacadeUnitTest {

    private static final String TRAINEE_USERNAME = "John.Doe";
    private static final String TRAINER_USERNAME = "Jim.Smith";

    private static final String TRAINEE_NOT_FOUND_EXCEPTION_MSG = "Trainee with username '" + TRAINEE_USERNAME + "' not found";
    private static final String TRAINER_NOT_FOUND_EXCEPTION_MSG = "Trainer with username '" + TRAINER_USERNAME + "' not found";

    @InjectMocks
    DefaultTrainingFacade testInstance;

    @Mock
    TrainingService trainingService;
    @Mock
    TraineeService traineeService;
    @Mock
    TrainerService trainerService;
    @Mock
    ModelMapper modelMapper;

    @Mock
    TrainingDto trainingDto;
    @Mock
    Trainee trainee;
    @Mock
    Trainer trainer;
    @Mock
    Training training;

    @Test
    void shouldCreateTraining() {
        when(trainingDto.getTraineeUsername()).thenReturn(TRAINEE_USERNAME);
        when(traineeService.getTraineeForUsername(TRAINEE_USERNAME)).thenReturn(Optional.of(trainee));
        when(trainingDto.getTrainerUsername()).thenReturn(TRAINER_USERNAME);
        when(trainerService.getTrainerForUsername(TRAINER_USERNAME)).thenReturn(Optional.of(trainer));
        when(modelMapper.map(trainingDto, Training.class)).thenReturn(training);

        testInstance.createTraining(trainingDto);

        verify(training).setTrainee(trainee);
        verify(training).setTrainer(trainer);
        verify(trainingService).createTraining(training);
    }

    @Test
    void createTraining_shouldThrowException_whenTraineeNotFound() {
        when(trainingDto.getTraineeUsername()).thenReturn(TRAINEE_USERNAME);
        when(traineeService.getTraineeForUsername(TRAINEE_USERNAME)).thenReturn(Optional.empty());

        Exception exception = assertThrows(AppException.class, () ->
                testInstance.createTraining(trainingDto));

        assertEquals(TRAINEE_NOT_FOUND_EXCEPTION_MSG, exception.getMessage());
    }

    @Test
    void createTraining_shouldThrowException_whenTrainerNotFound() {
        when(trainingDto.getTraineeUsername()).thenReturn(TRAINEE_USERNAME);
        when(traineeService.getTraineeForUsername(TRAINEE_USERNAME)).thenReturn(Optional.of(trainee));
        when(trainingDto.getTrainerUsername()).thenReturn(TRAINER_USERNAME);
        when(trainerService.getTrainerForUsername(TRAINER_USERNAME)).thenReturn(Optional.empty());

        Exception exception = assertThrows(AppException.class, () ->
                testInstance.createTraining(trainingDto));

        assertEquals(TRAINER_NOT_FOUND_EXCEPTION_MSG, exception.getMessage());
    }
}