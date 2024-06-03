package org.example.bddtests.helpers.clients;

import io.cucumber.spring.ScenarioScope;
import lombok.Getter;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Getter
@Component
@ScenarioScope
public class TrainerHttpClient {

    private static final String SERVER_URL = "http://localhost:";
    private static final String TRAINERS_ENDPOINT = "/trainers";
    private static final String REPORT_ENDPOINT = "/report";

    @LocalServerPort
    private int port;

    private final RestTemplate restTemplate;

    public TrainerHttpClient() {
        restTemplate = new RestTemplate();
    }

    public ResponseEntity<String> formReport(String jwtToken, String trainerUsername) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        try {
            return restTemplate.exchange(formReportEndpoint(trainerUsername), HttpMethod.GET, entity, String.class);
        } catch (HttpClientErrorException | HttpServerErrorException exception) {
            return ResponseEntity.status(exception.getStatusCode()).body(exception.getMessage());
        }
    }

    private String formReportEndpoint(String trainerUsername) {
        return SERVER_URL + getPort() + TRAINERS_ENDPOINT + "/" + trainerUsername + REPORT_ENDPOINT;
    }
}
