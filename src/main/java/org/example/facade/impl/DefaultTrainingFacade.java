package org.example.facade.impl;

import lombok.Getter;
import org.example.dto.TrainingDto;
import org.example.entity.Trainee;
import org.example.entity.Trainer;
import org.example.entity.Training;
import org.example.exception.AppException;
import org.example.facade.TrainingFacade;
import org.example.mapper.Mapper;
import org.example.service.TraineeService;
import org.example.service.TrainerService;
import org.example.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.example.constants.GeneralConstants.TRAINEE_NOT_FOUND_EXCEPTION_MSG;
import static org.example.constants.GeneralConstants.TRAINER_NOT_FOUND_EXCEPTION_MSG;

@Getter
@Component
public class DefaultTrainingFacade implements TrainingFacade {

    private TraineeService traineeService;
    private TrainerService trainerService;
    private TrainingService trainingService;
    private Mapper<TrainingDto, Training> trainingMapper;

    @Override
    public void createTraining(TrainingDto trainingDto) {
        String traineeUsername = trainingDto.getTraineeUsername();
        Trainee trainee = getTraineeService().getFullTraineeForUsername(traineeUsername)
                .orElseThrow(() -> new AppException(formExceptionMessage(TRAINEE_NOT_FOUND_EXCEPTION_MSG, traineeUsername)));
        String trainerUsername = trainingDto.getTrainerUsername();
        Trainer trainer = getTrainerService().getFullTrainerForUsername(trainerUsername)
                .orElseThrow(() -> new AppException(formExceptionMessage(TRAINER_NOT_FOUND_EXCEPTION_MSG, trainerUsername)));
        Training training = getTrainingMapper().map(trainingDto);
        training.setTrainee(trainee);
        training.setTrainer(trainer);
        getTrainingService().createTraining(training);
    }

    private String formExceptionMessage(String message, Object... args) {
        return String.format(message, args);
    }

    @Autowired
    public void setTrainingService(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    @Autowired
    public void setTraineeService(TraineeService traineeService) {
        this.traineeService = traineeService;
    }

    @Autowired
    public void setTrainerService(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    @Autowired
    public void setTrainingMapper(Mapper<TrainingDto, Training> trainingMapper) {
        this.trainingMapper = trainingMapper;
    }
}
