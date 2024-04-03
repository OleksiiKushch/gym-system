package org.example.dao;

import org.example.entity.Trainee;

import java.util.Collection;
import java.util.Optional;

public interface TraineeDao {

    void insert(Trainee trainee);
    void update(Trainee trainee);
    void remove(Integer id);
    Optional<Trainee> findById(Integer id);
    Collection<Trainee> findAll();
}
