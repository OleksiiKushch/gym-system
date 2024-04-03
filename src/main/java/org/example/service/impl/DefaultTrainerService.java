package org.example.service.impl;

import lombok.Getter;
import org.example.dao.TrainerDao;
import org.example.entity.Trainer;
import org.example.service.TrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Getter
@Service
public class DefaultTrainerService implements TrainerService {

    @Autowired
    @Qualifier("hibernateTrainerDao")
    private TrainerDao trainerDao;

    @Override
    public void createTrainer(Trainer trainer) {
        getTrainerDao().insert(trainer);
    }

    @Override
    public void updateTrainer(Trainer trainer) {
        getTrainerDao().update(trainer);
    }

    @Override
    public Optional<Trainer> getTrainerForId(Integer id) {
        return getTrainerDao().findById(id);
    }

    @Override
    public Optional<Trainer> getTrainerForUsername(String username) {
        return getTrainerDao().findByUsername(username);
    }

    @Override
    public Optional<Trainer> getFullTrainerForUsername(String username) {
        return getTrainerDao().findWithTrainingsByUsername(username);
    }

    @Override
    public List<Trainer> getAllTrainersThatNotAssignedOnTrainee(String traineeUsername) {
        return getTrainerDao().findAllThatNotAssignedOnTrainee(traineeUsername).stream().toList();
    }
}
