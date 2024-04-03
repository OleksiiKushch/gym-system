package org.example.dao.impl.inmemory;

import org.example.config.storage.inmemory.InMemoryStorage;
import org.example.entity.Training;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InMemoryTrainingDaoUnitTest {

    private static final String NAME = "Test Training";
    private static final String TRAINING_ALREADY_EXISTS_EXCEPTION_MSG = "Training with name \"" + NAME + "\" already exists";

    @InjectMocks
    InMemoryTrainingDao testInstance;

    @Mock
    InMemoryStorage<String, Training> trainingStorage;

    @Spy
    Training training;

    @Test
    void shouldInsert_whenTrainingWithFollowingNameDoseNotExists() {
        when(training.getTrainingName()).thenReturn(NAME);
        when(trainingStorage.containsKey(NAME)).thenReturn(false);

        testInstance.insert(training);

        verify(trainingStorage).put(NAME, training);
    }

    @Test
    void shouldThrowException_whenInsertAndTrainingWithFollowingNameAlreadyExists() {
        when(training.getTrainingName()).thenReturn(NAME);
        when(trainingStorage.containsKey(NAME)).thenReturn(true);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> testInstance.insert(training)
        );

        assertEquals(TRAINING_ALREADY_EXISTS_EXCEPTION_MSG, exception.getMessage());
    }

    @Test
    void findByName() {
        when(trainingStorage.get(NAME)).thenReturn(training);

        Optional<Training> actualResult = testInstance.findByName(NAME);

        assertTrue(actualResult.isPresent());
        assertEquals(training, actualResult.get());
    }
}