package org.example.dao;

import org.example.entity.TrainingType;
import org.example.entity.TrainingTypeEnum;

import java.util.Collection;
import java.util.Optional;

public interface TrainingTypeDao {

    Optional<TrainingType> findTrainingType(TrainingTypeEnum trainingType);
    Collection<TrainingType> findAllTrainingTypes();
}
