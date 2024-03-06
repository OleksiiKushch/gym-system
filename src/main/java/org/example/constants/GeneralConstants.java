package org.example.constants;

public class GeneralConstants {

    public static final String MESSAGE_DELIMETER = ", ";

    public static final String PASSWORD_PLACEHOLDER = "****";

    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

    public static final String TRAINING_TYPE_ENUM_NAME = "Training Type";
    public static final String SPECIALIZATION_ENUM_NAME = "Specialization";

    public static final String CREATE_HIBERNATE_SESSION_FACTORY_EXCEPTION_MSG = "Failed to create Hibernate SessionFactory";

    // Service layer exception messages
    public static final String DELETE_ACTIVE_TRAINEE_EXCEPTION_MSG = "Active trainee (username: %s) cannot be deleted";
    public static final String PASSWORD_DOES_NOT_MUTCH_EXCEPTION_MSG = "Current password does not match";
    public static final String USERNAME_OR_PASSWORD_DO_NOT_MUTCH_EXCEPTION_MSG = "Username or password does not match";
    public static final String TRAINING_TYPE_NOT_FOUND_EXCEPTION_MSG = "Training type with name: %s does not exist";
    public static final String USER_NOT_FOUND_EXCEPTION_MSG = "User with username '%s' not found";
    public static final String TRAINEE_NOT_FOUND_EXCEPTION_MSG = "Trainee with username '%s' not found";
    public static final String TRAINER_NOT_FOUND_EXCEPTION_MSG = "Trainer with username '%s' not found";

    public static final String NEW_AUTHORIZATION_TOKEN_LOG_MSG = "Generated new authorization token: {}";
    public static final String STORED_TOKENS_LOG_MSG = "Stored tokens: {}";
    public static final String DEACTIVATE_TOKEN_LOG_MSG = "Deactivated token: {}";
}
