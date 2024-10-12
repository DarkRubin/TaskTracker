package org.roadmap.tasktrackeremailsender.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.roadmap.tasktrackeremailsender.dto.MessageDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConsumerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerService.class);

    private final MailService mailService;

    @KafkaListener(topics = "EMAIL_SENDING_TASKS", groupId = "task-tracker")
    public void listen(String message) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            MessageDto dto = objectMapper.readValue(message, MessageDto.class);
            mailService.send(dto.recipient(), dto.subject(), dto.text());
        } catch (JsonProcessingException e) {
            LOGGER.error("Error while converting message to json", e);
        }
    }
}
