package org.example.constants;

/**
 * Constants for persistence layer (HQL / naming / native queries, query parameters, entity / search criteria field names etc.)
 */
public class PersistenceLayerConstants {

    public static final String ID_PARAM = "id";
    public static final String USERNAME_PARAM = "username";
    public static final String TRAINING_TYPE_PARAM = "trainingType";

    public static final String TRAININER_FIELD_NAME = "trainer";
    public static final String TRAININEE_FIELD_NAME = "trainee";
    public static final String USERNAME_FIELD_NAME = "username";
    public static final String FIRST_NAME_FIELD_NAME = "firstName";
    public static final String TRAINING_DATE_FIELD_NAME = "trainingDate";
    public static final String TRAINING_TYPE_FIELD_NAME = "trainingType";
    public static final String NAME_FIELD_NAME = "name";

    // User queries
    public static final String FIND_USER_BY_USERNAME_QUERY_NAME = "User.findByUsername";
    public static final String FIND_ALL_USERS_QUERY_NAME = "User.findAll";

    // Trainee queries
    public static final String FIND_TRAINEE_BY_USERNAME_QUERY_NAME = "Trainee.findByUsername";
    public static final String FIND_TRAINEE_WITH_TRAININGS_BY_USERNAME_QUERY_NAME = "Trainee.findWithTrainingsByUsername";
    public static final String FIND_ALL_TRAINEES_QUERY_NAME = "Trainee.findAll";
    public static final String DELETE_TRAINEE_BY_ID_QUERY_NAME = "Trainee.deleteById";
    public static final String DELETE_TRAINEE_BY_USERNAME_QUERY_NAME = "Trainee.deleteByUsername";

    // Trainer queries
    public static final String FIND_TRAINER_BY_USERNAME_QUERY_NAME = "Trainer.findByUsername";
    public static final String FIND_TRAINER_WITH_TRAININGS_BY_USERNAME_QUERY_NAME = "Trainer.findWithTrainingsByUsername";
    public static final String FIND_ALL_TRAINERS_QUERY_NAME = "Trainer.findAll";
    public static final String FIND_ALL_TRAINERS_NOT_ASSIGNED_TO_TRAINEE_QUERY_NAME = "Trainer.findAllNotAssignedToTrainee";

    // Training type queries
    public static final String FIND_TRAINING_TYPE_BY_NAME_QUERY_NAME = "TrainingType.findByName";
    public static final String FIND_ALL_TRAINING_TYPES_QUERY_NAME = "TrainingType.findAll";
}
