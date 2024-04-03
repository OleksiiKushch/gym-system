package org.example.controller;

import org.example.dto.TraineeDto;
import org.example.dto.form.RegistrationTraineeForm;
import org.example.dto.form.UpdateTraineeForm;
import org.example.dto.form.search.SearchTraineeTrainingsPayload;
import org.example.dto.response.AfterRegistrationResponse;
import org.example.dto.response.SimpleTrainerResponse;
import org.example.dto.response.TraineeProfileResponse;
import org.example.dto.response.TraineeTrainingResponse;
import org.example.facade.TraineeFacade;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class TraineeControllerUnitTest {

    private static final String TEST_TRAINEE_USERNAME = "John.Doe";
    private static final String TEST_AUTHORIZATION_TOKEN = "authorization_token";

    @InjectMocks
    TraineeController testInstance;

    @Mock
    TraineeFacade traineeFacade;
    @Mock
    ModelMapper modelMapper;

    @Mock
    SearchTraineeTrainingsPayload searchTrainingsForm;
    @Mock
    UpdateTraineeForm updateTraineeForm;
    @Mock
    RegistrationTraineeForm registrationTraineeForm;
    @Mock
    BindingResult result;

    @Mock
    TraineeDto traineeDto;
    @Mock
    TraineeProfileResponse traineeProfileResponse;
    @Mock
    AfterRegistrationResponse afterRegistrationResponse;
    @Mock
    List<String> usernamesOfTrainers;
    @Mock
    List<SimpleTrainerResponse> trainerDtoList;
    @Mock
    List<TraineeTrainingResponse> trainingDtoList;
    @Mock
    List<FieldError> errors;

    @Test
    void shouldRegisterTrainee_whenNoErrors() {
        doReturn(false).when(result).hasErrors();
        doReturn(traineeDto).when(modelMapper).map(registrationTraineeForm, TraineeDto.class);
        doReturn(afterRegistrationResponse).when(traineeFacade).registerTrainee(traineeDto);

        var actualResult = testInstance.registerTrainee(registrationTraineeForm, result);

        assertNotNull(actualResult);
        assertEquals(HttpStatus.CREATED, actualResult.getStatusCode());
        assertEquals(afterRegistrationResponse, actualResult.getBody());
    }

    @Test
    void shouldDoesntRegisterTrainee_whenSomeErrors() {
        doReturn(true).when(result).hasErrors();
        doReturn(errors).when(result).getFieldErrors();

        var actualResult = testInstance.registerTrainee(registrationTraineeForm, result);

        assertNotNull(actualResult);
        assertEquals(HttpStatus.BAD_REQUEST, actualResult.getStatusCode());
        assertEquals(errors, actualResult.getBody());
    }

    @Test
    void shouldUpdateTrainee_whenNoErrors() {
        doReturn(false).when(result).hasErrors();
        doReturn(traineeDto).when(modelMapper).map(updateTraineeForm, TraineeDto.class);
        doReturn(traineeProfileResponse).when(traineeFacade).updateTrainee(TEST_TRAINEE_USERNAME, traineeDto);

        var actualResult = testInstance.updateTrainee(TEST_AUTHORIZATION_TOKEN, TEST_TRAINEE_USERNAME, updateTraineeForm, result);

        assertNotNull(actualResult);
        assertEquals(HttpStatus.OK, actualResult.getStatusCode());
        assertEquals(traineeProfileResponse, actualResult.getBody());
    }

    @Test
    void shouldDoesntUpdateTrainee_whenSomeErrors() {
        doReturn(true).when(result).hasErrors();
        doReturn(errors).when(result).getFieldErrors();

        var actualResult = testInstance.updateTrainee(TEST_AUTHORIZATION_TOKEN, TEST_TRAINEE_USERNAME, updateTraineeForm, result);

        assertNotNull(actualResult);
        assertEquals(HttpStatus.BAD_REQUEST, actualResult.getStatusCode());
        assertEquals(errors, actualResult.getBody());
    }

    @Test
    void shouldUpdateTraineeTrainers() {
        doReturn(trainerDtoList).when(traineeFacade).updateTraineeTrainers(TEST_TRAINEE_USERNAME, usernamesOfTrainers);

        var actualResult = testInstance.updateTraineeTrainers(TEST_AUTHORIZATION_TOKEN, TEST_TRAINEE_USERNAME, usernamesOfTrainers);

        assertNotNull(actualResult);
        assertEquals(HttpStatus.OK, actualResult.getStatusCode());
        assertEquals(trainerDtoList, actualResult.getBody());
    }

    @Test
    void shouldGetTraineeProfile() {
        doReturn(traineeProfileResponse).when(traineeFacade).getTraineeProfile(TEST_TRAINEE_USERNAME);

        var actualResult = testInstance.getTraineeProfile(TEST_AUTHORIZATION_TOKEN, TEST_TRAINEE_USERNAME);

        assertNotNull(actualResult);
        assertEquals(HttpStatus.OK, actualResult.getStatusCode());
        assertEquals(traineeProfileResponse, actualResult.getBody());
    }

    @Test
    void shouldGetTraineeTrainings_whenNoErrors() {
        doReturn(false).when(result).hasErrors();
        doReturn(trainingDtoList).when(traineeFacade).getTraineeTrainings(TEST_TRAINEE_USERNAME, searchTrainingsForm);

        var actualResult = testInstance.getTraineeTrainings(TEST_AUTHORIZATION_TOKEN, TEST_TRAINEE_USERNAME, searchTrainingsForm, result);

        assertNotNull(actualResult);
        assertEquals(HttpStatus.OK, actualResult.getStatusCode());
        assertEquals(trainingDtoList, actualResult.getBody());
    }

    @Test
    void shouldDoesntGetTraineeTrainings_whenSomeErrors() {
        doReturn(true).when(result).hasErrors();
        doReturn(errors).when(result).getFieldErrors();

        var actualResult = testInstance.getTraineeTrainings(TEST_AUTHORIZATION_TOKEN, TEST_TRAINEE_USERNAME, searchTrainingsForm, result);

        assertNotNull(actualResult);
        assertEquals(HttpStatus.BAD_REQUEST, actualResult.getStatusCode());
        assertEquals(errors, actualResult.getBody());
    }

    @Test
    void shouldDeleteTrainee() {
        doNothing().when(traineeFacade).deleteTrainee(TEST_TRAINEE_USERNAME);

        var actualResult = testInstance.deleteTrainee(TEST_AUTHORIZATION_TOKEN, TEST_TRAINEE_USERNAME);

        assertNotNull(actualResult);
        assertEquals(HttpStatus.NO_CONTENT, actualResult.getStatusCode());
    }

}