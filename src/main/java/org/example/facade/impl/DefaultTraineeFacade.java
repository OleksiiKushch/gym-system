package org.example.facade.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.example.dto.TraineeDto;
import org.example.dto.form.search.SearchTraineeTrainingsPayload;
import org.example.dto.response.AfterRegistrationResponse;
import org.example.dto.response.SimpleTrainerResponse;
import org.example.dto.response.TraineeProfileResponse;
import org.example.dto.response.TraineeTrainingResponse;
import org.example.entity.Trainee;
import org.example.entity.Trainer;
import org.example.entity.search.TraineeTrainingsCriteria;
import org.example.exception.AppException;
import org.example.exception.NotFoundException;
import org.example.facade.TraineeFacade;
import org.example.service.TraineeService;
import org.example.service.TrainerService;
import org.example.service.TrainingService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.example.constants.GeneralConstants.DELETE_ACTIVE_TRAINEE_EXCEPTION_MSG;
import static org.example.constants.GeneralConstants.TRAINEE_NOT_FOUND_EXCEPTION_MSG;
import static org.example.constants.GeneralConstants.TRAINER_NOT_FOUND_EXCEPTION_MSG;

@Getter
@Setter
@RequiredArgsConstructor
@Component
public class DefaultTraineeFacade extends DefaultUserFacade implements TraineeFacade {

    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;
    private final ModelMapper modelMapper;

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
    public void deleteTrainee(String username) {
        Trainee trainee = getTraineeByUsernameOrThrowException(username);
        if (trainee.isActive()) {
            throw new AppException(formExceptionMessage(DELETE_ACTIVE_TRAINEE_EXCEPTION_MSG, username));
        }
        getTraineeService().deleteTrainee(trainee);
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
}
