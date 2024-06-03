package org.example.bddtests.constants;

public interface BddTestsConstants {

    // General
    String SLASH = "/";
    String COLON = ":";

    String MSG_DELIMITER = "~";

    // Configuration
    String DATASOURCE_URL_KEY = "spring.datasource.url";
    String DATASOURCE_USERNAME_KEY = "spring.datasource.username";
    String DATASOURCE_PASSWORD_KEY = "spring.datasource.password";
    String ACTIVEMQ_BROKER_URL_KEY = "spring.activemq.broker-url";
    String ACTIVEMQ_USER_KEY = "spring.activemq.user";
    String ACTIVEMQ_PASSWROD_KEY = "spring.activemq.password";

    String PROFILE_TEST = "test";
    String SPRING_PROFILES_ACTIVE_PROPERTY = "SPRING_PROFILES_ACTIVE";
    String MONGODB_URI_PROPERTY = "MONGODB_URI";
    String ACTIVEMQ_BROKER_URL_PROPERTY = "ACTIVEMQ_BROKER_URL";

    String ACTIVEMQ_PROTOCOL = "tcp://";
    String DEFAULT_ACTIVEMQ_PORT = "61616";
    String MONGO_PROTOCOL = "mongodb://";
    String DEFAULT_MONGO_PORT = "27017";

    String TRAINER_MANAGER_DB = "trainermanagerdb";

    String SKIP_TESTS_DOCKER_ARG = "$SKIP_TESTS";

    // Context
    String REGISTRATION_TRAINEE_FORM = "registrationTraineeForm";
    String CREATE_TRAINING_FORM = "createTrainingForm";
    String LAST_RESPONSE = "lastResponse";
    String USERNAEM = "username";
    String TRAINEE = "trainee";
    String LOGIN_FORM = "loginForm";
    String JWT_TOKEN = "jwtToken";
    String TRAINING = "training";

    // User fields
    String USERNAME = "Username";
    String PASSWORD = "Password";

    // Trainee fields
    String FIRST_NAME = "First Name";
    String LAST_NAME = "Last Name";
    String CONFIRMATION_PASSWORD = "Confirmation Password";
    String DATE_OF_BIRTHDAY = "Date of Birthday";
    String ADDRESS = "Address";

    // Training fields
    String TRAINEE_USERNAME = "Trainee Username";
    String TRAINER_USERNAME = "Trainer Username";
    String TRAINING_NAME = "Training Name";
    String TRAINING_TYPE = "Training Type";
    String TRAINING_DATE = "Training Date";
    String DURATION = "Duration";
}
