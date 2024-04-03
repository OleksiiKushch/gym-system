package org.example.service.impl;

import lombok.Getter;
import org.example.dao.TraineeDao;
import org.example.entity.Trainee;
import org.example.entity.Trainer;
import org.example.service.TraineeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Getter
@Service
public class DefaultTraineeService implements TraineeService {

    @Autowired
    @Qualifier("hibernateTraineeDao")
    private TraineeDao traineeDao;

    @Override
    public void createTrainee(Trainee trainee) {
        getTraineeDao().insert(trainee);
    }

    @Override
    public void updateTrainee(Trainee trainee) {
        getTraineeDao().update(trainee);
    }

    @Override
    public void deleteTrainee(Trainee trainee) {
        getTraineeDao().remove(trainee.getUserId());
    }

    @Override
    public void deleteTraineeForUsername(String username) {
        getTraineeDao().removeByUsername(username);
    }

    @Override
    public Optional<Trainee> getTraineeForId(Integer id) {
        return getTraineeDao().findById(id);
    }

    @Override
    public Optional<Trainee> getTraineeForUsername(String username) {
        return getTraineeDao().findByUsername(username);
    }

    @Override
    public Optional<Trainee> getFullTraineeForUsername(String username) {
        return getTraineeDao().findWithTrainingsByUsername(username);
    }

    @Override
    public void updateTrainersList(String username, List<Trainer> newTrainers) {
        getTraineeDao().updateTrainersList(username, newTrainers);
    }
}
