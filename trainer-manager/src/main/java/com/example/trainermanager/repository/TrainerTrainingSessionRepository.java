package com.example.trainermanager.repository;

import com.example.trainermanager.entity.TrainerTrainingSession;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

public interface TrainerTrainingSessionRepository extends CrudRepository<TrainerTrainingSession, Integer> {

    @Query("SELECT YEAR(t.trainingDate) as year, MONTH(t.trainingDate) as month, SUM(t.trainingDuration) as totalDuration " +
            "FROM TrainerTrainingSession t " +
            "WHERE t.username = :username AND t.isActive = true AND t.trainingDate < CURRENT_DATE " +
            "GROUP BY YEAR(t.trainingDate), MONTH(t.trainingDate)")
    List<Object[]> getTotalDurationForEachMonth(String username);

    void deleteByUsernameAndTrainingDate(String username, LocalDate trainingDate);
}
