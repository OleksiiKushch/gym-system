package org.example.aspect;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

import static org.example.constants.GeneralConstants.SOME_SERVICE_IS_UNEVAILABLE_ERR_MSG;

@Slf4j
@Getter
@RequiredArgsConstructor
@Aspect
@Component
public class CircuitBreakerAspect {

    private static final int FIRST = 0;
    private static final int ONE_SECOND = 1000;

    private static final String ACTIVATED_CIRCUIT_BREAKER_MSG = "Activated circuit breaker due to the unavailability of the service.";
    private static final String CIRCUIT_BREAKER_IS_ACTIVE_MSG =
            "Circuit breaker is active. Less than {} ms have passed since the first service unavailability. Time remaining until next check: {} seconds.";

    @Value("${trainer-manager.server-name}")
    private String trainerManagerServerName;
    @Value("${application.fault-tolerance.circuit-breaker.timeout}")
    private long timeout;

    private final EurekaClient eurekaClient;

    private final AtomicLong lastFailureTimestamp = new AtomicLong(0L);

    @Pointcut("execution(* org.example.facade.impl.DefaultTrainingFacade.createTraining(..))")
    public void createTraining() {
    }

    @Pointcut("execution(* org.example.facade.impl.DefaultTrainingFacade.deleteTraining(..))")
    public void deleteTraining() {
    }

    @Pointcut("execution(* org.example.facade.impl.DefaultTraineeFacade.deleteTrainee(..))")
    public void deleteTrainee() {
    }

    @Around("createTraining() || deleteTraining() || deleteTrainee()")
    public Object circuitBreakerAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long currentTimeMillis = System.currentTimeMillis();
        long timePassedSinceLastFailure = currentTimeMillis - lastFailureTimestamp.get();
        long remainingTimeMillis = getTimeout() - (timePassedSinceLastFailure);
        if (timePassedSinceLastFailure < getTimeout()) {
            log.info(CIRCUIT_BREAKER_IS_ACTIVE_MSG, getTimeout(), remainingTimeMillis / ONE_SECOND);
            throw new RuntimeException(SOME_SERVICE_IS_UNEVAILABLE_ERR_MSG);
        }

        InstanceInfo service;
        try {
            service = eurekaClient
                    .getApplication(getTrainerManagerServerName())
                    .getInstances()
                    .get(FIRST);
        } catch (RuntimeException exception) {
            processFailure(currentTimeMillis);
            throw new RuntimeException(SOME_SERVICE_IS_UNEVAILABLE_ERR_MSG);
        }

        if (service != null && InstanceInfo.InstanceStatus.UP.equals(service.getStatus())) {
            return joinPoint.proceed();
        } else {
            processFailure(currentTimeMillis);
            throw new RuntimeException(SOME_SERVICE_IS_UNEVAILABLE_ERR_MSG);
        }
    }

    private void processFailure(long currentTimeMillis) {
        lastFailureTimestamp.set(currentTimeMillis);
        log.info(ACTIVATED_CIRCUIT_BREAKER_MSG);
    }
}
