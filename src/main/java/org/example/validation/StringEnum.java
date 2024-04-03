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
@Constraint(validatedBy = {StringEnumValidator.class})
public @interface StringEnum {

    Class<? extends Enum<?>> enumClass();
    String enumName();
    String message() default "invalidate enum type";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
