package org.example.constants;

public class GeneralConstants {

    public static final String LEFT_SQUARE_BRACKET = "[";
    public static final String RIGHT_SQUARE_BRACKET = "]";
    public static final String MESSAGE_DELIMETER = ", ";

    public static final String PASSWORD_PLACEHOLDER = "****";

    public static final String CREATE_HIBERNATE_SESSION_FACTORY_EXCEPTION_MSG = "Failed to create Hibernate SessionFactory";

    // Service layer exception messages
    public static final String DELETE_ACTIVE_TRAINEE_EXCEPTION_MSG = "Active trainee (username: %s) cannot be deleted";
    public static final String USER_IS_DEACTIVATED_EXCEPTION_MSG = "This user is deactivated";
    public static final String USER_TYPE_DOES_NOT_MUCH_EXCEPTION_MSG = "User type does not match";
    public static final String ANONYMOUS_USER_CAN_NOT_BE_DEACTIVATED_EXCEPTION_MSG = "Anonymous user can't be deactivated";
    public static final String ANONYMOUS_USER_CAN_NOT_CHANGE_PASSWORD_EXCEPTION_MSG = "Anonymous user can't change password";
    public static final String ANONYMOUS_USER_CAN_NOT_BE_UPDATED_EXCEPTION_MSG = "Anonymous user can't be updated";
    public static final String USER_IS_ALREADY_DEACTIVATED_EXCEPTION_MSG = "User is already deactivated";
    public static final String USER_IS_ALREADY_ACTIVE_EXCEPTION_MSG = "User with username: %s is already active";
    public static final String USER_BY_USERNAME_NOT_FOUND_EXCEPTION_MSG = "User with username: %s not found";
    public static final String PASSWORD_DOES_NOT_MUTCH_EXCEPTION_MSG = "Current password does not match";
    public static final String USERNAME_OR_PASSWORD_DO_NOT_MUTCH_EXCEPTION_MSG = "Username or password does not match";
    public static final String TRAINING_TYPE_NOT_FOUND_EXCEPTION_MSG = "Training type with name: %s does not exist";
    public static final String TRAINEE_NOT_FOUND_EXCEPTION_MSG = "Trainee with username '%s' not found";
    public static final String TRAINER_NOT_FOUND_EXCEPTION_MSG = "Trainer with username '%s' not found";

    public static final String CURRENT_USER_ATTR = "currentUser";

    public static final String PUT_USER_TO_SESSION_LOG_MSG = "Put user with username '{}' to session";

    // Controller layer
    public static final String BAD_REQUEST = "400, Bad request";
    public static final String OK_REQUEST = "200, OK";
    public static final String UNAUTHORIZED_REQUEST = "401, Unauthorized";

}
