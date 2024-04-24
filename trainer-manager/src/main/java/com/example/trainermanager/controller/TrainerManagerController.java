package com.example.trainermanager.controller;

import com.example.trainermanager.dto.request.TrainerWorkloadRequest;
import com.example.trainermanager.entity.ActionType;
import com.example.trainermanager.entity.TrainerTrainingSession;
import com.example.trainermanager.service.TrainerTrainingSessionService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Getter
@RequiredArgsConstructor
@RestController
public class TrainerManagerController {

    private static final String RECEIVED_REQUEST_TO_UPDATE_TRAINING_SESSION_MSG =
            "Received request to update training session: {}";

    private final TrainerTrainingSessionService trainerTrainingSessionService;
    private final ModelMapper modelMapper;

    @PostMapping("/training-session")
    public ResponseEntity<?> updateTrainingSession(@RequestBody List<TrainerWorkloadRequest> workloadRequests) {
        log.info(RECEIVED_REQUEST_TO_UPDATE_TRAINING_SESSION_MSG, workloadRequests);

        for (TrainerWorkloadRequest workloadRequest : workloadRequests) {
            TrainerTrainingSession trainerTrainingSession =
                    getModelMapper().map(workloadRequest, TrainerTrainingSession.class);
            switch (ActionType.valueOf(workloadRequest.getActionType())) {
                case ADD:
                    getTrainerTrainingSessionService().saveTrainerTrainingSession(trainerTrainingSession);
                    break;
                case DELETE:
                    getTrainerTrainingSessionService().deleteTrainerTrainingSession(trainerTrainingSession);
                    break;
            }
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping("/report")
    public ResponseEntity<?> getReport(@RequestBody String username) {
        return ResponseEntity.ok(getTrainerTrainingSessionService().formReport(username));
    }
}
