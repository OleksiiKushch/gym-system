package org.example.facade;

import org.example.dto.TrainingDto;

public interface TrainingFacade {

    void createTraining(TrainingDto trainingDto);
    void deleteTraining(Integer trainingId);
}
