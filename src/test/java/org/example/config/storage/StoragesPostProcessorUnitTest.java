package org.example.config.storage;

import org.example.config.storage.inmemory.InMemoryStorage;
import org.example.config.storage.inmemory.InMemoryStorageWithIntId;
import org.example.config.storage.inmemory.StorageConfig;
import org.example.entity.Trainee;
import org.example.entity.Trainer;
import org.example.entity.Training;
import org.example.entity.TrainingType;
import org.example.entity.TrainingTypeEnum;
import org.example.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class StoragesPostProcessorUnitTest {

    private static final String TRAINEES_TEST_DATA_FILE_PATH = "src/test/resources/testStoragesPostProcessor/trainees.csv";
    private static final String TRAINERS_TEST_DATA_FILE_PATH = "src/test/resources/testStoragesPostProcessor/trainers.csv";
    private static final String TRAINEING_TEST_DATA_FILE_PATH = "src/test/resources/testStoragesPostProcessor/trainings.csv";
    private static final String TRAINEE_STORAGE_BEAN_NAME = "traineeStorage";
    private static final String TRAINER_STORAGE_BEAN_NAME = "trainerStorage";
    private static final String TRAINING_STORAGE_BEAN_NAME = "trainingStorage";
    private static final String SOME_BEAN_NAME = "someBean";
    private static final String TRAINEE_TEST_DATA_FILE_PATH_FIELD_NAME = "traineeTestDataFilePath";
    private static final String TRAINER_TEST_DATA_FILE_PATH_FIELD_NAME = "trainerTestDataFilePath";
    private static final String TRAINING_TEST_DATA_FILE_PATH_FIELD_NAME = "trainingTestDataFilePath";
    private static final int TEST_USER_ID_1 = 1;
    private static final int TEST_USER_ID_2 = 2;
    private static final String TRAINING_NAME_1 = "Training1";
    private static final String TRAINING_NAME_2 = "Training2";
    private static final int EXPECTED_NUMBER_OF_CALLS = 2;
    private static final int EXPECTED_EMOUNT_OF_ITEMS = 2;

    @Spy
    BeanPostProcessor testInstance;

    @Spy
    StorageConfig storageConfig;

    @Spy
    InMemoryStorageWithIntId<Trainee> traineeStorage;
    @Spy
    InMemoryStorageWithIntId<Trainer> trainerStorage;
    @Spy
    InMemoryStorage<String, Training> trainingStorage;
    @Mock
    Object someBean;
    @Spy
    Trainee expectedTrainee1;
    @Spy
    Trainee expectedTrainee2;
    @Spy
    Trainer expectedTrainer1;
    @Spy
    Trainer expectedTrainer2;
    @Spy
    Training expectingTraining1;
    @Spy
    Training expectingTraining2;

    @BeforeEach
    void setUp() {
        testInstance = storageConfig.storagesPostProcessor();
        injectTestDataFilePaths();
        prepareVerificationDate();
    }

    @Test
    void shouldPostProcessAfterInitialization_whenItsTraineeStorage() {
        Object result = testInstance.postProcessAfterInitialization(traineeStorage, TRAINEE_STORAGE_BEAN_NAME);

        verify(traineeStorage, times(EXPECTED_NUMBER_OF_CALLS)).put(anyInt(), any(Trainee.class));
        assertEquals(traineeStorage, result);
        checkTraineeInStorage(TEST_USER_ID_1, expectedTrainee1);
        checkTraineeInStorage(TEST_USER_ID_2, expectedTrainee2);
    }

    @Test
    void shouldPostProcessAfterInitialization_whenItsTrainerStorage() {
        Object result = testInstance.postProcessAfterInitialization(trainerStorage, TRAINER_STORAGE_BEAN_NAME);

        verify(trainerStorage, times(EXPECTED_NUMBER_OF_CALLS)).put(anyInt(), any(Trainer.class));
        assertEquals(trainerStorage, result);
        checkTrainerInStorage(TEST_USER_ID_1, expectedTrainer1);
        checkTrainerInStorage(TEST_USER_ID_2, expectedTrainer2);
    }

    @Test
    void shouldPostProcessAfterInitialization_whenItsTrainingStorage() {
        Object result = testInstance.postProcessAfterInitialization(trainingStorage, TRAINING_STORAGE_BEAN_NAME);

        verify(trainingStorage, times(EXPECTED_NUMBER_OF_CALLS)).put(anyString(), any(Training.class));
        assertEquals(EXPECTED_EMOUNT_OF_ITEMS, trainingStorage.size());
        assertEquals(trainingStorage, result);
        checkTrainingInStorage(TRAINING_NAME_1, expectingTraining1);
        checkTrainingInStorage(TRAINING_NAME_2, expectingTraining2);
    }

    @Test
    void shouldPostProcessAfterInitialization_whenItsAnyOtherBean() {
        Object result = testInstance.postProcessAfterInitialization(someBean, SOME_BEAN_NAME);

        assertEquals(someBean, result);
    }

    void checkTraineeInStorage(Integer id, Trainee expectedTrainee) {
        Trainee acturalTrainee = traineeStorage.get(id);
        checkUserInStorage(expectedTrainee, acturalTrainee);
        assertEquals(expectedTrainee.getDateOfBirthday(), acturalTrainee.getDateOfBirthday());
        assertEquals(expectedTrainee.getAddress(), acturalTrainee.getAddress());
    }

    void checkTrainerInStorage(Integer id, Trainer expectedTrainer) {
        Trainer acturalTrainer = trainerStorage.get(id);
        checkUserInStorage(expectedTrainer, acturalTrainer);
        assertEquals(expectedTrainer.getSpecialization(), acturalTrainer.getSpecialization());
    }

    void checkUserInStorage(User expectedUser, User actualUser) {
        assertEquals(expectedUser.getUserId(), actualUser.getUserId());
        assertEquals(expectedUser.getFirstName(), actualUser.getFirstName());
        assertEquals(expectedUser.getLastName(), actualUser.getLastName());
        assertEquals(expectedUser.getUsername(), actualUser.getUsername());
        assertEquals(expectedUser.getPassword(), actualUser.getPassword());
        assertEquals(expectedUser.isActive(), actualUser.isActive());
    }

    void checkTrainingInStorage(String name, Training expectedTraining) {
        Training actualTraining = trainingStorage.get(name);
        assertEquals(expectedTraining.getTrainee().getUserId(), actualTraining.getTrainee().getUserId());
        assertEquals(expectedTraining.getTrainer().getUserId(), actualTraining.getTrainer().getUserId());
        assertEquals(expectedTraining.getTrainingName(), actualTraining.getTrainingName());
        assertEquals(expectedTraining.getTrainingType(), actualTraining.getTrainingType());
        assertEquals(expectedTraining.getTrainingDate(), actualTraining.getTrainingDate());
        assertEquals(expectedTraining.getTrainingDuration(), actualTraining.getTrainingDuration());
    }

    void injectTestDataFilePaths() {
        ReflectionTestUtils.setField(storageConfig, TRAINEE_TEST_DATA_FILE_PATH_FIELD_NAME, TRAINEES_TEST_DATA_FILE_PATH);
        ReflectionTestUtils.setField(storageConfig, TRAINER_TEST_DATA_FILE_PATH_FIELD_NAME, TRAINERS_TEST_DATA_FILE_PATH);
        ReflectionTestUtils.setField(storageConfig, TRAINING_TEST_DATA_FILE_PATH_FIELD_NAME, TRAINEING_TEST_DATA_FILE_PATH);
    }

    void prepareVerificationDate() {
        prepareTraineeVerificationDate();
        prepareTrainerVerificationDate();
        prepareTrainingVerificationDate();
    }

    void prepareTraineeVerificationDate() {
        expectedTrainee1 = Trainee.builder()
                .userId(TEST_USER_ID_1)
                .firstName("John")
                .lastName("Doe")
                .username("John.Doe")
                .password("password")
                .isActive(true)
                .dateOfBirthday(LocalDate.parse("1990-01-01"))
                .address("123 Main St")
                .build();
        expectedTrainee2 = Trainee.builder()
                .userId(TEST_USER_ID_2)
                .firstName("Jane")
                .lastName("Doe")
                .username("Jane.Doe")
                .password("password")
                .isActive(true)
                .dateOfBirthday(LocalDate.parse("1990-02-02"))
                .address("234 Park Ave")
                .build();
    }

    void prepareTrainerVerificationDate() {
        expectedTrainer1 = Trainer.builder()
                .userId(TEST_USER_ID_1)
                .firstName("John")
                .lastName("Doe")
                .username("John.Doe1")
                .password("password")
                .isActive(true)
                .specialization(TrainingType.builder().name(TrainingTypeEnum.CARDIO).build())
                .build();
        expectedTrainer2 = Trainer.builder()
                .userId(TEST_USER_ID_2)
                .firstName("Jane")
                .lastName("Doe")
                .username("Jane.Doe1")
                .password("password")
                .isActive(true)
                .specialization(TrainingType.builder().name(TrainingTypeEnum.STRENGTH).build())
                .build();
    }

    void prepareTrainingVerificationDate() {
        expectingTraining1 = Training.builder()
                .trainee(Trainee.builder().userId(TEST_USER_ID_1).build())
                .trainer(Trainer.builder().userId(TEST_USER_ID_1).build())
                .trainingName(TRAINING_NAME_1)
                .trainingType(TrainingType.builder().name(TrainingTypeEnum.CARDIO).build())
                .trainingDate(LocalDate.parse("2022-01-01"))
                .trainingDuration(60)
                .build();
        expectingTraining2 = Training.builder()
                .trainee(Trainee.builder().userId(TEST_USER_ID_2).build())
                .trainer(Trainer.builder().userId(TEST_USER_ID_2).build())
                .trainingName(TRAINING_NAME_2)
                .trainingType(TrainingType.builder().name(TrainingTypeEnum.STRENGTH).build())
                .trainingDate(LocalDate.parse("2022-01-02"))
                .trainingDuration(45)
                .build();
    }
}
