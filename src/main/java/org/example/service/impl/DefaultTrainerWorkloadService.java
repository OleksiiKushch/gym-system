package org.example.service.impl;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.example.config.security.JwtAuthenticationDetails;
import org.example.dto.request.TrainerWorkloadRequest;
import org.example.service.TrainerWorkloadService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.Month;
import java.time.Year;
import java.util.List;
import java.util.Map;

import static org.example.constants.GeneralConstants.AUTHORIZATION_HEADER_KEY;
import static org.example.constants.GeneralConstants.AUTHORIZATION_HEADER_VALUE_PREFIX;
import static org.example.constants.GeneralConstants.COULD_NOT_FETCH_TRAINING_REPORTERR_MSG;
import static org.example.constants.GeneralConstants.SENT_SUCCESSFULLY_TRAINER_WORKLOAD_REQUEST_MSG;
import static org.example.constants.GeneralConstants.SOME_SERVICE_IS_UNEVAILABLE_ERR_MSG;

@Slf4j
@Getter
@Service
public class DefaultTrainerWorkloadService implements TrainerWorkloadService {

    private static final int FIRST = 0;

    @Value("${trainer-manager.server-name}")
    private String trainerManagerServerName;

    @Value("${trainer-manager.update-training-sessions-mapping}")
    private String trainingSessionMapping;
    @Value("${trainer-manager.get-training-report-mapping}")
    private String trainingReportMapping;

    private final RestTemplate restTemplate;
    private final EurekaClient eurekaClient;

    public DefaultTrainerWorkloadService(RestTemplateBuilder restTemplateBuilder, EurekaClient eurekaClient) {
        this.restTemplate = restTemplateBuilder.build();
        this.eurekaClient = eurekaClient;
    }

    @Override
    public void sendTrainerWorkload(List<TrainerWorkloadRequest> workloadRequests) {
        getRestTemplate()
                .exchange(URI.create(getTrainingManagerServerHomePageUrl() + getTrainingSessionMapping()),
                        HttpMethod.POST, new HttpEntity<>(workloadRequests, prepareHeadersWithToken()), String.class);
        log.info(SENT_SUCCESSFULLY_TRAINER_WORKLOAD_REQUEST_MSG, workloadRequests);
    }

    @Override
    public Map<Year, Map<Month, Integer>> getTrainerTotalHoursReport(String username) {
        ResponseEntity<Map<Year, Map<Month, Integer>>> response = getRestTemplate()
                .exchange(
                        URI.create(getTrainingManagerServerHomePageUrl() + getTrainingReportMapping()),
                        HttpMethod.GET,
                        new HttpEntity<>(username, prepareHeadersWithToken()),
                        new ParameterizedTypeReference<>() {}
                );

        if(HttpStatus.OK.equals(response.getStatusCode())) {
            return response.getBody();
        } else {
            throw new RuntimeException(formErrorMessage(username));
        }
    }

    private String getTrainingManagerServerHomePageUrl() {
        InstanceInfo service;
        try {
            service = eurekaClient
                    .getApplication(getTrainerManagerServerName())
                    .getInstances()
                    .get(FIRST);
        } catch (RuntimeException exception) {
            throw new RuntimeException(SOME_SERVICE_IS_UNEVAILABLE_ERR_MSG);
        }
        return service.getHomePageUrl();
    }

    private HttpHeaders prepareHeadersWithToken() {
        JwtAuthenticationDetails jwtAuthenticationDetails =
                (JwtAuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION_HEADER_KEY, AUTHORIZATION_HEADER_VALUE_PREFIX + jwtAuthenticationDetails.jwtToken());
        return headers;
    }

    private String formErrorMessage(String username) {
        return String.format(COULD_NOT_FETCH_TRAINING_REPORTERR_MSG, username);
    }
}
