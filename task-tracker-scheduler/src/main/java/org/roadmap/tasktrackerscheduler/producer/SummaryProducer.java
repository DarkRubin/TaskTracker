package org.roadmap.tasktrackerscheduler.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.roadmap.tasktrackerscheduler.dto.DaySummaryDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SummaryProducer {

    private static final String TOPIC = "EMAIL_SENDING_TASKS";

    private static final String UNFINISHED_TASKS_MESSAGE = """
            Hello, %s! Today you have %d unfinished tasks.
            %s
            
            Don't sadness! :)
            """;

    private static final String FINISHED_TASKS_MESSAGE = """
            Hello, %s! Today you have %d finished tasks.
            %s
            
            You are awesome!
            """;

    private static final String ALL_TASKS_MESSAGE = """
            Hello, %s! Today you had %d tasks. You completed %d tasks.
            Planed: %s
            
            
            Completed: %s
            
            You are cool!
            """;

    private static final Logger LOGGER = LoggerFactory.getLogger(SummaryProducer.class);

    public static final String JSON_CONVERSION_ERROR = "Error while converting message to json";

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public SummaryProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    private void send(Message message) {
        try {
            String jsonMessage = objectMapper.writeValueAsString(message);
            kafkaTemplate.send(TOPIC, jsonMessage);
        } catch (JsonProcessingException e) {
            LOGGER.error(JSON_CONVERSION_ERROR, e);
        }
    }

    public void sendUnfinishedTasks(DaySummaryDto dto) {
        String formatted = String.format(UNFINISHED_TASKS_MESSAGE, dto.getEmail(),
                dto.getUnfinishedCount(), dto.getUnfinishedTasks().toString());
        send(new Message(dto.getEmail(), "Unfinished tasks😥", formatted));
    }

    public void sendFinishedTasks(DaySummaryDto dto) {
        String formatted = String.format(FINISHED_TASKS_MESSAGE, dto.getEmail(),
                dto.getFinishedCount(), dto.getFinishedTasks().toString());
        send(new Message(dto.getEmail(), "Finished tasks🥳", formatted));
    }

    public void sendAllTasks(DaySummaryDto dto) {
        String allTaskMessageFormated = String.format(ALL_TASKS_MESSAGE, dto.getEmail(), dto.getUnfinishedCount(),
                dto.getFinishedCount(),
                dto.getUnfinishedTasks().toString(), dto.getFinishedTasks().toString());
        send(new Message(dto.getEmail(), "Your summary🤩", allTaskMessageFormated));
    }

    @Data
    @AllArgsConstructor
    private static class Message {
        private String recipient;
        private String subject;
        private String text;
    }

}
