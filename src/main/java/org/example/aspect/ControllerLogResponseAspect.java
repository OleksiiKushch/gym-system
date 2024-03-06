package org.example.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

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

    @Around("restController() && allPublicMethods() && !getters() && !setters() && !swaggerEndPoints()")
    public Object loggingResponse(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object response = proceedingJoinPoint.proceed();
        log.info(response.toString());
        return response;
    }
}
