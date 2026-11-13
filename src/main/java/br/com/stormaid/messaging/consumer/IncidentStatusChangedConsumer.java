package br.com.stormaid.messaging.consumer;

import br.com.stormaid.entity.IncidentReport;
import br.com.stormaid.enums.IncidentPublicStatus;
import br.com.stormaid.repository.IncidentReportRepository;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Component
public class IncidentStatusChangedConsumer {

    private final IncidentReportRepository incidentRepository;
    private final ObjectMapper objectMapper;

    public IncidentStatusChangedConsumer(IncidentReportRepository incidentRepository, ObjectMapper objectMapper) {
        this.incidentRepository = incidentRepository;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = "incident.status.changed.queue")
    @Transactional
    public void consumeStatusChanged(String messageJson) {
        try {
            StatusChangedEvent event = objectMapper.readValue(messageJson, StatusChangedEvent.class);

            IncidentReport incident = incidentRepository.findById(event.getIncidentId()).orElse(null);
            if (incident != null) {
                // Valida se a versão recebida é mais recente para evitar retrocessos[cite: 53]
                if (event.getVersion() > incident.getStatusVersion()) {
                    incident.setPublicStatus(IncidentPublicStatus.valueOf(event.getStatus()));
                    incident.setStatusVersion(event.getVersion());
                    incident.setUpdatedAt(Instant.now());
                    incidentRepository.save(incident);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class StatusChangedEvent {
        private Long incidentId;
        private String status;
        private Long version;

        public Long getIncidentId() { return incidentId; }
        public void setIncidentId(Long incidentId) { this.incidentId = incidentId; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public Long getVersion() { return version; }
        public void setVersion(Long version) { this.version = version; }
    }
}