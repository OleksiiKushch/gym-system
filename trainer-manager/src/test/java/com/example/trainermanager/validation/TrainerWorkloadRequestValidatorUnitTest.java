package com.example.trainermanager.validation;

import com.example.trainermanager.dto.request.TrainerWorkloadRequest;
import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.Errors;
import org.springframework.validation.SimpleErrors;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class TrainerWorkloadRequestValidatorUnitTest {

    private static final String TEST_TRAINER_USERNAME = "John.Doe";
    private static final String TEST_TRAINER_FIRST_NAME = "John";
    private static final String TEST_TRAINER_LAST_NAME = "Doe";
    private static final String ACTUAL_ACTION = "Add";
    private static final int ACTUAL_TRAINING_DURATION = 60;

    private static final String TRAINER_USERNAME_FIELD_NAME = "trainerUsername";
    private static final String ACTION_TYPE_FIELD_NAME = "actionType";

    @InjectMocks
    TrainerWorkloadRequestValidator validator;

    Errors errors;
    TrainerWorkloadRequest request;

    @BeforeEach
    void setUp() {
        request = TrainerWorkloadRequest.builder()
                .trainerUsername(TEST_TRAINER_USERNAME)
                .trainerFirstName(TEST_TRAINER_FIRST_NAME)
                .trainerLastName(TEST_TRAINER_LAST_NAME)
                .isActive(true)
                .trainingDate(LocalDate.now())
                .trainingDuration(ACTUAL_TRAINING_DURATION)
                .actionType(ACTUAL_ACTION)
                .build();
        errors = new SimpleErrors(request);
    }

    @Test
    void validate() {
        validator.validate(request, errors);

        assertTrue(errors.getFieldErrors().isEmpty());
    }

    @Test
    void validate_whenUsernameIsEmpty() {
        request.setTrainerUsername(StringUtils.EMPTY);

        validator.validate(request, errors);

        assertEquals(1, errors.getFieldErrors().size());
        assertEquals(TRAINER_USERNAME_FIELD_NAME, errors.getFieldErrors().get(0).getField());
    }

    @Test
    void validate_whenUsernameAndActionTypeIsEmpty() {
        request.setTrainerUsername(StringUtils.EMPTY);
        request.setActionType(StringUtils.EMPTY);

        validator.validate(request, errors);

        assertEquals(2, errors.getFieldErrors().size());
        assertEquals(TRAINER_USERNAME_FIELD_NAME, errors.getFieldErrors().get(0).getField());
        assertEquals(ACTION_TYPE_FIELD_NAME, errors.getFieldErrors().get(1).getField());
    }
}