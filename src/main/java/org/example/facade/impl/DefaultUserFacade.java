package org.example.facade.impl;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.example.dto.TraineeDto;
import org.example.dto.TrainerDto;
import org.example.entity.Trainee;
import org.example.entity.Trainer;
import org.example.entity.User;
import org.example.exception.AppException;
import org.example.facade.UserFacade;
import org.example.mapper.Mapper;
import org.example.service.SessionService;
import org.example.service.TraineeService;
import org.example.service.TrainerService;
import org.example.service.TrainingService;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.example.constants.GeneralConstants.ANONYMOUS_USER_CAN_NOT_BE_DEACTIVATED_EXCEPTION_MSG;
import static org.example.constants.GeneralConstants.ANONYMOUS_USER_CAN_NOT_BE_UPDATED_EXCEPTION_MSG;
import static org.example.constants.GeneralConstants.ANONYMOUS_USER_CAN_NOT_CHANGE_PASSWORD_EXCEPTION_MSG;
import static org.example.constants.GeneralConstants.CURRENT_USER_ATTR;
import static org.example.constants.GeneralConstants.DELETE_ACTIVE_TRAINEE_EXCEPTION_MSG;
import static org.example.constants.GeneralConstants.PASSWORD_DOES_NOT_MUTCH_EXCEPTION_MSG;
import static org.example.constants.GeneralConstants.USERNAME_OR_PASSWORD_DO_NOT_MUTCH_EXCEPTION_MSG;
import static org.example.constants.GeneralConstants.USER_BY_USERNAME_NOT_FOUND_EXCEPTION_MSG;
import static org.example.constants.GeneralConstants.USER_IS_ALREADY_ACTIVE_EXCEPTION_MSG;
import static org.example.constants.GeneralConstants.USER_IS_ALREADY_DEACTIVATED_EXCEPTION_MSG;
import static org.example.constants.GeneralConstants.USER_IS_DEACTIVATED_EXCEPTION_MSG;
import static org.example.constants.GeneralConstants.USER_TYPE_DOES_NOT_MUCH_EXCEPTION_MSG;

@Getter
@Component
public class DefaultUserFacade implements UserFacade {

    private UserService userService;
    private TraineeService traineeService;
    private TrainerService trainerService;
    private TrainingService trainingService;
    private Mapper<TraineeDto, Trainee> traineeMapper;
    private Mapper<TrainerDto, Trainer> trainerMapper;
    private SessionService sessionService;

    @Override
    public void registerTrainee(TraineeDto traineeDto) {
        Trainee trainee = getTraineeMapper().map(traineeDto);
        processNewUserProfile(trainee);
        getTraineeService().createTrainee(trainee);
    }

    @Override
    public void registerTrainer(TrainerDto trainerDto) {
        Trainer trainer = getTrainerMapper().map(trainerDto);
        processNewUserProfile(trainer);
        getTrainerService().createTrainer(trainer);
    }

    @Override
    public void login(String username, String password) {
        User user = getUserService().authenticateUser(username, password)
                .orElseThrow(() -> new AppException(USERNAME_OR_PASSWORD_DO_NOT_MUTCH_EXCEPTION_MSG));
        if (Boolean.FALSE.equals(user.getIsActive())) {
            throw new AppException(USER_IS_DEACTIVATED_EXCEPTION_MSG);
        }
        getSessionService().setCurrentUser(user);
    }

    @Override
    public void logout() {
        getSessionService().removeAttribute(CURRENT_USER_ATTR);
    }

    @Override
    public boolean authorizationCurrentUser() {
        return getSessionService().getCurrentUser().isPresent();
    }

    @Override
    public void changePassword(String currentPassword, String newPassword) {
        User currentUser = getCurrentUserOrThrowException(ANONYMOUS_USER_CAN_NOT_CHANGE_PASSWORD_EXCEPTION_MSG);
        if (!currentUser.getPassword().equals(currentPassword)) {
            throw new AppException(PASSWORD_DOES_NOT_MUTCH_EXCEPTION_MSG);
        }
        currentUser.setPassword(newPassword);
        getUserService().updateUser(currentUser);
    }

