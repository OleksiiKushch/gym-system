package org.example.aspect;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.example.facade.UserFacade;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestHeader;

import java.lang.reflect.Parameter;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * This aspect for authorization of requests.
 */
@Slf4j
@Getter
@Aspect
@Order(2)
@Component
public class ControllerAuthorizingAspect extends AbstractControllerAspect {

    private static final String HEADER_KEY = "Authorization";
    private static final int START_INDEX = 0;

    private final UserFacade userFacade;

    public ControllerAuthorizingAspect(@Lazy UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    @Pointcut("execution(* registerTrainee(..)) || execution(* registerTrainer(..))")
    protected void userRegistrationMethods() {
    }

    @Pointcut("execution(* loginUser(..))")
    protected void loginMethod() {
    }

    @Pointcut("loginMethod() || userRegistrationMethods() || swaggerEndPoints()")
    protected void nonAuthorizingMethods() {
    }

    @Pointcut("execution(* *(..,@org.springframework.web.bind.annotation.RequestHeader(\"Authorization\") String,..))")
    public void methodsWithAuthToken() {
    }

    @Around("restController() && allPublicMethods() && !nonAuthorizingMethods() && !getters() && !setters()")
    public Object authorizing(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String token = getTokenFromRequest(proceedingJoinPoint);

        if (StringUtils.isEmpty(token) || !getUserFacade().authorizationCurrentUser(token)) {
            ResponseEntity<?> response = ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            log.info(response.toString());
            return response;
        }
        return proceedingJoinPoint.proceed();
    }

    private String getTokenFromRequest(ProceedingJoinPoint proceedingJoinPoint) {
        Parameter[] params = getParams(proceedingJoinPoint);
        Object[] args = proceedingJoinPoint.getArgs();

        return IntStream.range(START_INDEX, params.length)
                .filter(i -> checkParameter(params[i]))
                .mapToObj(i -> args[i].toString())
                .findFirst()
                .orElse(null);
    }

    private Parameter[] getParams(ProceedingJoinPoint proceedingJoinPoint) {
        return ((MethodSignature) proceedingJoinPoint.getSignature()).getMethod().getParameters();
    }

    private boolean checkParameter(Parameter parameter) {
        RequestHeader annotation = parameter.getAnnotation(RequestHeader.class);
        return Objects.nonNull(annotation) && HEADER_KEY.equals(annotation.value());
    }
}
