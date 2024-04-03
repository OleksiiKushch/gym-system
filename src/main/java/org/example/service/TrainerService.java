package org.example.service;

import org.example.entity.Trainer;

import java.util.Optional;

public interface TrainerService {

    void createTrainer(Trainer trainer);
    void updateTrainer(Trainer trainer);
    Optional<Trainer> getTrainerForId(Integer id);
}
