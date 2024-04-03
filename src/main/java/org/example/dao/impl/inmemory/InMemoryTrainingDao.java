package org.example.dao.impl.inmemory;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.example.dao.TrainingDao;
import org.example.entity.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Getter
@Component
public class InMemoryTrainingDao implements TrainingDao {

    private static final String TRAINING_ALREADY_EXISTS_EXCEPTION_MESSAGE = "Training with name \"%s\" already exists";
    private static final String TRAINING_ALREADY_EXISTS_LOG_MESSAGE = "Training with name {} already exists";

    private Map<String, Training> trainingStorage;

    @Override
    public void insert(Training training) {
        if (!getTrainingStorage().containsKey(training.getTrainingName())) {
            getTrainingStorage().put(training.getTrainingName(), training);
        } else {
            log.error(TRAINING_ALREADY_EXISTS_LOG_MESSAGE, training.getTrainingName());
            throw new IllegalArgumentException(String.format(TRAINING_ALREADY_EXISTS_EXCEPTION_MESSAGE, training.getTrainingName()));
        }
    }

    @Override
    public Optional<Training> findByName(String name) {
        return Optional.ofNullable(getTrainingStorage().get(name));
    }

    @Autowired
    public void setTrainingStorage(Map<String, Training> trainingStorage) {
        this.trainingStorage = trainingStorage;
    }
}
