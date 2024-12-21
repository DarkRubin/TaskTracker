package org.roadmap.tasktrackerbackend.kafka;

import lombok.RequiredArgsConstructor;
import org.roadmap.tasktrackerbackend.dto.MessageDTO;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducer {

    private static final String SUCCESS_REGISTRATION_SUBJECT = "Success registration😎";
    private static final String SUCCESS_REGISTRATION_MESSAGE = "Hello, %s! Your registration was successful!";

    private final KafkaTemplate<String, MessageDTO> kafkaTemplate;

    public void sendSuccessRegistration(String email) {
        kafkaTemplate.sendDefault(new MessageDTO(email,
                SUCCESS_REGISTRATION_SUBJECT,
                SUCCESS_REGISTRATION_MESSAGE.formatted(email)
        ));
    }


}
