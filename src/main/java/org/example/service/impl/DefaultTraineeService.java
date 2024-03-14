package org.example.service.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.dao.TraineeDao;
import org.example.entity.Trainee;
import org.example.entity.Trainer;
import org.example.exception.NotFoundException;
import org.example.service.TraineeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.example.constants.GeneralConstants.TRAINEE_NOT_FOUND_EXCEPTION_MSG;

@Getter
@RequiredArgsConstructor
@Service
public class DefaultTraineeService implements TraineeService {

    private final TraineeDao traineeDao;

    @Override
    public void createTrainee(Trainee trainee) {
        getTraineeDao().save(trainee);
    }

    @Override
    public Trainee updateTrainee(Trainee trainee) {
        return getTraineeDao().save(trainee);
    }

    @Override
    public void deleteTrainee(Trainee trainee) {
        getTraineeDao().delete(trainee);
    }

    @Override
    public Optional<Trainee> getTraineeForUsername(String username) {
        return getTraineeDao().findByUsername(username);
    }

    @Override
    @Transactional
    public void updateTrainersList(String username, List<Trainer> newTrainers) {
        getTraineeDao().findByUsername(username)
                        .ifPresentOrElse(trainee -> {
                            trainee.getTrainers().clear();
                            trainee.getTrainers().addAll(newTrainers);
                        }, () -> {
                            throw new NotFoundException(formExceptionMessage(username));
                        });
    }

    private String formExceptionMessage(Object... args) {
        return String.format(TRAINEE_NOT_FOUND_EXCEPTION_MSG, args);
    }
}
