package org.example.service.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.IterableUtils;
import org.example.dao.TrainingTypeDao;
import org.example.entity.TrainingType;
import org.example.entity.TrainingTypeEnum;
import org.example.service.TrainingTypeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Getter
@RequiredArgsConstructor
@Service
public class DefaultTrainingTypeService implements TrainingTypeService {

    private final TrainingTypeDao trainingTypeDao;

    @Override
    public Optional<TrainingType> getTrainingTypeForName(TrainingTypeEnum trainingType) {
        return getTrainingTypeDao().findByName(trainingType);
    }

    @Override
    public List<TrainingType> getAllTrainingTypes() {
        return IterableUtils.toList(getTrainingTypeDao().findAll());
    }
}
