package br.com.stormaid.messaging;

import br.com.stormaid.config.RabbitMQConfig;
import br.com.stormaid.dto.event.IncidentCreatedEvent;
import br.com.stormaid.entity.IncidentReport;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class IncidentEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public IncidentEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishIncidentCreated(IncidentReport incident) {
        try {
            IncidentCreatedEvent event = new IncidentCreatedEvent(
                    incident.getId(),
                    incident.getRequester() != null ? incident.getRequester().getId() : null,
                    incident.getRecipientType(),
                    incident.getChannelType(),
                    incident.getEmergencyCategory(),
                    incident.getPublicStatus(),
                    incident.getLocation(),
                    incident.getCreatedAt()
            );

            rabbitTemplate.convertAndSend(RabbitMQConfig.INCIDENT_CREATED_QUEUE, event);
        } catch (Exception e) {
            System.err.println("Erro ao publicar evento no RabbitMQ: " + e.getMessage());
        }
    }
}