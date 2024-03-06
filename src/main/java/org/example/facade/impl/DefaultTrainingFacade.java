package org.example.facade.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.dto.TrainingDto;
import org.example.entity.Trainee;
import org.example.entity.Trainer;
import org.example.entity.Training;
import org.example.exception.AppException;
import org.example.facade.TrainingFacade;
import org.example.service.TraineeService;
import org.example.service.TrainerService;
import org.example.service.TrainingService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import static org.example.constants.GeneralConstants.TRAINEE_NOT_FOUND_EXCEPTION_MSG;
import static org.example.constants.GeneralConstants.TRAINER_NOT_FOUND_EXCEPTION_MSG;

@Getter
@RequiredArgsConstructor
@Component
public class DefaultTrainingFacade implements TrainingFacade {

    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;
    private final ModelMapper modelMapper;

    @Override
    public void createTraining(TrainingDto trainingDto) {
        String traineeUsername = trainingDto.getTraineeUsername();
        Trainee trainee = getTraineeService().getFullTraineeForUsername(traineeUsername)
                .orElseThrow(() -> new AppException(formExceptionMessage(TRAINEE_NOT_FOUND_EXCEPTION_MSG, traineeUsername)));
        String trainerUsername = trainingDto.getTrainerUsername();
        Trainer trainer = getTrainerService().getFullTrainerForUsername(trainerUsername)
                .orElseThrow(() -> new AppException(formExceptionMessage(TRAINER_NOT_FOUND_EXCEPTION_MSG, trainerUsername)));
        Training training = getModelMapper().map(trainingDto, Training.class);
        training.setTrainee(trainee);
        training.setTrainer(trainer);
        getTrainingService().createTraining(training);
    }

    private String formExceptionMessage(String message, Object... args) {
        return String.format(message, args);
    }
}
