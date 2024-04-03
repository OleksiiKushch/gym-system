package org.example.facade;

import org.example.dto.TraineeDto;
import org.example.dto.TrainerDto;

public interface UserFacade {

    void registerTrainee(TraineeDto traineeDto);
    void registerTrainer(TrainerDto trainerDto);
}
