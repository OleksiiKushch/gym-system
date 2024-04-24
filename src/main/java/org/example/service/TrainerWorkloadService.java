package org.example.service;

import org.example.dto.request.TrainerWorkloadRequest;

import java.time.Month;
import java.time.Year;
import java.util.List;
import java.util.Map;

public interface TrainerWorkloadService {

    void sendTrainerWorkload(List<TrainerWorkloadRequest> workloadRequest);
    Map<Year, Map<Month, Integer>> getTrainerTotalHoursReport(String username);
}
