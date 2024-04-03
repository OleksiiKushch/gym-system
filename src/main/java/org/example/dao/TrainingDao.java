package org.example.dao;

import org.example.entity.Training;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TrainingDao extends CrudRepository<Training, Integer> {

    Optional<Training> findByTrainingName(String trainingName);
}
