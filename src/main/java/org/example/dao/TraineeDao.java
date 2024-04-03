package org.example.dao;

import org.example.entity.Trainee;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TraineeDao extends CrudRepository<Trainee, Integer> {

    Optional<Trainee> findByUsername(String username);
}
