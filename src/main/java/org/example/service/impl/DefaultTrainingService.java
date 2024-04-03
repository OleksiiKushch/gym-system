package org.example.service.impl;

import lombok.Getter;
import org.example.dao.TraineeDao;
import org.example.dao.TrainingDao;
import org.example.entity.Trainee;
import org.example.entity.Trainer;
import org.example.entity.Training;
import org.example.entity.search.TraineeTrainingsCriteria;
import org.example.entity.search.TrainerTrainingsCriteria;
import org.example.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Getter
@Service
public class DefaultTrainingService implements TrainingService {

    @Autowired
    @Qualifier("hibernateTrainingDao")
    private TrainingDao trainingDao;

    @Autowired
    @Qualifier("hibernateTraineeDao")
    private TraineeDao traineeDao;

    @Override
    public void createTraining(Training training) {
        Trainee trainee = training.getTrainee();
        Trainer trainer = training.getTrainer();

        if(isTrainerNotAssignedToTrainee(trainee, trainer)) {
            trainee.addTrainer(trainer);
            getTraineeDao().update(trainee);
        }

        getTrainingDao().insert(training);
    }

    private boolean isTrainerNotAssignedToTrainee(Trainee trainee, Trainer trainer) {
        return trainee.getTrainers().stream()
                .noneMatch(actualTrainer -> actualTrainer.getUserId().equals(trainer.getUserId()));
    }

    @Override
    public Optional<Training> getTrainingForName(String name) {
        return getTrainingDao().findByName(name);
    }

    @Override
    public List<Training> getTraineeTrainings(TraineeTrainingsCriteria criteria) {
        return getTrainingDao().findTraineeTrainingsByCriteria(criteria).stream().toList();
    }

    @Override
    public List<Training> getTrainerTrainings(TrainerTrainingsCriteria criteria) {
        return getTrainingDao().findTrainerTrainingsByCriteria(criteria).stream().toList();
    }
}
