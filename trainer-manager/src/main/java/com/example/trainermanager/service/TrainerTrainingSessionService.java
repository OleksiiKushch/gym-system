package com.example.trainermanager.service;

import com.example.trainermanager.entity.TrainerTrainingSession;

import java.time.Month;
import java.time.Year;
import java.util.Map;

public interface TrainerTrainingSessionService {

    void saveTrainerTrainingSession(TrainerTrainingSession trainerTrainingSession);
    Map<Year, Map<Month, Integer>> formReport(String username);
    void deleteTrainerTrainingSession(TrainerTrainingSession trainerTrainingSession);
}
