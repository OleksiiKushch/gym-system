package com.example.trainermanager.validation;

import com.example.trainermanager.constants.GeneralConstants;
import com.example.trainermanager.dto.request.TrainerWorkloadRequest;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class TrainerWorkloadRequestValidator implements Validator {

    private static final String TRAINER_USERNAME = "trainerUsername";
    private static final String TRAINER_FIRST_NAME = "trainerFirstName";
    private static final String TRAINER_LAST_NAME = "trainerLastName";
    private static final String IS_ACTIVE = "isActive";
    private static final String TRAINING_DATE = "trainingDate";
    private static final String TRAINING_DURATION = "trainingDuration";
    private static final String ACTION_TYPE = "actionType";

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return TrainerWorkloadRequest.class.equals(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, TRAINER_USERNAME, GeneralConstants.EMPTY_FIELD_MSG_CODE,
                GeneralConstants.EMPTY_FIELD_DEFAULT_MSG);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, TRAINER_FIRST_NAME, GeneralConstants.EMPTY_FIELD_MSG_CODE,
                GeneralConstants.EMPTY_FIELD_DEFAULT_MSG);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, TRAINER_LAST_NAME, GeneralConstants.EMPTY_FIELD_MSG_CODE,
                GeneralConstants.EMPTY_FIELD_DEFAULT_MSG);
        ValidationUtils.rejectIfEmpty(errors, IS_ACTIVE, GeneralConstants.EMPTY_FIELD_MSG_CODE,
                GeneralConstants.EMPTY_FIELD_DEFAULT_MSG);
        ValidationUtils.rejectIfEmpty(errors, TRAINING_DATE, GeneralConstants.EMPTY_FIELD_MSG_CODE,
                GeneralConstants.EMPTY_FIELD_DEFAULT_MSG);
        ValidationUtils.rejectIfEmpty(errors, TRAINING_DURATION, GeneralConstants.EMPTY_FIELD_MSG_CODE,
                GeneralConstants.EMPTY_FIELD_DEFAULT_MSG);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, ACTION_TYPE, GeneralConstants.EMPTY_FIELD_MSG_CODE,
                GeneralConstants.EMPTY_FIELD_DEFAULT_MSG);
    }
}
