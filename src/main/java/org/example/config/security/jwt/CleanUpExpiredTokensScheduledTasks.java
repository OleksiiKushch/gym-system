package org.example.config.security.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dao.RevokedTokenDao;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Slf4j
@Getter
@RequiredArgsConstructor
@Component
public class CleanUpExpiredTokensScheduledTasks {

    public static final String CLEANING_UP_EXPIRED_TOKENS_LOG_MSG = "Search and clean up expired tokens";

    private final RevokedTokenDao revokedTokenDao;

    @Transactional
    @Scheduled(fixedRateString = "${application.security.jwt.expiration}")
    public void performOperation() {
        log.info(CLEANING_UP_EXPIRED_TOKENS_LOG_MSG);
        getRevokedTokenDao().removeAllByExpirationDateTimeBefore(new Date());
    }
}
