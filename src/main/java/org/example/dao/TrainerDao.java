package org.example.dao;

import org.example.entity.Trainer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

import static org.example.constants.PersistenceLayerConstants.FIND_ALL_TRAINERS_THAT_NOT_ASSIGNED_TO_TRAINEE_QUERY_NAME;
import static org.example.constants.PersistenceLayerConstants.USERNAME_PARAM;

public interface TrainerDao extends CrudRepository<Trainer, Integer> {

    Optional<Trainer> findByUsername(String username);
    @Query(name = FIND_ALL_TRAINERS_THAT_NOT_ASSIGNED_TO_TRAINEE_QUERY_NAME)
    Iterable<Trainer> findAllThatNotAssignedOnTrainee(@Param(USERNAME_PARAM) String traineeUsername);
}
