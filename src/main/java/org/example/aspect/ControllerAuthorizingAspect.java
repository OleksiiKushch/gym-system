package org.example.aspect;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.example.facade.UserFacade;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import static org.example.constants.GeneralConstants.UNAUTHORIZED_REQUEST;

/**
 * This aspect for authorization of requests.
 */
@Slf4j
@Getter
@Aspect
@Order(2)
@Component
public class ControllerAuthorizingAspect extends AbstractControllerAspect {

    private final UserFacade userFacade;

    public ControllerAuthorizingAspect(@Lazy UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    @Pointcut("execution(* registerTrainee(..)) || execution(* registerTrainer(..))")
    protected void userRegistrationMethods() {
    }

    @Pointcut("execution(* loginUser(..)) || execution(* logoutUser(..))")
    protected void userSessionMethods() {
    }

    @Pointcut("userSessionMethods() || userRegistrationMethods()")
    protected void nonAuthorizingMethods() {
    }

    @Around("controller() && allPublicMethods() && !nonAuthorizingMethods() && !getters() && !setters()")
    public Object authorizing(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        if (!getUserFacade().authorizationCurrentUser()) {
            log.info(UNAUTHORIZED_REQUEST);
            return UNAUTHORIZED_REQUEST;
        }
        return proceedingJoinPoint.proceed();
    }
}
