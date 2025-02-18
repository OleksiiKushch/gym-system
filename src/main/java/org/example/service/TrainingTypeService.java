package org.example.service;

import org.example.entity.TrainingType;
import org.example.entity.TrainingTypeEnum;

import java.util.List;
import java.util.Optional;

public interface TrainingTypeService {

    Optional<TrainingType> getTrainingTypeForName(TrainingTypeEnum trainingType);
    List<TrainingType> getAllTrainingTypes();
}
