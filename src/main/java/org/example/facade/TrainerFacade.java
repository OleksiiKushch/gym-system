package org.example.facade;

import org.example.dto.TrainerDto;
import org.example.dto.form.search.SearchTrainerTrainingsPayload;
import org.example.dto.response.AfterRegistrationResponse;
import org.example.dto.response.SimpleTrainerResponse;
import org.example.dto.response.TrainerProfileResponse;
import org.example.dto.response.TrainerTrainingResponse;

import java.util.List;

public interface TrainerFacade extends UserFacade {

    AfterRegistrationResponse registerTrainer(TrainerDto trainerDto);
    TrainerProfileResponse getTrainerProfile(String username);
    List<SimpleTrainerResponse> getTrainersThatNotAssignedOnTrainee(String username);
    List<TrainerTrainingResponse> getTrainerTrainings(String username, SearchTrainerTrainingsPayload payload);
    TrainerProfileResponse updateTrainer(String username, TrainerDto trainerDto);
}
