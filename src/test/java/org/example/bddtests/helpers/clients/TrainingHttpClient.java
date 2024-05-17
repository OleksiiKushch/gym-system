package org.example.bddtests.helpers.clients;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.spring.ScenarioScope;
import lombok.Getter;
import org.example.dto.form.CreateTrainingForm;
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
public class TrainingHttpClient {

    private static final String SERVER_URL = "http://localhost:";
    private static final String TRAININGS_ENDPOINT = "/trainings";

    @LocalServerPort
    private int port;

    private final RestTemplate restTemplate;

    public TrainingHttpClient() {
        restTemplate = new RestTemplate();
    }

    public ResponseEntity<String> createNewTraining(String jwtToken, CreateTrainingForm createTrainingForm) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(jwtToken);
        HttpEntity<String> entity = new HttpEntity<>(new ObjectMapper().writeValueAsString(createTrainingForm), headers);
        try {
            return restTemplate.exchange(getTraineesEndpoint(), HttpMethod.POST, entity, String.class);
        } catch (HttpClientErrorException | HttpServerErrorException exception) {
            return ResponseEntity.status(exception.getStatusCode()).body(exception.getMessage());
        }
    }

    private String getTraineesEndpoint() {
        return SERVER_URL + getPort() + TRAININGS_ENDPOINT;
    }
}
