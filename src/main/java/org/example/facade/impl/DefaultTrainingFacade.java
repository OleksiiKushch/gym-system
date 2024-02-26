package org.example.facade.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.dto.ActionType;
import org.example.dto.TrainingDto;
import org.example.dto.request.TrainerWorkloadRequest;
import org.example.entity.Trainee;
import org.example.entity.Trainer;
import org.example.entity.Training;
import org.example.exception.AppException;
import org.example.facade.TrainingFacade;
import org.example.service.TraineeService;
import org.example.service.TrainerService;
import org.example.service.TrainerWorkloadService;
import org.example.service.TrainingService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.example.constants.GeneralConstants.TRAINEE_NOT_FOUND_EXCEPTION_MSG;
import static org.example.constants.GeneralConstants.TRAINER_NOT_FOUND_EXCEPTION_MSG;
import static org.example.constants.GeneralConstants.TRAINING_NOT_FOUND_EXCEPTION_MSG;

@Getter
@RequiredArgsConstructor
@Component
public class DefaultTrainingFacade implements TrainingFacade {

    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;
    private final ModelMapper modelMapper;
    private final TrainerWorkloadService trainerWorkloadService;

    @Override
    @Transactional
    public void createTraining(TrainingDto trainingDto) {
        String traineeUsername = trainingDto.getTraineeUsername();
        Trainee trainee = getTraineeService().getTraineeForUsername(traineeUsername)
                .orElseThrow(() -> new AppException(formExceptionMessage(TRAINEE_NOT_FOUND_EXCEPTION_MSG, traineeUsername)));
        String trainerUsername = trainingDto.getTrainerUsername();
        Trainer trainer = getTrainerService().getTrainerForUsername(trainerUsername)
                .orElseThrow(() -> new AppException(formExceptionMessage(TRAINER_NOT_FOUND_EXCEPTION_MSG, trainerUsername)));
        Training training = getModelMapper().map(trainingDto, Training.class);
        training.setTrainee(trainee);
        training.setTrainer(trainer);
        getTrainingService().createTraining(training);
        getTrainerWorkloadService().sendTrainerWorkload(List.of(formCreateTrainingRequest(training)));
    }

    @Override
    @Transactional
    public void deleteTraining(Integer trainingId) {
        Training training = getTrainingService().getTrainingForId(trainingId)
                .orElseThrow(() -> new AppException(formExceptionMessage(TRAINING_NOT_FOUND_EXCEPTION_MSG, trainingId)));
        getTrainingService().removeTraining(training);
        if (training.getTrainingDate().isAfter(getCurrentDate())) {
            getTrainerWorkloadService().sendTrainerWorkload(List.of(formDeleteTrainingRequest(training)));
        }
    }

    private String formExceptionMessage(String message, Object... args) {
        return String.format(message, args);
    }

    private TrainerWorkloadRequest formCreateTrainingRequest(Training training) {
        TrainerWorkloadRequest result = getModelMapper().map(training, TrainerWorkloadRequest.class);
        result.setActionType(ActionType.ADD);
        return result;
    }

    private TrainerWorkloadRequest formDeleteTrainingRequest(Training training) {
        TrainerWorkloadRequest result = getModelMapper().map(training, TrainerWorkloadRequest.class);
        result.setActionType(ActionType.DELETE);
        return result;
    }

    private LocalDate getCurrentDate() {
        return LocalDate.now();
    }
}
