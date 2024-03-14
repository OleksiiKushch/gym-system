package org.example.constants;

/**
 * Constants for persistence layer (HQL / naming / native queries, query parameters, entity / search criteria field names etc.)
 */
public class PersistenceLayerConstants {

    public static final String USERNAME_PARAM = "username";

    public static final String TRAININER_FIELD_NAME = "trainer";
    public static final String TRAININEE_FIELD_NAME = "trainee";
    public static final String USERNAME_FIELD_NAME = "username";
    public static final String FIRST_NAME_FIELD_NAME = "firstName";
    public static final String TRAINING_DATE_FIELD_NAME = "trainingDate";
    public static final String TRAINING_TYPE_FIELD_NAME = "trainingType";
    public static final String NAME_FIELD_NAME = "name";

    public static final String NUMBER_OF_ACTIVE_TOKENS_METRIC_NAME = "number_of_active_authentication_tokens";
    public static final String TOKENS_VERIFICATION_COUNT_METRIC_NAME = "authentication_tokens_verify_count";

    // Trainer queries
    public static final String FIND_ALL_TRAINERS_THAT_NOT_ASSIGNED_TO_TRAINEE_QUERY_NAME = "Trainer.findAllNotAssignedToTrainee";
}
