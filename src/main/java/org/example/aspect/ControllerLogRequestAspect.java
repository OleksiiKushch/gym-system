package org.example.aspect;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * This aspect for logging requests (method arguments).
 */
@Slf4j
@Aspect
@Order(1)
@Component
public class ControllerLogRequestAspect extends AbstractControllerAspect {

    @Around("controller() && allPublicMethods() && !getters() && !setters()")
    public Object loggingRequest(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        log.info(formRequestLogMessage(proceedingJoinPoint));
        return proceedingJoinPoint.proceed();
    }

    private String formRequestLogMessage(ProceedingJoinPoint proceedingJoinPoint) {
        return proceedingJoinPoint.getSignature().toShortString() + StringUtils.SPACE + formPresentationOfArgs(proceedingJoinPoint);
    }

    private String formPresentationOfArgs(ProceedingJoinPoint proceedingJoinPoint) {
        return Arrays.toString(proceedingJoinPoint.getArgs());
    }
}
