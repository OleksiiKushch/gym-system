package com.example.testcors;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
public class TestController {

    @GetMapping("/test")
    public String testCors() {
        RestTemplate restTemplate = new RestTemplate();

        String url = "http://localhost:8080/login";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> loginForm = new HashMap<>();
        loginForm.put("username", "John.Doe");
        loginForm.put("password", "123");

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(loginForm, headers);

        return restTemplate.postForEntity(url, request, String.class).getBody();
    }
}
