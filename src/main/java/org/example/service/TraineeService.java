package org.example.service;

import org.example.entity.Trainee;
import org.example.entity.Trainer;

import java.util.List;
import java.util.Optional;

public interface TraineeService {

    void createTrainee(Trainee trainee);
    void updateTrainee(Trainee trainee);
    void deleteTrainee(Trainee trainee);
    void deleteTraineeForUsername(String username);
    Optional<Trainee> getTraineeForId(Integer id);
    Optional<Trainee> getTraineeForUsername(String username);
    Optional<Trainee> getFullTraineeForUsername(String username);
    void updateTrainersList(Integer id, List<Trainer> newTrainers);
}
