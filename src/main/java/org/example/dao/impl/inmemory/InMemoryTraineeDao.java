package org.example.dao.impl.inmemory;

import lombok.extern.slf4j.Slf4j;
import org.example.config.storage.inmemory.InMemoryStorageWithIntId;
import org.example.dao.TraineeDao;
import org.example.entity.Trainee;
import org.example.entity.Trainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Component
public class InMemoryTraineeDao extends InMemoryAbstractUserDao<Trainee> implements TraineeDao {

    private InMemoryStorageWithIntId<Trainee> traineeStorage;

    @Override
    public void remove(Integer id) {
        getUserStorage().remove(id);
    }

    @Override
    public void removeByUsername(String username) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<Trainee> findWithTrainingsByUsername(String username) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void updateTrainersList(Integer id, Collection<Trainer> newTrainers) {
        throw new UnsupportedOperationException();
    }

    @Override
    public InMemoryStorageWithIntId<Trainee> getUserStorage() {
        return traineeStorage;
    }

    @Autowired
    public void setTraineeStorage(InMemoryStorageWithIntId<Trainee> traineeStorage) {
        this.traineeStorage = traineeStorage;
    }
}
