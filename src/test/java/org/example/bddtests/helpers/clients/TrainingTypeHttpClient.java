package org.example.bddtests.helpers.clients;

import io.cucumber.spring.ScenarioScope;
import lombok.Getter;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Getter
@Component
@ScenarioScope
public class TrainingTypeHttpClient {

    private static final String SERVER_URL = "http://localhost:";
    private static final String TRAINING_TYPE_ENDPOINT = "/training-types";

    @LocalServerPort
    private int port;

    private final RestTemplate restTemplate;

    public TrainingTypeHttpClient() {
        restTemplate = new RestTemplate();
    }

    public ResponseEntity<String> getTrainingTypeCatalog(String jwtToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(jwtToken);
        try {
            return restTemplate.exchange(getTrainingTypeEndpoint(), HttpMethod.GET, new HttpEntity<>(headers), String.class);
        } catch (HttpClientErrorException | HttpServerErrorException exception) {
            return ResponseEntity.status(exception.getStatusCode()).body(exception.getMessage());
        }
    }

    private String getTrainingTypeEndpoint() {
        return SERVER_URL + getPort() + TRAINING_TYPE_ENDPOINT;
    }
}
