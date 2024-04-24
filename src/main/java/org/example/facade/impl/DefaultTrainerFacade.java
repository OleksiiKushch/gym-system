package org.example.facade.impl;

import lombok.Getter;
import org.example.dto.TrainerDto;
import org.example.dto.form.search.SearchTrainerTrainingsPayload;
import org.example.dto.response.AfterRegistrationResponse;
import org.example.dto.response.ReportResponse;
import org.example.dto.response.SimpleTrainerResponse;
import org.example.dto.response.TrainerProfileResponse;
import org.example.dto.response.TrainerTrainingResponse;
import org.example.entity.Trainer;
import org.example.entity.search.TrainerTrainingsCriteria;
import org.example.exception.NotFoundException;
import org.example.facade.TrainerFacade;
import org.example.service.TraineeService;
import org.example.service.TrainerService;
import org.example.service.TrainerWorkloadService;
import org.example.service.TrainingService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.Month;
import java.time.Year;
import java.util.List;
import java.util.Map;

import static org.example.constants.GeneralConstants.TRAINEE_NOT_FOUND_EXCEPTION_MSG;
import static org.example.constants.GeneralConstants.TRAINER_NOT_FOUND_EXCEPTION_MSG;

@Getter
@Component
public class DefaultTrainerFacade extends DefaultUserFacade implements TrainerFacade {

    private TrainerService trainerService;
    private TraineeService traineeService;
    private TrainingService trainingService;
    private ModelMapper modelMapper;
    private TrainerWorkloadService trainerWorkloadService;

    @Override
    public AfterRegistrationResponse registerTrainer(TrainerDto trainerDto) {
        Trainer trainer = getModelMapper().map(trainerDto, Trainer.class);
        String password = processNewUserProfile(trainer);
        getTrainerService().createTrainer(trainer);
        return new AfterRegistrationResponse(trainer.getUsername(), password);
    }

    @Override
    public TrainerProfileResponse getTrainerProfile(String username) {
        return getTrainerService().getTrainerForUsername(username)
                .map(trainer -> getModelMapper().map(trainer, TrainerProfileResponse.class))
                .orElseThrow(() -> new NotFoundException(formExceptionMessage(TRAINER_NOT_FOUND_EXCEPTION_MSG, username)));
    }

    @Override
    public List<SimpleTrainerResponse> getTrainersThatNotAssignedOnTrainee(String username) {
        checkIfTraineeExists(username);
        return getTrainerService().getAllTrainersThatNotAssignedOnTrainee(username).stream()
                .map(trainer -> getModelMapper().map(trainer, SimpleTrainerResponse.class))
                .toList();
    }

    @Override
    public List<TrainerTrainingResponse> getTrainerTrainings(String username, SearchTrainerTrainingsPayload payload) {
        getTrainerByUsernameOrThrowException(username); // check if trainer exists
        TrainerTrainingsCriteria criteria = getModelMapper().map(payload, TrainerTrainingsCriteria.class);
        criteria.setTrainerUsername(username);
        return getTrainingService().getTrainerTrainings(criteria).stream()
                .map(training -> getModelMapper().map(training, TrainerTrainingResponse.class))
                .toList();
    }

    @Override
    public TrainerProfileResponse updateTrainer(String username, TrainerDto trainerDto) {
        Trainer actualTrainer = getTrainerByUsernameOrThrowException(username);
        Trainer newTrainer = getModelMapper().map(trainerDto, Trainer.class);
        setUpdatedFieldsForTrainer(actualTrainer, newTrainer);
        getTrainerService().updateTrainer(actualTrainer);
        return getTrainerProfile(actualTrainer.getUsername());
    }

    @Override
    public ReportResponse formTrainerTotalHoursReport(String username) {
        Trainer actualTrainer = getTrainerByUsernameOrThrowException(username);
        Map<Year, Map<Month, Integer>> history = getTrainerWorkloadService().getTrainerTotalHoursReport(username);
        ReportResponse result = getModelMapper().map(actualTrainer, ReportResponse.class);
        result.setHistory(history);
        return result;
    }

    private void setUpdatedFieldsForTrainer(Trainer currentTrainer, Trainer newTrainer) {
        currentTrainer.setFirstName(newTrainer.getFirstName());
        currentTrainer.setLastName(newTrainer.getLastName());
        currentTrainer.setSpecialization(newTrainer.getSpecialization());
        currentTrainer.setIsActive(newTrainer.getIsActive());
    }

    private Trainer getTrainerByUsernameOrThrowException(String username) {
        return getTrainerService().getTrainerForUsername(username)
                .orElseThrow(() -> new NotFoundException(formExceptionMessage(TRAINER_NOT_FOUND_EXCEPTION_MSG, username)));
    }

    private void checkIfTraineeExists(String username) {
        getTraineeService().getTraineeForUsername(username)
                .orElseThrow(() -> new NotFoundException(formExceptionMessage(TRAINEE_NOT_FOUND_EXCEPTION_MSG, username)));
    }

    @Autowired
    public void setTrainerService(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    @Autowired
    public void setTraineeService(TraineeService traineeService) {
        this.traineeService = traineeService;
    }

    @Autowired
    public void setTrainingService(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Autowired
    public void setTrainerWorkloadService(@Qualifier("jmsTrainerWorkloadService") TrainerWorkloadService trainerWorkloadService) {
        this.trainerWorkloadService = trainerWorkloadService;
    }
}
