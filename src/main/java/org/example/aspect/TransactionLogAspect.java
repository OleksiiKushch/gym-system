package org.example.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static org.example.constants.GeneralConstants.END_TRANSACTION_LOG_MSG;
import static org.example.constants.GeneralConstants.START_TRANSACTION_LOG_MSG;

@Slf4j
@Aspect
@Component
public class TransactionLogAspect {

    private static final String TRANSACTION_ID_ATTR = "transactionId";

    @Pointcut("@annotation(org.springframework.transaction.annotation.Transactional)")
    public void transactionalMethods() {
    }

    @Before("transactionalMethods()")
    public void beforeTransactional(JoinPoint joinPoint) {
        String transactionId = UUID.randomUUID().toString();

        MDC.put(TRANSACTION_ID_ATTR, transactionId);

        log.info(START_TRANSACTION_LOG_MSG, joinPoint.getSignature().getName(), transactionId);
    }

    @After("transactionalMethods()")
    public void afterTransactional(JoinPoint joinPoint) {
        log.info(END_TRANSACTION_LOG_MSG, joinPoint.getSignature().getName(), MDC.get(TRANSACTION_ID_ATTR));

        MDC.remove(TRANSACTION_ID_ATTR);
    }
}
