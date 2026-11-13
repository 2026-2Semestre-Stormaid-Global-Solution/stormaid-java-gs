package br.com.stormaid.messaging.publisher;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Component
public class IncidentAiEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(IncidentAiEventPublisher.class);
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public IncidentAiEventPublisher(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    public void publishAiCompleted(Long incidentId, Long analysisId, String status) {
        try {
            Map<String, Object> event = Map.of(
                    "eventType", "incident.ai.completed.v1",
                    "eventId", "evt-ai-" + UUID.randomUUID(),
                    "incidentId", incidentId,
                    "analysisId", analysisId,
                    "analysisStatus", status,
                    "completedAt", Instant.now().toString()
            );
            String json = objectMapper.writeValueAsString(event);
            rabbitTemplate.convertAndSend("stormaid.events", "incident.ai.completed.v1", json);
        } catch (Exception e) {
            log.error("Erro ao publicar evento incident.ai.completed.v1: {}", e.getMessage());
        }
    }
}