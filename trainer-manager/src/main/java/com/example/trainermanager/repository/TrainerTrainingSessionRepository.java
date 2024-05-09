package com.example.trainermanager.repository;

import com.example.trainermanager.entity.TrainerTrainingSummary;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TrainerTrainingSessionRepository extends MongoRepository<TrainerTrainingSummary, String> {

    Optional<TrainerTrainingSummary> findByTrainerUsername(String username);
    Optional<TrainerTrainingSummary> findByTrainerUsernameAndTrainerFirstNameAndTrainerLastName
            (String trainerUsername, String trainerFirstName, String trainerLastName);
}
