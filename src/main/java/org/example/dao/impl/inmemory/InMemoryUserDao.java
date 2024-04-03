package org.example.dao.impl.inmemory;

import lombok.Getter;
import org.example.dao.TraineeDao;
import org.example.dao.TrainerDao;
import org.example.dao.UserDao;
import org.example.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Component
public class InMemoryUserDao implements UserDao {

    private TraineeDao traineeDao;
    private TrainerDao trainerDao;

    @Override
    public Collection<User> findAll() {
        return Stream.of(getTraineeDao().findAll(), getTrainerDao().findAll())
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    @Autowired
    public void setTraineeDao(TraineeDao traineeDao) {
        this.traineeDao = traineeDao;
    }

    @Autowired
    public void setTrainerDao(TrainerDao trainerDao) {
        this.trainerDao = trainerDao;
    }
}
