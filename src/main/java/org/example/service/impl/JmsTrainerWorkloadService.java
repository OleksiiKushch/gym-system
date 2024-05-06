package org.example.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.TextMessage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.dto.request.TrainerWorkloadRequest;
import org.example.service.TrainerWorkloadService;
import org.example.utils.YearKeyDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Month;
import java.time.Year;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Getter
@RequiredArgsConstructor
@Service
public class JmsTrainerWorkloadService implements TrainerWorkloadService {

    private static final String EMPTY_JSON = "{}";
    private static final String BACKSLASH = "\\";
    private static final int FIRST = 1;

    private static final ObjectMapper objectMapper = createObjectMapper();

    @Value("${activemq.query.name.trainerworkload}")
    private String trainerWorkloadQueryName;
    @Value("${activemq.query.name.report}")
    private String reportQueueName;

    private final JmsTemplate jmsTemplate;

    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addKeyDeserializer(Year.class, new YearKeyDeserializer());
        mapper.registerModule(module);
        return mapper;
    }

    @Override
    public void sendTrainerWorkload(List<TrainerWorkloadRequest> workloadRequest) {
        getJmsTemplate().convertAndSend(getTrainerWorkloadQueryName(), workloadRequest);
    }

    @Override
    public Map<Year, Map<Month, Integer>> getTrainerTotalHoursReport(String username) {
        Message replyMessage = getJmsTemplate().sendAndReceive(getReportQueueName(), session -> {
            TextMessage message = session.createTextMessage();
            message.setText(username);
            return message;
        });

        String jsonResult = EMPTY_JSON;
        if (Objects.nonNull(replyMessage)) {
            jsonResult = processPlainMessageContent(getPlainMessageContent(replyMessage));
        }

        try {
            return objectMapper.readValue(jsonResult, new TypeReference<>(){});
        } catch (IOException exception) {
            log.info(exception.getMessage());
            throw new RuntimeException(exception);
        }
    }

    private String getPlainMessageContent(Message message) {
        try {
            return message.getBody(String.class);
        } catch (JMSException exception) {
            throw new RuntimeException(exception);
        }
    }

    private String processPlainMessageContent(String content) {
        final int indexOfLastChar = content.length() - 1;
        return content.substring(FIRST, indexOfLastChar).replace(BACKSLASH, StringUtils.EMPTY);
    }
}
