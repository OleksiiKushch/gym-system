package org.example.controller;

import org.example.dto.TrainerDto;
import org.example.dto.form.RegistrationTrainerForm;
import org.example.dto.form.UpdateTrainerForm;
import org.example.dto.form.search.SearchTrainerTrainingsPayload;
import org.example.dto.response.AfterRegistrationResponse;
import org.example.dto.response.SimpleTrainerResponse;
import org.example.dto.response.TrainerProfileResponse;
import org.example.dto.response.TrainerTrainingResponse;
import org.example.facade.TrainerFacade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class TrainerControllerUnitTest {

    private static final String TEST_TRAINER_USERNAME = "John.Doe";
    private static final String TEST_AUTHORIZATION_TOKEN = "authorization_token";

    @InjectMocks
    TrainerController testInstance;

    @Mock
    TrainerFacade trainerFacade;
    @Mock
    ModelMapper modelMapper;

    @Mock
    RegistrationTrainerForm registrationTrainerForm;
    @Mock
    UpdateTrainerForm updateTrainerForm;
    @Mock
    SearchTrainerTrainingsPayload searchTrainingsForm;
    @Mock
    BindingResult result;

    @Mock
    TrainerDto trainerDto;
    @Mock
    TrainerProfileResponse trainerProfileResponse;
    @Mock
    AfterRegistrationResponse afterRegistrationResponse;
    @Mock
    List<SimpleTrainerResponse> trainerDtoList;
    @Mock
    List<TrainerTrainingResponse> trainingDtoList;
    @Mock
    List<FieldError> errors;

    @Test
    void shouldRegisterTrainer_whenNoErrors() {
        doReturn(false).when(result).hasErrors();
        doReturn(trainerDto).when(modelMapper).map(registrationTrainerForm, TrainerDto.class);
        doReturn(afterRegistrationResponse).when(trainerFacade).registerTrainer(trainerDto);

        var actualResult = testInstance.registerTrainer(registrationTrainerForm, result);

        assertNotNull(actualResult);
        assertEquals(HttpStatus.CREATED, actualResult.getStatusCode());
        assertEquals(afterRegistrationResponse, actualResult.getBody());
    }

    @Test
    void shouldDoesntRegisterTrainer_whenSomeErrors() {
        doReturn(true).when(result).hasErrors();
        doReturn(errors).when(result).getFieldErrors();

        var actualResult = testInstance.registerTrainer(registrationTrainerForm, result);

        assertNotNull(actualResult);
        assertEquals(HttpStatus.BAD_REQUEST, actualResult.getStatusCode());
        assertEquals(errors, actualResult.getBody());
    }

    @Test
    void shouldUpdateTrainer_whenNoErrors() {
        doReturn(false).when(result).hasErrors();
        doReturn(trainerDto).when(modelMapper).map(updateTrainerForm, TrainerDto.class);
        doReturn(trainerProfileResponse).when(trainerFacade).updateTrainer(TEST_TRAINER_USERNAME, trainerDto);

        var actualResult = testInstance.updateTrainer(TEST_AUTHORIZATION_TOKEN, TEST_TRAINER_USERNAME, updateTrainerForm, result);

        assertNotNull(actualResult);
        assertEquals(HttpStatus.OK, actualResult.getStatusCode());
        assertEquals(trainerProfileResponse, actualResult.getBody());
    }

    @Test
    void shouldDoesntUpdateTrainer_whenSomeErrors() {
        doReturn(true).when(result).hasErrors();
        doReturn(errors).when(result).getFieldErrors();

        var actualResult = testInstance.updateTrainer(TEST_AUTHORIZATION_TOKEN, TEST_TRAINER_USERNAME, updateTrainerForm, result);

        assertNotNull(actualResult);
        assertEquals(HttpStatus.BAD_REQUEST, actualResult.getStatusCode());
        assertEquals(errors, actualResult.getBody());
    }

    @Test
    void shouldGetTrainerProfile() {
        doReturn(trainerProfileResponse).when(trainerFacade).getTrainerProfile(TEST_TRAINER_USERNAME);

        var actualResult = testInstance.getTrainerProfile(TEST_AUTHORIZATION_TOKEN, TEST_TRAINER_USERNAME);

        assertNotNull(actualResult);
        assertEquals(HttpStatus.OK, actualResult.getStatusCode());
        assertEquals(trainerProfileResponse, actualResult.getBody());
    }

    @Test
    void shouldGetTrainerTrainings_whenNoErrors() {
        doReturn(false).when(result).hasErrors();
        doReturn(trainingDtoList).when(trainerFacade).getTrainerTrainings(TEST_TRAINER_USERNAME, searchTrainingsForm);

        var actualResult = testInstance.getTrainerTrainings(TEST_AUTHORIZATION_TOKEN, TEST_TRAINER_USERNAME, searchTrainingsForm, result);

        assertNotNull(actualResult);
        assertEquals(HttpStatus.OK, actualResult.getStatusCode());
        assertEquals(trainingDtoList, actualResult.getBody());
    }

    @Test
    void shouldDoesntGetTraineeTrainings_whenSomeErrors() {
        doReturn(true).when(result).hasErrors();
        doReturn(errors).when(result).getFieldErrors();

        var actualResult = testInstance.getTrainerTrainings(TEST_AUTHORIZATION_TOKEN, TEST_TRAINER_USERNAME, searchTrainingsForm, result);

        assertNotNull(actualResult);
        assertEquals(HttpStatus.BAD_REQUEST, actualResult.getStatusCode());
        assertEquals(errors, actualResult.getBody());
    }

    @Test
    void shouldGetTrainersThatNotAssignedOnTrainee() {
        doReturn(trainingDtoList).when(trainerFacade).getTrainersThatNotAssignedOnTrainee(TEST_TRAINER_USERNAME);

        var actualResult = testInstance.getTrainersThatNotAssignedOnTrainee(TEST_AUTHORIZATION_TOKEN, TEST_TRAINER_USERNAME);

        assertNotNull(actualResult);
        assertEquals(HttpStatus.OK, actualResult.getStatusCode());
        assertEquals(trainingDtoList, actualResult.getBody());
    }

}