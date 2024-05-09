package com.example.trainermanager.service;

import com.example.trainermanager.dto.request.TrainerWorkloadRequest;

import java.time.Month;
import java.util.Map;

public interface TrainerTrainingSessionService {

    void updateTrainerTrainingSummary(TrainerWorkloadRequest trainerWorkloadRequest);
    Map<Integer, Map<Month, Integer>> formReport(String username);
    void deleteTrainerTrainingSession(TrainerWorkloadRequest trainerWorkloadRequest);
}
