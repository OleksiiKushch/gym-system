package org.example.dao.impl.inmemory;

import lombok.extern.slf4j.Slf4j;
import org.example.config.storage.inmemory.InMemoryStorageWithIntId;
import org.example.dao.TrainerDao;
import org.example.entity.Trainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Component
public class InMemoryTrainerDao extends InMemoryAbstractUserDao<Trainer> implements TrainerDao {

    private InMemoryStorageWithIntId<Trainer> trainerStorage;

    @Override
    protected InMemoryStorageWithIntId<Trainer> getUserStorage() {
        return trainerStorage;
    }

    @Autowired
    public void setTrainerStorage(InMemoryStorageWithIntId<Trainer> trainerStorage) {
        this.trainerStorage = trainerStorage;
    }

    @Override
    public Optional<Trainer> findWithTrainingsByUsername(String username) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<Trainer> findAllThatNotAssignedOnTrainee(String traineeUsername) {
        throw new UnsupportedOperationException();
    }
}
