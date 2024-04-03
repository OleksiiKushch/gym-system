package org.example.service.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.IterableUtils;
import org.example.dao.TraineeDao;
import org.example.dao.TrainingDao;
import org.example.dao.TrainingSearchDao;
import org.example.entity.Trainee;
import org.example.entity.Trainer;
import org.example.entity.Training;
import org.example.entity.search.TraineeTrainingsCriteria;
import org.example.entity.search.TrainerTrainingsCriteria;
import org.example.service.TrainingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Getter
@RequiredArgsConstructor
@Service
public class DefaultTrainingService implements TrainingService {

    private final TrainingDao trainingDao;
    private final TrainingSearchDao trainingSearchDao;
    private final TraineeDao traineeDao;

    @Override
    @Transactional
    public void createTraining(Training training) {
        Trainee trainee = training.getTrainee();
        Trainer trainer = training.getTrainer();

        if(isTrainerNotAssignedToTrainee(trainee, trainer)) {
            trainee.addTrainer(trainer);
            getTraineeDao().save(trainee);
        }

        getTrainingDao().save(training);
    }

    private boolean isTrainerNotAssignedToTrainee(Trainee trainee, Trainer trainer) {
        return trainee.getTrainers().stream()
                .noneMatch(actualTrainer -> actualTrainer.getUserId().equals(trainer.getUserId()));
    }

    @Override
    public Optional<Training> getTrainingForName(String name) {
        return getTrainingDao().findByTrainingName(name);
    }

    @Override
    public List<Training> getTraineeTrainings(TraineeTrainingsCriteria criteria) {
        return IterableUtils.toList(getTrainingSearchDao().findTraineeTrainingsByCriteria(criteria));
    }

    @Override
    public List<Training> getTrainerTrainings(TrainerTrainingsCriteria criteria) {
        return IterableUtils.toList(getTrainingSearchDao().findTrainerTrainingsByCriteria(criteria));
    }
}
