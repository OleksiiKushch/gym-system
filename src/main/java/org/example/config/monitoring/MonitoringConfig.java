package org.example.config.monitoring;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.binder.MeterBinder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.example.constants.PersistenceLayerConstants.TOKENS_VERIFICATION_COUNT_METRIC_NAME;
import static org.example.constants.PersistenceLayerConstants.USERNAME_PARAM;

@Configuration
public class MonitoringConfig {

    public static final String COUNT_OF_TOKEN_VERIFICATIONS_DESCRIPTION = "Count of token verifications";

    @Bean
    public MeterBinder meterBinder() {
        return meterRegistry -> Counter.builder(TOKENS_VERIFICATION_COUNT_METRIC_NAME)
                    .description(COUNT_OF_TOKEN_VERIFICATIONS_DESCRIPTION)
                    .tag(USERNAME_PARAM, StringUtils.EMPTY)
                    .register(meterRegistry);
    }
}
