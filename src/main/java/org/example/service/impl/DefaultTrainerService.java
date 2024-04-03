package org.example.service.impl;

import lombok.Getter;
import org.example.dao.TrainerDao;
import org.example.entity.Trainer;
import org.example.service.TrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Getter
@Service
public class DefaultTrainerService implements TrainerService {

    @Autowired
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
}
