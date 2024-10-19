package org.roadmap.tasktrackerbackend.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendSuccessRegistration(String email) {
        Message message = new Message(
                email, "Success registration😎", "Hello, " + email + "! Your registration was successful!"
        );
        try {
            String jsonMessage = objectMapper.writeValueAsString(message);
            kafkaTemplate.send("EMAIL_SENDING_TASKS", jsonMessage);
        } catch (JsonProcessingException e) {
            log.error("Error while converting message to json", e);
        }
    }

    @Data
    @AllArgsConstructor
    private static class Message {
        private String recipient;
        private String subject;
        private String text;
    }
}
