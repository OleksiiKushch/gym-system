package org.example.dao;

import org.example.entity.Trainee;
import org.example.entity.Trainer;

import java.util.Collection;
import java.util.Optional;

public interface TraineeDao {

    void insert(Trainee trainee);
    void update(Trainee trainee);
    void remove(Integer id);
    void removeByUsername(String username);
    Optional<Trainee> findById(Integer id);
    Optional<Trainee> findByUsername(String username);
    Optional<Trainee> findWithTrainingsByUsername(String username);
    Collection<Trainee> findAll();
    void updateTrainersList(String username, Collection<Trainer> newTrainers);
}
