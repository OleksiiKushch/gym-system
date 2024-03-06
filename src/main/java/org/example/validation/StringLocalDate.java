package org.example.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {StringLocalDateValidator.class})
public @interface StringLocalDate {

    String dateFormat() default "";
    String message() default "incorrect date format";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
