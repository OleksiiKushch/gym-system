package org.example.service.impl;

import lombok.Getter;
import org.example.dao.TrainingTypeDao;
import org.example.entity.TrainingType;
import org.example.entity.TrainingTypeEnum;
import org.example.service.TrainingTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Getter
@Service
public class DefaultTrainingTypeService implements TrainingTypeService {

    @Autowired
    @Qualifier("hibernateTrainingTypeDao")
    private TrainingTypeDao trainingTypeDao;

    @Override
    public Optional<TrainingType> getTrainingTypeForName(TrainingTypeEnum trainingType) {
        return getTrainingTypeDao().findTrainingType(trainingType);
    }

    @Override
    public List<TrainingType> getAllTrainingTypes() {
        return getTrainingTypeDao().findAllTrainingTypes().stream().toList();
    }
}
