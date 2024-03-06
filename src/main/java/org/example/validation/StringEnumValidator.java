package org.example.validation;

import io.micrometer.common.util.StringUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static org.example.constants.GeneralConstants.MESSAGE_DELIMETER;

public class StringEnumValidator implements ConstraintValidator<StringEnum, String> {

    private static final String ERROR_MESSAGE = "The value '%s' is not a valid %s. Valid %s values are: %s.";

    private Set<String> acceptedValues;
    private String enumName;

    @Override
    public void initialize(StringEnum constraintAnnotation) {
        acceptedValues = Arrays.stream(constraintAnnotation.enumClass().getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toSet());
        enumName = constraintAnnotation.enumName();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isEmpty(value) || acceptedValues.contains(value)) {
            return true;
        }
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(formErrorMessage(value))
                .addConstraintViolation();
        return false;
    }

    private String formErrorMessage(String value) {
        return String.format(ERROR_MESSAGE, value, enumName, enumName, formMsgWithEnumValues());
    }

    private String formMsgWithEnumValues() {
        return String.join(MESSAGE_DELIMETER, acceptedValues);
    }
}
