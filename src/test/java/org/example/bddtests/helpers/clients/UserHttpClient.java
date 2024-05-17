package org.example.bddtests.helpers.clients;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.spring.ScenarioScope;
import lombok.Getter;
import org.example.dto.form.LoginForm;
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
public class UserHttpClient {

    private static final String SERVER_URL = "http://localhost:";
    private static final String LOGIN_ENDPOINT = "/login";

    @LocalServerPort
    private int port;

    private final RestTemplate restTemplate;

    public UserHttpClient() {
        restTemplate = new RestTemplate();
    }

    public ResponseEntity<String> loginUser(LoginForm loginForm) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(new ObjectMapper().writeValueAsString(loginForm), headers);
        try {
            return restTemplate.exchange(getLoginEndpoint(), HttpMethod.POST, entity, String.class);
        } catch (HttpClientErrorException | HttpServerErrorException exception) {
            return ResponseEntity.status(exception.getStatusCode()).body(exception.getMessage());
        }
    }

    public String getLoginEndpoint() {
        return SERVER_URL + getPort() + LOGIN_ENDPOINT;
    }
}
