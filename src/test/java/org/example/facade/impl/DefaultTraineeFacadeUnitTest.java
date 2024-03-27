package org.example.facade.impl;

import org.apache.commons.lang3.StringUtils;
import org.example.dto.TraineeDto;
import org.example.dto.form.search.SearchTraineeTrainingsPayload;
import org.example.dto.response.SimpleTrainerResponse;
import org.example.dto.response.TraineeProfileResponse;
import org.example.dto.response.TraineeTrainingResponse;
import org.example.entity.Trainee;
import org.example.entity.Trainer;
import org.example.entity.Training;
import org.example.entity.search.TraineeTrainingsCriteria;
import org.example.exception.AppException;
import org.example.exception.NotFoundException;
import org.example.service.TraineeService;
import org.example.service.TrainerService;
import org.example.service.TrainingService;
import org.example.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultTraineeFacadeUnitTest {

    private static final String TEST_USERNAME = "John.Doe";
    private static final String TEST_FIRSTNAME = "John";
    private static final String TEST_LASTNAME = "Doe";
    private static final LocalDate TEST_DATE_OF_BURTHDAY = LocalDate.of(1990, 1, 1);
    private static final String TEST_ADDRESS = "Some address";
    private static final String TEST_PASSWORD = "password";
    private static final String ENCODED_PASSWORD = "encodedPassword";
    private static final String GENERATED_PASSWORD = "generated_password";
    private static final String TEST_TRAINER_USERNAME1 = "TrainerUsername1";
    private static final String TEST_TRAINER_USERNAME2 = "TrainerUsername2";

    private static final String TRAINEE_NOT_FOUND_EXPECTED_EXCEPTION_MSG = "Trainee with username '" + TEST_USERNAME + "' not found";
    private static final String TRAINEE_IS_ACTIVE_EXPECTED_EXCEPTION_MSG = "Active trainee (username: " + TEST_USERNAME + ") cannot be deleted";
    private static final String TRAINER_NOT_FOUND_EXPECTED_EXCEPTION_MSG = "Trainer with username '" + TEST_TRAINER_USERNAME2 + "' not found";

    @InjectMocks
    DefaultTraineeFacade testInstance;

    @Mock
    UserService userService;
    @Mock
    TraineeService traineeService;
    @Mock
    TrainerService trainerService;
    @Mock
    TrainingService trainingService;
    @Mock
    ModelMapper modelMapper;
    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    Trainee trainee;
    @Mock
    Trainee newTrainee;
    @Mock
    Trainee updatedTrainee;
    @Mock
    TraineeDto traineeDto;
    @Mock
    TraineeProfileResponse traineeProfileResponse;
    @Mock
    SearchTraineeTrainingsPayload payload;
    @Mock
    TraineeTrainingsCriteria traineeTrainingsCriteria;
    @Mock
    SimpleTrainerResponse simpleTrainerResponse1;
    @Mock
    SimpleTrainerResponse simpleTrainerResponse2;

    @Mock
    Training training1;
    @Mock
    TraineeTrainingResponse traineeTrainingResponse1;
    @Mock
    Training training2;
    @Mock
    TraineeTrainingResponse traineeTrainingResponse2;
    @Mock
    Trainer trainer1;
    @Mock
    Trainer trainer2;

    @Captor
    ArgumentCaptor<List<Trainer>> trainersCaptor;

    List<Training> trainings;

    @BeforeEach
    void setUp() {
        testInstance.setUserService(userService);
        testInstance.setPasswordEncoder(passwordEncoder);
        trainings = List.of(training1, training2);
    }

    @Test
    void shouldRegisterTrainee() {
        setUpTraineeDtoForRegistration();
        when(trainee.getPassword()).thenReturn(TEST_PASSWORD);
        when(passwordEncoder.encode(anyString())).thenReturn(ENCODED_PASSWORD);

        testInstance.registerTrainee(traineeDto);

        verifyRegistrationTrainee();
        verify(trainee).setActive(true);
        verify(userService, never()).generateRandomPassword();
        verify(trainee).setPassword(ENCODED_PASSWORD);
    }

    @Test
    void shouldRegisterTrainee_whenPasswordAutoGeneratedBecauseItsNull() {
        setUpTraineeDtoForRegistration();
        when(trainee.getPassword()).thenReturn(null);
        when(userService.generateRandomPassword()).thenReturn(GENERATED_PASSWORD);
        when(passwordEncoder.encode(GENERATED_PASSWORD)).thenReturn(ENCODED_PASSWORD);

        testInstance.registerTrainee(traineeDto);

        verifyRegistrationTrainee();
        verify(trainee).setActive(true);
        verify(userService).generateRandomPassword();
        verify(trainee).setPassword(ENCODED_PASSWORD);
    }

    @Test
    void shouldRegisterTrainee_whenPasswordAutoGeneratedBecauseItsEmpty() {
        setUpTraineeDtoForRegistration();
        when(trainee.getPassword()).thenReturn(StringUtils.EMPTY);
        when(userService.generateRandomPassword()).thenReturn(GENERATED_PASSWORD);
        when(passwordEncoder.encode(GENERATED_PASSWORD)).thenReturn(ENCODED_PASSWORD);

        testInstance.registerTrainee(traineeDto);

        verifyRegistrationTrainee();
        verify(trainee).setActive(true);
        verify(userService).generateRandomPassword();
        verify(trainee).setPassword(ENCODED_PASSWORD);
    }

    @Test
    void shouldGetTraineeProfile() {
        when(traineeService.getTraineeForUsername(TEST_USERNAME)).thenReturn(Optional.of(trainee));
        when(modelMapper.map(trainee, TraineeProfileResponse.class)).thenReturn(traineeProfileResponse);

        var actualResult = testInstance.getTraineeProfile(TEST_USERNAME);

        assertEquals(traineeProfileResponse, actualResult);
    }

    @Test
    void getTraineeProfile_shouldThrowException_whenTraineeNotExists() {
        when(traineeService.getTraineeForUsername(TEST_USERNAME)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NotFoundException.class, () ->
                testInstance.getTraineeProfile(TEST_USERNAME));

        assertEquals(TRAINEE_NOT_FOUND_EXPECTED_EXCEPTION_MSG, exception.getMessage());
    }

    @Test
    void shouldGetTraineeTrainings() {
        when(traineeService.getTraineeForUsername(TEST_USERNAME)).thenReturn(Optional.of(trainee));
        when(modelMapper.map(payload, TraineeTrainingsCriteria.class)).thenReturn(traineeTrainingsCriteria);
        when(trainingService.getTraineeTrainings(traineeTrainingsCriteria)).thenReturn(trainings);
        when(modelMapper.map(training1, TraineeTrainingResponse.class)).thenReturn(traineeTrainingResponse1);
        when(modelMapper.map(training2, TraineeTrainingResponse.class)).thenReturn(traineeTrainingResponse2);

        List<TraineeTrainingResponse> actualResult = testInstance.getTraineeTrainings(TEST_USERNAME, payload);

        verify(traineeTrainingsCriteria).setTraineeUsername(TEST_USERNAME);
        assertEquals(trainings.size(), actualResult.size());
        assertTrue(actualResult.contains(traineeTrainingResponse1));
        assertTrue(actualResult.contains(traineeTrainingResponse2));
    }

    @Test
    void getTraineeTrainings_shouldThrowException_whenTraineeNotExists() {
        doReturn(Optional.empty()).when(traineeService).getTraineeForUsername(TEST_USERNAME);

        Exception exception = assertThrows(NotFoundException.class, () ->
                testInstance.getTraineeTrainings(TEST_USERNAME, payload));

        assertEquals(TRAINEE_NOT_FOUND_EXPECTED_EXCEPTION_MSG, exception.getMessage());
    }

    @Test
    void shouldUpdateTrainee() {
        when(traineeService.getTraineeForUsername(TEST_USERNAME)).thenReturn(Optional.of(trainee));
        when(modelMapper.map(traineeDto, Trainee.class)).thenReturn(newTrainee);
        prepareNewTrainee();
        when(traineeService.updateTrainee(trainee)).thenReturn(updatedTrainee);
        when(modelMapper.map(updatedTrainee, TraineeProfileResponse.class)).thenReturn(traineeProfileResponse);

        TraineeProfileResponse actualResult = testInstance.updateTrainee(TEST_USERNAME, traineeDto);

        verifyUpdatedTraineeFields();
        assertEquals(traineeProfileResponse, actualResult);
    }

    @Test
    void updateTrainee_shouldThrowException_whenTraineeNotExists() {
        doReturn(Optional.empty()).when(traineeService).getTraineeForUsername(TEST_USERNAME);

        Exception exception = assertThrows(NotFoundException.class, () ->
                testInstance.updateTrainee(TEST_USERNAME, traineeDto));

        assertEquals(TRAINEE_NOT_FOUND_EXPECTED_EXCEPTION_MSG, exception.getMessage());
    }

    @Test
    void shouldUpdateTraineeTrainers() {
        when(trainerService.getTrainerForUsername(TEST_TRAINER_USERNAME1)).thenReturn(Optional.of(trainer1));
        when(trainerService.getTrainerForUsername(TEST_TRAINER_USERNAME2)).thenReturn(Optional.of(trainer2));
        when(modelMapper.map(trainer1, SimpleTrainerResponse.class)).thenReturn(simpleTrainerResponse1);
        when(modelMapper.map(trainer2, SimpleTrainerResponse.class)).thenReturn(simpleTrainerResponse2);

        List<SimpleTrainerResponse> actualResult = testInstance.updateTraineeTrainers(TEST_USERNAME, List.of(TEST_TRAINER_USERNAME1, TEST_TRAINER_USERNAME2));

        verify(traineeService).updateTrainersList(eq(TEST_USERNAME), trainersCaptor.capture());
        assertTrue(trainersCaptor.getValue().contains(trainer1));
        assertTrue(trainersCaptor.getValue().contains(trainer2));
        assertTrue(actualResult.contains(simpleTrainerResponse1));
        assertTrue(actualResult.contains(simpleTrainerResponse2));
    }

    @Test
    void updateTraineeTrainers_shouldThrowException_whenAnyOfTrainersNotFound() {
        when(trainerService.getTrainerForUsername(TEST_TRAINER_USERNAME1)).thenReturn(Optional.of(trainer1));
        when(trainerService.getTrainerForUsername(TEST_TRAINER_USERNAME2)).thenReturn(Optional.empty());

        Exception exception = assertThrows(AppException.class, () ->
                testInstance.updateTraineeTrainers(TEST_USERNAME, List.of(TEST_TRAINER_USERNAME1, TEST_TRAINER_USERNAME2)));

        assertEquals(TRAINER_NOT_FOUND_EXPECTED_EXCEPTION_MSG, exception.getMessage());
    }

    @Test
    void shouldDeleteTrainee() {
        doReturn(Optional.of(trainee)).when(traineeService).getTraineeForUsername(TEST_USERNAME);
        doReturn(false).when(trainee).isActive();

        testInstance.deleteTrainee(TEST_USERNAME);

        verify(traineeService).deleteTrainee(trainee);
    }

    @Test
    void deleteTrainee_shouldThrowException_whenTraineeNotExists() {
        doReturn(Optional.empty()).when(traineeService).getTraineeForUsername(TEST_USERNAME);

        Exception exception = assertThrows(NotFoundException.class, () ->
                testInstance.deleteTrainee(TEST_USERNAME));

        assertEquals(TRAINEE_NOT_FOUND_EXPECTED_EXCEPTION_MSG, exception.getMessage());
    }

    @Test
    void deleteTrainee_shouldThrowException_whenTraineeIsActive() {
        doReturn(Optional.of(trainee)).when(traineeService).getTraineeForUsername(TEST_USERNAME);
        doReturn(true).when(trainee).isActive();

        Exception exception = assertThrows(AppException.class, () ->
                testInstance.deleteTrainee(TEST_USERNAME));

        assertEquals(TRAINEE_IS_ACTIVE_EXPECTED_EXCEPTION_MSG, exception.getMessage());
    }

    void setUpTraineeDtoForRegistration() {
        when(modelMapper.map(traineeDto, Trainee.class)).thenReturn(trainee);
        when(userService.calculateUsername(trainee)).thenReturn(TEST_USERNAME);
    }

    void verifyRegistrationTrainee() {
        verify(trainee).setUsername(TEST_USERNAME);
        verify(traineeService).createTrainee(trainee);
    }

    void prepareNewTrainee() {
        when(newTrainee.getFirstName()).thenReturn(TEST_FIRSTNAME);
        when(newTrainee.getLastName()).thenReturn(TEST_LASTNAME);
        when(newTrainee.getDateOfBirthday()).thenReturn(TEST_DATE_OF_BURTHDAY);
        when(newTrainee.getAddress()).thenReturn(TEST_ADDRESS);
        when(newTrainee.isActive()).thenReturn(true);
    }

    void verifyUpdatedTraineeFields() {
        verify(trainee).setFirstName(TEST_FIRSTNAME);
        verify(trainee).setLastName(TEST_LASTNAME);
        verify(trainee).setDateOfBirthday(TEST_DATE_OF_BURTHDAY);
        verify(trainee).setAddress(TEST_ADDRESS);
        verify(trainee).setIsActive(true);
    }
}