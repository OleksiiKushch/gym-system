package org.example.service;

import org.example.entity.Trainee;
import org.example.entity.Trainer;

import java.util.List;
import java.util.Optional;

public interface TraineeService {

    void createTrainee(Trainee trainee);
    Trainee updateTrainee(Trainee trainee);
    void deleteTrainee(Trainee trainee);
    Optional<Trainee> getTraineeForUsername(String username);
    void updateTrainersList(String username, List<Trainer> newTrainers);
}
