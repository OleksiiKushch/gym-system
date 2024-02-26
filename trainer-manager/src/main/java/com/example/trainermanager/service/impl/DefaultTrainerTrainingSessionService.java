package com.example.trainermanager.service.impl;

import com.example.trainermanager.entity.TrainerTrainingSession;
import com.example.trainermanager.repository.TrainerTrainingSessionRepository;
import com.example.trainermanager.service.TrainerTrainingSessionService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Month;
import java.time.Year;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
@Service
public class DefaultTrainerTrainingSessionService implements TrainerTrainingSessionService {

    private static final int YEAR_INDEX = 0;
    private static final int MONTH_INDEX = 1;
    private static final int TOTAL_INDEX = 2;

    private final TrainerTrainingSessionRepository trainerTrainingSessionRepository;

    @Override
    public void saveTrainerTrainingSession(TrainerTrainingSession trainerTrainingSession) {
        getTrainerTrainingSessionRepository().save(trainerTrainingSession);
    }

    @Override
    public Map<Year, Map<Month, Integer>> formReport(String username) {
        return getTrainerTrainingSessionRepository().getTotalDurationForEachMonth(username).stream()
                .collect(Collectors.groupingBy(
                        objects -> Year.of((Integer) objects[YEAR_INDEX]),
                        Collectors.toMap(
                                objects -> Month.of((Integer) objects[MONTH_INDEX]),
                                objects -> ((Long) objects[TOTAL_INDEX]).intValue()
                        )
                ));
    }

    @Override
    @Transactional
    public void deleteTrainerTrainingSession(TrainerTrainingSession trainerTrainingSession) {
        getTrainerTrainingSessionRepository().deleteByUsernameAndTrainingDate(
                trainerTrainingSession.getUsername(), trainerTrainingSession.getTrainingDate());
    }
}
