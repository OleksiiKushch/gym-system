package org.example.config;

import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableWebMvc
@EnableAspectJAutoProxy
@ComponentScan(basePackages = "org.example")
@PropertySource("classpath:application.properties")
public class AppConfig {

    @Value("${default.date.format}")
    private String defaultDateFormat;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfig() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public ValidatorFactory validatorFactory(){
        return Validation.buildDefaultValidatorFactory();
    }

    @Bean
    public Set<String> tokenStore() {
        return new HashSet<>();
    }

    @Bean
    public DateTimeFormatter defaultDateFormatter() {
        return DateTimeFormatter.ofPattern(defaultDateFormat);
    }
}
