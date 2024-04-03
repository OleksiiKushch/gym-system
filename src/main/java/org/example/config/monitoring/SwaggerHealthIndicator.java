package org.example.config.monitoring;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static org.example.constants.GeneralConstants.ERROR_PARAMETER;

@Getter
@Component
public class SwaggerHealthIndicator implements HealthIndicator {

    private static final String SWAGGER_DOWN_MSG = "Swagger UI is not responding";

    @Value("${springdoc.swagger-ui.path}")
    private String swaggerPath;

    private final RestTemplate restTemplate;

    public SwaggerHealthIndicator(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Override
    public Health health() {
        if (!check().getStatusCode().equals(HttpStatus.OK)) {
            return Health.down()
                    .withDetail(ERROR_PARAMETER, SWAGGER_DOWN_MSG)
                    .build();
        }
        return Health.up()
                .build();
    }

    private ResponseEntity<String> check() {
        String localUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        return restTemplate.getForEntity(localUrl + swaggerPath, String.class);
    }
}
