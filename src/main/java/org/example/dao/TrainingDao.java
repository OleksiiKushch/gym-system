package org.example.dao;

import org.example.entity.Training;

import java.util.Optional;

public interface TrainingDao {

    void insert(Training training);
    Optional<Training> findByName(String name);
}
