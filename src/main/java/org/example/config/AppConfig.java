package org.example.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

@Configuration
public class AppConfig {

    @Value("${default.date.format}")
    private String defaultDateFormat;

    @Bean
    public Set<String> tokenStore() {
        return new HashSet<>();
    }

    @Bean
    public DateTimeFormatter defaultDateFormatter() {
        return DateTimeFormatter.ofPattern(defaultDateFormat);
    }
}
