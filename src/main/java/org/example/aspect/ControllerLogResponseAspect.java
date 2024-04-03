package org.example.aspect;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.example.exception.AppException;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import static org.example.constants.GeneralConstants.BAD_REQUEST;
import static org.example.constants.GeneralConstants.LEFT_SQUARE_BRACKET;
import static org.example.constants.GeneralConstants.RIGHT_SQUARE_BRACKET;

/**
 * This aspect for:
 * <br/>
 * - logging responses (method return value);
 * <br/>
 * - handling exceptions thrown by controllers ({@link org.example.exception.AppException});
 */
@Slf4j
@Aspect
@Order(4)
@Component
public class ControllerLogResponseAspect extends AbstractControllerAspect {

    @Around("controller() && allPublicMethods() && !getters() && !setters()")
    public Object loggingResponse(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object response = handleAppException(proceedingJoinPoint);
        log.info(response.toString());
        return response;
    }

    private Object handleAppException(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        try {
            return proceedingJoinPoint.proceed();
        } catch (AppException exception) {
            return formResponse(exception);
        }
    }

    private String formResponse(Exception exception) {
        return BAD_REQUEST + StringUtils.SPACE + formSingleErrorMessage(exception.getMessage());
    }

    private String formSingleErrorMessage(String errorMessage) {
        return LEFT_SQUARE_BRACKET + errorMessage + RIGHT_SQUARE_BRACKET;
    }
}
