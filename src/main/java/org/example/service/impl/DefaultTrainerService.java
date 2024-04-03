package org.example.service.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.IterableUtils;
import org.example.dao.TrainerDao;
import org.example.entity.Trainer;
import org.example.service.TrainerService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Getter
@RequiredArgsConstructor
@Service
public class DefaultTrainerService implements TrainerService {

    private final TrainerDao trainerDao;

    @Override
    public void createTrainer(Trainer trainer) {
        getTrainerDao().save(trainer);
    }

    @Override
    public void updateTrainer(Trainer trainer) {
        getTrainerDao().save(trainer);
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
    public List<Trainer> getAllTrainersThatNotAssignedOnTrainee(String traineeUsername) {
        return IterableUtils.toList(getTrainerDao().findAllThatNotAssignedOnTrainee(traineeUsername));
    }
}
