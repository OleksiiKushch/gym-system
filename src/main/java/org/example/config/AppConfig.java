package org.example.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.format.DateTimeFormatter;

@EnableScheduling
@Configuration
public class AppConfig {

    @Value("${default.date.format}")
    private String defaultDateFormat;

    @Bean
    public DateTimeFormatter defaultDateFormatter() {
        return DateTimeFormatter.ofPattern(defaultDateFormat);
    }
}
