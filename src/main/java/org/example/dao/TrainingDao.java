package org.example.dao;

import org.example.entity.Training;
import org.example.entity.search.TraineeTrainingsCriteria;
import org.example.entity.search.TrainerTrainingsCriteria;

import java.util.Collection;
import java.util.Optional;

public interface TrainingDao {

    void insert(Training training);
    void update(Training training);
    Optional<Training> findByName(String name);
    Collection<Training> findTraineeTrainingsByCriteria(TraineeTrainingsCriteria criteria);
    Collection<Training> findTrainerTrainingsByCriteria(TrainerTrainingsCriteria criteria);
}
