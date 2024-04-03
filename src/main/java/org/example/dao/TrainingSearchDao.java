package org.example.dao;

import org.example.entity.Training;
import org.example.entity.search.TraineeTrainingsCriteria;
import org.example.entity.search.TrainerTrainingsCriteria;

public interface TrainingSearchDao {

    Iterable<Training> findTraineeTrainingsByCriteria(TraineeTrainingsCriteria criteria);
    Iterable<Training> findTrainerTrainingsByCriteria(TrainerTrainingsCriteria criteria);
}
