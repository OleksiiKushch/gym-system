package org.example.service;

import org.example.entity.Training;
import org.example.entity.search.TraineeTrainingsCriteria;
import org.example.entity.search.TrainerTrainingsCriteria;

import java.util.List;
import java.util.Optional;

public interface TrainingService {

    void createTraining(Training training);
    Optional<Training> getTrainingForId(Integer id);
    Optional<Training> getTrainingForName(String name);
    List<Training> getTraineeTrainings(TraineeTrainingsCriteria criteria);
    List<Training> getTrainerTrainings(TrainerTrainingsCriteria criteria);
    void removeTraining(Training training);
}
