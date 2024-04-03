package org.example.aspect;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Parameter;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.example.constants.GeneralConstants.BAD_REQUEST;
import static org.example.constants.GeneralConstants.LEFT_SQUARE_BRACKET;
import static org.example.constants.GeneralConstants.MESSAGE_DELIMETER;
import static org.example.constants.GeneralConstants.RIGHT_SQUARE_BRACKET;

@Slf4j
@Getter
@Aspect
@Order(3)
@Component
public class ControllerValidationAspect extends AbstractControllerAspect {

    private static final int START_INDEX = 0;

    private final ValidatorFactory validatorFactory;

    public ControllerValidationAspect(ValidatorFactory validatorFactory) {
        this.validatorFactory = validatorFactory;
    }

    @Pointcut(value = "execution(* *.*(.., @jakarta.validation.Valid (*), ..))")
    public void isValid() {
    }

    @Around("controller() && allPublicMethods() && isValid() && !setters() && !getters()")
    public Object validate(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Parameter[] parameters = getParams(proceedingJoinPoint);
        Object[] args = proceedingJoinPoint.getArgs();

        Optional<String> response = IntStream.range(START_INDEX, parameters.length)
                .filter(i -> isValidatedParam(parameters[i]))
                .mapToObj(i -> getValidator().validate(args[i]))
                .filter(result -> !result.isEmpty())
                .map(this::formResponse)
                .peek(log::info)
                .findFirst();

        if (response.isPresent()) {
            return response.get();
        }

        return proceedingJoinPoint.proceed();

    }

    private Parameter[] getParams(ProceedingJoinPoint proceedingJoinPoint) {
        return ((MethodSignature) proceedingJoinPoint.getSignature()).getMethod().getParameters();
    }

    private boolean isValidatedParam(Parameter param) {
        return Objects.nonNull(param.getAnnotation(Valid.class));
    }

    private Validator getValidator() {
        return getValidatorFactory().getValidator();
    }

    private String formResponse(Set<ConstraintViolation<Object>> result) {
        return BAD_REQUEST + StringUtils.SPACE + fromErrorMessage(result);
    }

    private String fromErrorMessage(Set<ConstraintViolation<Object>> result) {
        return LEFT_SQUARE_BRACKET + processErrors(result) + RIGHT_SQUARE_BRACKET;
    }

    private String processErrors(Set<ConstraintViolation<Object>> result) {
        return result.stream()
                .map(this::formSingleErrorMessage)
                .collect(Collectors.joining(MESSAGE_DELIMETER));
    }

    private String formSingleErrorMessage(ConstraintViolation<Object> error) {
        return error.getPropertyPath() + StringUtils.SPACE + error.getMessage();
    }
}
