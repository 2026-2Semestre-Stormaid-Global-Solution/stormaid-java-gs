package br.com.stormaid.messaging.consumer;

import br.com.stormaid.ai.RagAnalysisService;
import br.com.stormaid.entity.IncidentReport;
import br.com.stormaid.repository.IncidentReportRepository;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class IncidentAiConsumer {

    private static final Logger log = LoggerFactory.getLogger(IncidentAiConsumer.class);
    private final RagAnalysisService ragAnalysisService;
    private final IncidentReportRepository incidentRepository;
    private final ObjectMapper objectMapper;

    public IncidentAiConsumer(RagAnalysisService ragAnalysisService, IncidentReportRepository incidentRepository, ObjectMapper objectMapper) {
        this.ragAnalysisService = ragAnalysisService;
        this.incidentRepository = incidentRepository;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = "incident.ai.queue")
    public void consumeIncidentCreated(String messageJson) {
        try {
            IncidentEventPayload payload = objectMapper.readValue(messageJson, IncidentEventPayload.class);
            if (payload.getIncidentId() != null) {
                log.info("Iniciando analise RAG assincrona para o incidente ID: {}", payload.getIncidentId());
                IncidentReport incident = incidentRepository.findById(payload.getIncidentId()).orElse(null);
                if (incident != null) {
                    ragAnalysisService.analyzeIncident(incident);
                }
            }
        } catch (Exception e) {
            log.error("Falha ao processar evento de IA para incidente: {}", e.getMessage());
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class IncidentEventPayload {
        private Long incidentId;
        public Long getIncidentId() { return incidentId; }
        public void setIncidentId(Long incidentId) { this.incidentId = incidentId; }
    }
}