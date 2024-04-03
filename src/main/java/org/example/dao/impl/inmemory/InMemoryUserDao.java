package org.example.dao.impl.inmemory;

import lombok.Getter;
import org.example.dao.TraineeDao;
import org.example.dao.TrainerDao;
import org.example.dao.UserDao;
import org.example.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Component
public class InMemoryUserDao implements UserDao {

    private TraineeDao traineeDao;
    private TrainerDao trainerDao;

    @Override
    public Optional<User> findByUsername(String username) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<User> findAll() {
        return Stream.of(getTraineeDao().findAll(), getTrainerDao().findAll())
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    @Override
    public void update(User user) {
        throw new UnsupportedOperationException();
    }

    @Autowired
    public void setTraineeDao(@Qualifier("inMemoryTraineeDao") TraineeDao traineeDao) {
        this.traineeDao = traineeDao;
    }

    @Autowired
    public void setTrainerDao(@Qualifier("inMemoryTrainerDao") TrainerDao trainerDao) {
        this.trainerDao = trainerDao;
    }
}
