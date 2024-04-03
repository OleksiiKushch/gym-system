package org.example.validation;

import io.micrometer.common.util.StringUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

public class StringLocalDateValidator implements ConstraintValidator<StringLocalDate, String> {

    private static final String ERROR_FORMAT_MESSAGE =
            "The value '%s' does not match the expected date format. Please ensure the date follows the format '%s'.";

    @Value("${default.date.format}")
    private String defaultDateFormat;

    private DateTimeFormatter dateFormatter;

    @Override
    public void initialize(StringLocalDate constraintAnnotation) {
        String annotatedDateFormat = constraintAnnotation.dateFormat();
        if (StringUtils.isNotEmpty(annotatedDateFormat)) {
            defaultDateFormat = annotatedDateFormat;
        }
        dateFormatter = DateTimeFormatter.ofPattern(defaultDateFormat);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isEmpty(value)) {
            return true;
        }
        String result = validateDate(value);
        if (Objects.nonNull(result)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(result)
                    .addConstraintViolation();
            return false;
        }
        return true;
    }

    private String validateDate(String strDate) {
        String result = validateDateFormat(strDate);
        if (Objects.nonNull(result)) {
            return result;
        } else {
            return validateDateValue(strDate);
        }
    }

    private String validateDateFormat(String strDate) {
        try {
            dateFormatter.parse(strDate);
        } catch (DateTimeParseException ignored) {
            return formErrorMessage(strDate);
        }
        return null;
    }

    private String formErrorMessage(String value) {
        return String.format(ERROR_FORMAT_MESSAGE, value, defaultDateFormat);
    }

    private String validateDateValue(String strDate) {
        try {
            var ignored = LocalDate.parse(strDate);
        } catch (DateTimeParseException exception) {
            return exception.getMessage();
        }
        return null;
    }

}
