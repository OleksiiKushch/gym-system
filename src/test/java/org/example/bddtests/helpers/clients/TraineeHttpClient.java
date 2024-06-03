package org.example.bddtests.helpers.clients;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.spring.ScenarioScope;
import lombok.Getter;
import org.example.dto.form.RegistrationTraineeForm;
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
public class TraineeHttpClient {

    private static final String SERVER_URL = "http://localhost:";
    private static final String TRAINEES_ENDPOINT = "/trainees";

    @LocalServerPort
    private int port;

    private final RestTemplate restTemplate;

    public TraineeHttpClient() {
        restTemplate = new RestTemplate();
    }

    public ResponseEntity<String> registerTrainee(RegistrationTraineeForm registrationTraineeForm) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(new ObjectMapper().writeValueAsString(registrationTraineeForm), headers);
        try {
            return restTemplate.exchange(getTraineesEndpoint(), HttpMethod.POST, entity, String.class);
        } catch (HttpClientErrorException | HttpServerErrorException exception) {
            return ResponseEntity.status(exception.getStatusCode()).body(exception.getMessage());
        }
    }

    private String getTraineesEndpoint() {
        return SERVER_URL + getPort() + TRAINEES_ENDPOINT;
    }
}
