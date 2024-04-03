package org.example.constants;

public class GeneralConstants {

    public static final String MESSAGE_DELIMETER = ", ";

    public static final String PASSWORD_PLACEHOLDER = "****";

    public static final String TRAINING_TYPE_ENUM_NAME = "Training Type";
    public static final String SPECIALIZATION_ENUM_NAME = "Specialization";

    // Service layer exception messages
    public static final String DELETE_ACTIVE_TRAINEE_EXCEPTION_MSG = "Active trainee (username: %s) cannot be deleted";
    public static final String PASSWORD_DOES_NOT_MUTCH_EXCEPTION_MSG = "Current password does not match";
    public static final String TRAINING_TYPE_NOT_FOUND_EXCEPTION_MSG = "Training type with name: %s does not exist";
    public static final String USER_NOT_FOUND_EXCEPTION_MSG = "User with username '%s' not found";
    public static final String TRAINEE_NOT_FOUND_EXCEPTION_MSG = "Trainee with username '%s' not found";
    public static final String TRAINER_NOT_FOUND_EXCEPTION_MSG = "Trainer with username '%s' not found";
    public static final String TOO_MANY_REQUESTS_EXCEPTION_MSG = "Too many requests. Please try again later.";

    public static final String START_TRANSACTION_LOG_MSG = "Starting Transaction for method: {}, Transaction ID: {}";
    public static final String END_TRANSACTION_LOG_MSG = "Ending Transaction for method: {}, Transaction ID: {}";

    public static final String ERROR_PARAMETER = "Error";
    public static final String BAD_CREDENTIALS = "Bad credentials";
}
