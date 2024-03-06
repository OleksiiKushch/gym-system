package org.example.aspect;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static org.example.constants.GeneralConstants.MESSAGE_DELIMETER;

/**
 * This aspect for logging requests (method arguments).
 */
@Slf4j
@Aspect
@Order(1)
@Component
public class ControllerLogRequestAspect extends AbstractControllerAspect {

    private static final String LEFT_SQUARE_BRACKET = "[";
    private static final String RIGHT_SQUARE_BRACKET = "]";

    private static final Set<Class<?>> EXCLUDED_ARGS_TYPES = Set.of(
            BeanPropertyBindingResult.class,
            ServletUriComponentsBuilder.class);

    @Around("restController() && allPublicMethods() && !getters() && !setters() && !swaggerEndPoints()")
    public Object loggingRequest(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        log.info(formRequestLogMessage(proceedingJoinPoint));
        return proceedingJoinPoint.proceed();
    }

    private String formRequestLogMessage(ProceedingJoinPoint proceedingJoinPoint) {
        return proceedingJoinPoint.getSignature().toShortString() + StringUtils.SPACE + formPresentationOfArgs(proceedingJoinPoint);
    }

    private String formPresentationOfArgs(ProceedingJoinPoint proceedingJoinPoint) {
        return processArgs(proceedingJoinPoint);
    }

    /**
     * To exclude useless logging arguments (like BindException or UriComponentsBuilder).
     */
    private String processArgs(ProceedingJoinPoint proceedingJoinPoint) {
        return Arrays.stream(proceedingJoinPoint.getArgs())
                .filter(arg -> !(EXCLUDED_ARGS_TYPES.contains(arg.getClass())))
                .map(Object::toString)
                .collect(Collectors.joining(MESSAGE_DELIMETER, LEFT_SQUARE_BRACKET, RIGHT_SQUARE_BRACKET));
    }
}
