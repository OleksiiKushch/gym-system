package org.example.facade.impl;

import lombok.Getter;
import org.example.dto.ActionType;
import org.example.dto.TraineeDto;
import org.example.dto.form.search.SearchTraineeTrainingsPayload;
import org.example.dto.request.TrainerWorkloadRequest;
import org.example.dto.response.AfterRegistrationResponse;
import org.example.dto.response.SimpleTrainerResponse;
import org.example.dto.response.TraineeProfileResponse;
import org.example.dto.response.TraineeTrainingResponse;
import org.example.entity.Trainee;
import org.example.entity.Trainer;
import org.example.entity.Training;
import org.example.entity.search.TraineeTrainingsCriteria;
import org.example.exception.AppException;
import org.example.exception.NotFoundException;
import org.example.facade.TraineeFacade;
import org.example.service.TraineeService;
import org.example.service.TrainerService;
import org.example.service.TrainerWorkloadService;
import org.example.service.TrainingService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.example.constants.GeneralConstants.DELETE_ACTIVE_TRAINEE_EXCEPTION_MSG;
import static org.example.constants.GeneralConstants.TRAINEE_NOT_FOUND_EXCEPTION_MSG;
import static org.example.constants.GeneralConstants.TRAINER_NOT_FOUND_EXCEPTION_MSG;

@Getter
@Component
public class DefaultTraineeFacade extends DefaultUserFacade implements TraineeFacade {

    private TraineeService traineeService;
    private TrainerService trainerService;
    private TrainingService trainingService;
    private ModelMapper modelMapper;
    private TrainerWorkloadService trainerWorkloadService;

    @Override
    public AfterRegistrationResponse registerTrainee(TraineeDto traineeDto) {
        Trainee trainee = getModelMapper().map(traineeDto, Trainee.class);
        String password = processNewUserProfile(trainee);
        getTraineeService().createTrainee(trainee);
        return new AfterRegistrationResponse(trainee.getUsername(), password);
    }

    @Override
    public TraineeProfileResponse getTraineeProfile(String username) {
        return getTraineeService().getTraineeForUsername(username)
                .map(trainee -> getModelMapper().map(trainee, TraineeProfileResponse.class))
                .orElseThrow(() -> new NotFoundException(formExceptionMessage(TRAINEE_NOT_FOUND_EXCEPTION_MSG, username)));
    }

    @Override
    public List<TraineeTrainingResponse> getTraineeTrainings(String username, SearchTraineeTrainingsPayload payload) {
        getTraineeByUsernameOrThrowException(username); // check if trainee exists
        TraineeTrainingsCriteria criteria = getModelMapper().map(payload, TraineeTrainingsCriteria.class);
        criteria.setTraineeUsername(username);
        return getTrainingService().getTraineeTrainings(criteria).stream()
                .map(training -> getModelMapper().map(training, TraineeTrainingResponse.class))
                .toList();
    }

    @Override
    public TraineeProfileResponse updateTrainee(String username, TraineeDto traineeDto) {
        Trainee actualTrainee = getTraineeByUsernameOrThrowException(username);
        Trainee newTrainee = getModelMapper().map(traineeDto, Trainee.class);
        setUpdatedFieldsForTrainee(actualTrainee, newTrainee);
        return getModelMapper().map(getTraineeService().updateTrainee(actualTrainee), TraineeProfileResponse.class);
    }

    @Override
    public List<SimpleTrainerResponse> updateTraineeTrainers(String username, List<String> usernamesOfTrainers) {
        List<Trainer> trainers = usernamesOfTrainers.stream()
                .map(this::getTrainerByUsernameOrThrowException)
                .toList();
        getTraineeService().updateTrainersList(username, trainers);
        return trainers.stream()
                .map(trainer -> getModelMapper().map(trainer, SimpleTrainerResponse.class))
                .toList();
    }

    @Override
    @Transactional
    public void deleteTrainee(String username) {
        Trainee trainee = getTraineeByUsernameOrThrowException(username);
        if (trainee.isActive()) {
            throw new AppException(formExceptionMessage(DELETE_ACTIVE_TRAINEE_EXCEPTION_MSG, username));
        }
        getTrainerWorkloadService().sendTrainerWorkload(trainee.getTrainings().stream()
                .filter(training -> training.getTrainingDate().isAfter(getCurrentDate()))
                .map(this::formDeleteTrainingRequest)
                .toList());
        getTraineeService().deleteTrainee(trainee);
    }

    private LocalDate getCurrentDate() {
        return LocalDate.now();
    }

    private TrainerWorkloadRequest formDeleteTrainingRequest(Training training) {
        TrainerWorkloadRequest result = getModelMapper().map(training, TrainerWorkloadRequest.class);
        result.setActionType(ActionType.DELETE);
        return result;
    }

    private void setUpdatedFieldsForTrainee(Trainee currentTrainee, Trainee newTrainee) {
        currentTrainee.setFirstName(newTrainee.getFirstName());
        currentTrainee.setLastName(newTrainee.getLastName());
        currentTrainee.setDateOfBirthday(newTrainee.getDateOfBirthday());
        currentTrainee.setAddress(newTrainee.getAddress());
        currentTrainee.setIsActive(newTrainee.isActive());
    }

    private Trainee getTraineeByUsernameOrThrowException(String username) {
        return getTraineeService().getTraineeForUsername(username)
                .orElseThrow(() -> new NotFoundException(formExceptionMessage(TRAINEE_NOT_FOUND_EXCEPTION_MSG, username)));
    }

    private Trainer getTrainerByUsernameOrThrowException(String username) {
        return getTrainerService().getTrainerForUsername(username)
                .orElseThrow(() -> new AppException(formExceptionMessage(TRAINER_NOT_FOUND_EXCEPTION_MSG, username)));
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
