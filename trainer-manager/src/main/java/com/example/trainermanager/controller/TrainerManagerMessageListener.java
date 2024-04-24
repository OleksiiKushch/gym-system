package com.example.trainermanager.controller;

import com.example.trainermanager.dto.request.TrainerWorkloadRequest;
import com.example.trainermanager.entity.ActionType;
import com.example.trainermanager.entity.TrainerTrainingSession;
import com.example.trainermanager.exception.RequestValidationException;
import com.example.trainermanager.service.TrainerTrainingSessionService;
import com.example.trainermanager.utils.YearKeyDeserializer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.TextMessage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.validation.SimpleErrors;
import org.springframework.validation.Validator;

import java.time.Month;
import java.time.Year;
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

    private static final ObjectMapper objectMapper = createObjectMapper();

    private final TrainerTrainingSessionService trainerTrainingSessionService;
    private final ModelMapper modelMapper;
    private final JmsTemplate jmsTemplate;
    private final Validator trainerWorkloadRequestValidator;

    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        SimpleModule module = new SimpleModule();
        module.addKeyDeserializer(Year.class, new YearKeyDeserializer());
        mapper.registerModule(module);
        return mapper;
    }

    @JmsListener(destination = "${activemq.query.name.trainerworkload}")
    public void receiveMessage(List<?> message) {
        log.info(RECEIVED_MESSAGE_MSG, message);
        message.stream()
                .map(this::convertToTrainerWorkloadRequest)
                .peek(this::validateTrainerWorkloadRequestOrThrowException)
                .map(this::convertToTrainerTrainingSession)
                .forEach(entry -> { // key is TrainerTrainingSession, value is ActionType
                            switch (ActionType.valueOf(entry.getValue())) {
                                case ADD:
                                    getTrainerTrainingSessionService().saveTrainerTrainingSession(entry.getKey());
                                    break;
                                case DELETE:
                                    getTrainerTrainingSessionService().deleteTrainerTrainingSession(entry.getKey());
                                    break;
                            }
                        }
                );
    }

    @JmsListener(destination = "${activemq.query.name.report}")
    public void receiveMessage(Message message) throws JMSException, JsonProcessingException {
        log.info(RECEIVED_MESSAGE_MSG, message);
        Map<Year, Map<Month, Integer>> report = getTrainerTrainingSessionService()
                .formReport(((TextMessage) message).getText());

        String reply = objectMapper.writeValueAsString(report);

        jmsTemplate.convertAndSend(message.getJMSReplyTo(), reply, m -> {
            m.setJMSCorrelationID(message.getJMSCorrelationID());
            return m;
        });
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

    private Map.Entry<TrainerTrainingSession, String> convertToTrainerTrainingSession(TrainerWorkloadRequest request) {
        return Map.entry(getModelMapper().map(request, TrainerTrainingSession.class), request.getActionType());
    }

    private String formErrorMessage(Object... args) {
        return String.format(REQUIRED_INFORMATION_MISSING_MSG, args);
    }

}
