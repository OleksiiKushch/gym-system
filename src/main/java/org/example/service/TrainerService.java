package org.example.service;

import org.example.entity.Trainer;

import java.util.List;
import java.util.Optional;

public interface TrainerService {

    void createTrainer(Trainer trainer);
    void updateTrainer(Trainer trainer);
    Optional<Trainer> getTrainerForId(Integer id);
    Optional<Trainer> getTrainerForUsername(String username);
    Optional<Trainer> getFullTrainerForUsername(String username);
    List<Trainer> getAllTrainersThatNotAssignedOnTrainee(String traineeUsername);
}
