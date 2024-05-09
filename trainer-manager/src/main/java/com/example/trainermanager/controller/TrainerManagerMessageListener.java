package com.example.trainermanager.controller;

import com.example.trainermanager.dto.request.TrainerWorkloadRequest;
import com.example.trainermanager.entity.ActionType;
import com.example.trainermanager.exception.RequestValidationException;
import com.example.trainermanager.service.TrainerTrainingSessionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.TextMessage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.validation.SimpleErrors;
import org.springframework.validation.Validator;

import java.time.Month;
import java.util.List;
import java.util.Map;

@Slf4j
@Getter
@RequiredArgsConstructor
@Component
public class TrainerManagerMessageListener {

    private static final String RECEIVED_MESSAGE_MSG = "Received message: {}";
    public static final String REQUEST_VALIDATION_FAILED_MSG = "Request validation failed: {}";
    public static final String REQUIRED_INFORMATION_MISSING_MSG = "Required information missing. Request validation failed: %s";
    public static final String MESSAGE_TYPE_MISMATCH = "Message type mismatch";

    private static final ObjectMapper objectMapper = createObjectMapper();

    private final TrainerTrainingSessionService trainerTrainingSessionService;
    private final JmsTemplate jmsTemplate;
    private final Validator trainerWorkloadRequestValidator;

    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

    @JmsListener(destination = "${activemq.query.name.trainerworkload}")
    public void receiveMessage(List<?> message) {
        log.info(RECEIVED_MESSAGE_MSG, message);
        message.stream()
                .map(this::convertToTrainerWorkloadRequest)
                .peek(this::validateTrainerWorkloadRequestOrThrowException)
                .forEach(request -> {
                            switch (ActionType.valueOf(request.getActionType())) {
                                case ADD:
                                    getTrainerTrainingSessionService().updateTrainerTrainingSummary(request);
                                    break;
                                case DELETE:
                                    getTrainerTrainingSessionService().deleteTrainerTrainingSession(request);
                                    break;
                            }
                        }
                );
    }

    @JmsListener(destination = "${activemq.query.name.report}")
    public void receiveMessage(Message message) throws JMSException, JsonProcessingException {
        if (message instanceof TextMessage testMessage) {
            final String username = testMessage.getText();
            log.info(RECEIVED_MESSAGE_MSG, username);
            Map<Integer, Map<Month, Integer>> report = getTrainerTrainingSessionService().formReport(username);

            String reply = objectMapper.writeValueAsString(report);

            jmsTemplate.convertAndSend(message.getJMSReplyTo(), reply, m -> {
                m.setJMSCorrelationID(message.getJMSCorrelationID());
                return m;
            });
        } else {
            log.error(MESSAGE_TYPE_MISMATCH);
            throw new RequestValidationException(MESSAGE_TYPE_MISMATCH);
        }

    }

    private void validateTrainerWorkloadRequestOrThrowException(TrainerWorkloadRequest request) {
        SimpleErrors errors = new SimpleErrors(request);
        getTrainerWorkloadRequestValidator().validate(request, errors);
        if (errors.hasErrors()) {
            log.error(REQUEST_VALIDATION_FAILED_MSG, errors.getFieldErrors());
            throw new RequestValidationException(formErrorMessage(errors.getFieldErrors().toString()));
        }
    }

    private TrainerWorkloadRequest convertToTrainerWorkloadRequest(Object message) {
        return objectMapper.convertValue(message, TrainerWorkloadRequest.class);
    }

    private String formErrorMessage(Object... args) {
        return String.format(REQUIRED_INFORMATION_MISSING_MSG, args);
    }

}
