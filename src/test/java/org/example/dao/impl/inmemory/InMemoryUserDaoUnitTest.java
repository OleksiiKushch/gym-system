package org.example.dao.impl.inmemory;

import org.example.dao.TraineeDao;
import org.example.dao.TrainerDao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class InMemoryUserDaoUnitTest {

    @InjectMocks
    InMemoryUserDao testInstance;

    @Mock
    TraineeDao traineeDao;
    @Mock
    TrainerDao trainerDao;

    @Test
    void shouldGetAllUsers() {
        testInstance.findAll();

        verify(traineeDao).findAll();
        verify(trainerDao).findAll();
    }
}