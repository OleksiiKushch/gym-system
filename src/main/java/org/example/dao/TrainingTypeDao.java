package org.example.dao;

import org.example.entity.TrainingType;
import org.example.entity.TrainingTypeEnum;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TrainingTypeDao extends CrudRepository<TrainingType, Integer> {

    Optional<TrainingType> findByName(TrainingTypeEnum trainingType);
}
