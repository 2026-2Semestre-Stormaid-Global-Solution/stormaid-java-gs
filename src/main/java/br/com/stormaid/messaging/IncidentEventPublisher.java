package br.com.stormaid.messaging;

import br.com.stormaid.config.RabbitMQConfig;
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
            rabbitTemplate.convertAndSend(RabbitMQConfig.INCIDENT_CREATED_QUEUE, incident);
        } catch (Exception e) {
            // Regista o erro de publicação mas não bloqueia o fluxo principal da API
            System.err.println("Erro ao publicar evento no RabbitMQ: " + e.getMessage());
        }
    }
}