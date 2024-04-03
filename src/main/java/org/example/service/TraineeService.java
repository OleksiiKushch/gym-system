package org.example.service;

import org.example.entity.Trainee;

import java.util.Optional;

public interface TraineeService {

    void createTrainee(Trainee trainee);
    void updateTrainee(Trainee trainee);
    void deleteTrainee(Trainee trainee);
    Optional<Trainee> getTraineeForId(Integer id);
}
