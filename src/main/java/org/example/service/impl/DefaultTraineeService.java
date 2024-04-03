package org.example.service.impl;

import lombok.Getter;
import org.example.dao.TraineeDao;
import org.example.entity.Trainee;
import org.example.service.TraineeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Getter
@Service
public class DefaultTraineeService implements TraineeService {

    @Autowired
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
    public Optional<Trainee> getTraineeForId(Integer id) {
        return getTraineeDao().findById(id);
    }
}
