package org.example.facade;

import org.example.dto.TraineeDto;
import org.example.dto.form.search.SearchTraineeTrainingsPayload;
import org.example.dto.response.AfterRegistrationResponse;
import org.example.dto.response.SimpleTrainerResponse;
import org.example.dto.response.TraineeProfileResponse;
import org.example.dto.response.TraineeTrainingResponse;

import java.util.List;

public interface TraineeFacade extends UserFacade {

    AfterRegistrationResponse registerTrainee(TraineeDto traineeDto);
    TraineeProfileResponse getTraineeProfile(String username);
    List<TraineeTrainingResponse> getTraineeTrainings(String username, SearchTraineeTrainingsPayload payload);
    TraineeProfileResponse updateTrainee(String username, TraineeDto traineeDto);
    List<SimpleTrainerResponse> updateTraineeTrainers(String username, List<String> usernamesOfTrainers);
    void deleteTrainee(String username);
}