    @Override
    public void updateTrainee(TraineeDto traineeDto) {
        User currentUser = getCurrentUserOrThrowException(ANONYMOUS_USER_CAN_NOT_BE_UPDATED_EXCEPTION_MSG);
        if (currentUser instanceof Trainee currentTrainee) {
            Trainee newTrainee = getTraineeMapper().map(traineeDto);
            setUpdatedFieldsForTrainee(currentTrainee, newTrainee);
            getTraineeService().updateTrainee(currentTrainee);
        } else {
            throw new AppException(USER_TYPE_DOES_NOT_MUCH_EXCEPTION_MSG);
        }
    }

    @Override
    public void updateTrainer(TrainerDto trainerDto) {
        User currentUser = getCurrentUserOrThrowException(ANONYMOUS_USER_CAN_NOT_BE_UPDATED_EXCEPTION_MSG);
        if (currentUser instanceof Trainer currentTrainer) {
            Trainer newTrainer = getTrainerMapper().map(trainerDto);
            setUpdatedFieldsForTrainer(currentTrainer, newTrainer);
            getTrainerService().updateTrainer(currentTrainer);
        } else {
            throw new AppException(USER_TYPE_DOES_NOT_MUCH_EXCEPTION_MSG);
        }
    }

    @Override
    public void deactivateUser() {
        User currentUser = getCurrentUserOrThrowException(ANONYMOUS_USER_CAN_NOT_BE_DEACTIVATED_EXCEPTION_MSG);
        if (currentUser.getIsActive() == Boolean.FALSE) {
            throw new AppException(USER_IS_ALREADY_DEACTIVATED_EXCEPTION_MSG);
        }
        currentUser.setActive(Boolean.FALSE);
        getUserService().updateUser(currentUser);
        logout();
    }

    @Override
    public void activateUser(String username) {
        User currentUser = getUserService().getUserForUsername(username)
                .orElseThrow(() -> new AppException(formExceptionMessage(USER_BY_USERNAME_NOT_FOUND_EXCEPTION_MSG, username)));
        if (currentUser.getIsActive() == Boolean.TRUE) {
            throw new AppException(formExceptionMessage(USER_IS_ALREADY_ACTIVE_EXCEPTION_MSG, username));
        }
        currentUser.setActive(Boolean.TRUE);
        getUserService().updateUser(currentUser);
    }

    @Override
    public void deleteTrainee(String username) {
        Trainee trainee = getTraineeService().getTraineeForUsername(username)
                .orElseThrow(() -> new AppException(formExceptionMessage(USER_BY_USERNAME_NOT_FOUND_EXCEPTION_MSG, username)));
        if (trainee.isActive()) {
            throw new AppException(formExceptionMessage(DELETE_ACTIVE_TRAINEE_EXCEPTION_MSG, username));
        }
        getTraineeService().deleteTraineeForUsername(username);
    }

    private void processNewUserProfile(User user) {
        user.setActive(Boolean.TRUE);
        user.setUsername(getUserService().calculateUsername(user));
        if (StringUtils.isEmpty(user.getPassword())) {
            user.setPassword(getUserService().generateRandomPassword());
        }
    }

    private User getCurrentUserOrThrowException(String exceptionMessage) {
        return getSessionService().getCurrentUser()
                .orElseThrow(() -> new AppException(exceptionMessage));
    }

    private void setUpdatedFieldsForTrainee(Trainee currentTrainee, Trainee newTrainee) {
        currentTrainee.setFirstName(newTrainee.getFirstName());
        currentTrainee.setLastName(newTrainee.getLastName());
        currentTrainee.setDateOfBirthday(newTrainee.getDateOfBirthday());
        currentTrainee.setAddress(newTrainee.getAddress());
    }

    private void setUpdatedFieldsForTrainer(Trainer currentTrainer, Trainer newTrainer) {
        currentTrainer.setFirstName(newTrainer.getFirstName());
        currentTrainer.setLastName(newTrainer.getLastName());
        currentTrainer.setSpecialization(newTrainer.getSpecialization());
    }

    private String formExceptionMessage(String message, Object... args) {
        return String.format(message, args);
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
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
    public void setTraineeMapper(Mapper<TraineeDto, Trainee> traineeMapper) {
        this.traineeMapper = traineeMapper;
    }

    @Autowired
    public void setTrainerMapper(Mapper<TrainerDto, Trainer> trainerMapper) {
        this.trainerMapper = trainerMapper;
    }

    @Autowired
    public void setSessionService(SessionService sessionService) {
        this.sessionService = sessionService;
    }
}
