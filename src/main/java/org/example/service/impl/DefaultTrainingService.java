package org.example.service.impl;

import lombok.Getter;
import org.example.dao.TrainingDao;
import org.example.entity.Training;
import org.example.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Getter
@Service
public class DefaultTrainingService implements TrainingService {

    @Autowired
    private TrainingDao trainingDao;

    @Override
    public void createTraining(Training training) {
        getTrainingDao().insert(training);
    }

    @Override
    public Optional<Training> getTrainingForName(String name) {
        return getTrainingDao().findByName(name);
    }
}
