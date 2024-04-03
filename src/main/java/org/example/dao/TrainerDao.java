package org.example.dao;

import org.example.entity.Trainer;

import java.util.Collection;
import java.util.Optional;

public interface TrainerDao {

    void insert(Trainer trainer);
    void update(Trainer trainer);
    Optional<Trainer> findById(Integer id);
    Optional<Trainer> findByUsername(String username);
    Optional<Trainer> findWithTrainingsByUsername(String username);
    Collection<Trainer> findAll();
    Collection<Trainer> findAllThatNotAssignedOnTrainee(String traineeUsername);
}
