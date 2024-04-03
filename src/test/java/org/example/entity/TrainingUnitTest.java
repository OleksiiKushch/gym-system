package org.example.entity;

import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class TrainingUnitTest {

    private static final String TEST_TRAINEE_ID = "1";
    private static final String TEST_TRAINER_ID = "1";
    private static final String TEST_TRAINING_NAME = "Training1";
    private static final String TEST_TRAINING_TYPE = "Type1";
    private static final String TEST_TRAINING_DATA = "2021-01-01";
    private static final String TEST_TRAINING_DURATION = "1";

    @Spy
    Training testInstance;

    @Mock
    CSVRecord csvRecord;

    @Test
    void shouldInitByCsvRecord() {
        when(csvRecord.get(0)).thenReturn(TEST_TRAINEE_ID);
        when(csvRecord.get(1)).thenReturn(TEST_TRAINER_ID);
        when(csvRecord.get(2)).thenReturn(TEST_TRAINING_NAME);
        when(csvRecord.get(3)).thenReturn(TEST_TRAINING_TYPE);
        when(csvRecord.get(4)).thenReturn(TEST_TRAINING_DATA);
        when(csvRecord.get(5)).thenReturn(TEST_TRAINING_DURATION);

        testInstance.initByCsvRecord(csvRecord);

        assertEquals(TEST_TRAINEE_ID, testInstance.getTraineeId().toString());
        assertEquals(TEST_TRAINER_ID, testInstance.getTrainerId().toString());
        assertEquals(TEST_TRAINING_NAME, testInstance.getTrainingName());
        assertEquals(TEST_TRAINING_TYPE, testInstance.getTrainingType().getName());
        assertEquals(TEST_TRAINING_DATA, testInstance.getTrainingDate().toString());
        assertEquals(TEST_TRAINING_DURATION, testInstance.getTrainingDuration().toString());
    }
}