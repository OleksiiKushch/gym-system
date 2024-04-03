package org.example.facade;

import org.example.dto.TraineeDto;
import org.example.dto.TrainerDto;

public interface UserFacade {

    void registerTrainee(TraineeDto traineeDto);
    void registerTrainer(TrainerDto trainerDto);
    void login(String username, String password);
    void logout();
    boolean authorizationCurrentUser();
    void changePassword(String currentPassword, String newPassword);
    void updateTrainee(TraineeDto traineeDto);
    void updateTrainer(TrainerDto trainerDto);
    void deactivateUser();
    void activateUser(String username);
    void deleteTrainee(String username);
}
