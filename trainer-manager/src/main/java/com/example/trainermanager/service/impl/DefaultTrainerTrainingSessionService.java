package com.example.trainermanager.service.impl;

import com.example.trainermanager.dto.request.TrainerWorkloadRequest;
import com.example.trainermanager.entity.TrainerTrainingSummary;
import com.example.trainermanager.repository.TrainerTrainingSessionRepository;
import com.example.trainermanager.service.TrainerTrainingSessionService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
@Service
public class DefaultTrainerTrainingSessionService implements TrainerTrainingSessionService {

    public static final String TRAINER_NOT_FOUND = "Trainer with username '%s' not found";
    public static final Integer TRAINING_DURATION_START_VALUE = 0;
    private static final int YEAR_INDEX = 0;
    private static final int MONTH_INDEX = 1;
    private static final int TOTAL_INDEX = 2;

    private final TrainerTrainingSessionRepository trainerTrainingSessionRepository;

    @Override
    @Transactional
    public void updateTrainerTrainingSummary(TrainerWorkloadRequest trainerWorkloadRequest) {
        getTrainerTrainingSessionRepository()
                .findByTrainerUsernameAndTrainerFirstNameAndTrainerLastName(trainerWorkloadRequest.getTrainerUsername(),
                        trainerWorkloadRequest.getTrainerFirstName(), trainerWorkloadRequest.getTrainerLastName())
                .ifPresentOrElse(summary -> {
                    Map<Integer, Map<Month, Integer>> report = summary.getReport();
                    Integer year = getYear(trainerWorkloadRequest);
                    if (report.containsKey(year)) {
                        increaseTrainingDuration(report, year, trainerWorkloadRequest);
                    } else {
                        createNewEntryForTheMonth(report, year, trainerWorkloadRequest);
                    }
                    summary.setReport(report);
                    getTrainerTrainingSessionRepository().save(summary);
                }, () -> startNewTrainerTrainingHistory(trainerWorkloadRequest));
    }

    @Override
    public Map<Integer, Map<Month, Integer>> formReport(String username) {
        final LocalDate currentDate = LocalDate.now();
        return getTrainerTrainingSessionRepository().findByTrainerUsername(username)
                .orElseThrow(() -> new IllegalArgumentException(forErrorMessages(username)))
                .getReport()
                .entrySet().stream()
                .filter(entry -> entry.getKey() <= currentDate.getYear())
                .peek(entry -> {
                    if (entry.getKey().equals(currentDate.getYear())) {
                        entry.getValue().entrySet()
                                .removeIf(monthEntry -> monthEntry.getKey().getValue() > currentDate.getMonthValue());
                    }
                }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    @Transactional
    public void deleteTrainerTrainingSession(TrainerWorkloadRequest trainerWorkloadRequest) {
        getTrainerTrainingSessionRepository()
                .findByTrainerUsernameAndTrainerFirstNameAndTrainerLastName(trainerWorkloadRequest.getTrainerUsername(),
                        trainerWorkloadRequest.getTrainerFirstName(), trainerWorkloadRequest.getTrainerLastName())
                .ifPresent(summary -> {
                    Map<Integer, Map<Month, Integer>> report = summary.getReport();
                    Integer year = getYear(trainerWorkloadRequest);
                    if (report.containsKey(year)) {
                        Map<Month, Integer> yearReport = report.get(year);
                        Month month = getMonth(trainerWorkloadRequest);
                        if (yearReport.containsKey(month)) {
                            reduceTrainingDuration(yearReport, month, trainerWorkloadRequest);
                            removeMonthIfEmpty(yearReport, month);
                            removeYearIfEmpty(report, year);
                            summary.setReport(report);
                            getTrainerTrainingSessionRepository().save(summary);
                        }
                    }
                });
    }

    private void startNewTrainerTrainingHistory(TrainerWorkloadRequest request) {
        TrainerTrainingSummary result = new TrainerTrainingSummary();
        result.setTrainerUsername(request.getTrainerUsername());
        result.setTrainerFirstName(request.getTrainerFirstName());
        result.setTrainerLastName(request.getTrainerLastName());
        result.setTrainerStatus(Boolean.TRUE);
        result.setReport(Map.of(getYear(request),
                Map.of(getMonth(request), request.getTrainingDuration())));
        getTrainerTrainingSessionRepository().insert(result);
    }

    private Integer getYear(TrainerWorkloadRequest request) {
        return request.getTrainingDate().getYear();
    }

    private Month getMonth(TrainerWorkloadRequest request) {
        return Month.of(request.getTrainingDate().getMonthValue());
    }

    private void increaseTrainingDuration(Map<Integer, Map<Month, Integer>> report, Integer year,
                                          TrainerWorkloadRequest request) {
        Map<Month, Integer> yearReport = report.get(year);
        Month month = getMonth(request);
        yearReport.put(month, yearReport.getOrDefault(month, TRAINING_DURATION_START_VALUE) +
                request.getTrainingDuration());
    }

    private void createNewEntryForTheMonth(Map<Integer, Map<Month, Integer>> report, Integer year,
                                           TrainerWorkloadRequest request) {
        Map<Month, Integer> newYearReport = new HashMap<>();
        newYearReport.put(getMonth(request), request.getTrainingDuration());
        report.put(year, newYearReport);
    }

    private void reduceTrainingDuration(Map<Month, Integer> yearReport, Month month, TrainerWorkloadRequest request) {
        yearReport.put(month, yearReport.get(month) - request.getTrainingDuration());
    }

    private void removeMonthIfEmpty(Map<Month, Integer> yearReport, Month month) {
        if (TRAINING_DURATION_START_VALUE.equals(yearReport.get(month))) {
            yearReport.remove(month);
        }
    }

    private void removeYearIfEmpty(Map<Integer, Map<Month, Integer>> report, Integer year) {
        if (report.get(year).isEmpty()) {
            report.remove(year);
        }
    }

    private String forErrorMessages(Object... arguments) {
        return String.format(TRAINER_NOT_FOUND, arguments);
    }
}
