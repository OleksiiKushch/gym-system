package org.example.service;

import org.example.entity.Training;

import java.util.Optional;

public interface TrainingService {

    void createTraining(Training training);
    Optional<Training> getTrainingForName(String name);
}
