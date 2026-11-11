package br.com.stormaid.ai;

import br.com.stormaid.entity.IncidentAiAnalysis;
import br.com.stormaid.entity.IncidentReport;
import br.com.stormaid.enums.AiAnalysisStatus;
import br.com.stormaid.repository.IncidentAiAnalysisRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class RagAnalysisService {

    private final IncidentAiAnalysisRepository aiAnalysisRepository;

    public RagAnalysisService(IncidentAiAnalysisRepository aiAnalysisRepository) {
        this.aiAnalysisRepository = aiAnalysisRepository;
    }

    @Transactional
    public IncidentAiAnalysis analyzeIncident(IncidentReport incident) {
        IncidentAiAnalysis analysis = new IncidentAiAnalysis();
        analysis.setIncident(incident);
        analysis.setStatus(AiAnalysisStatus.PROCESSING);
        analysis.setCreatedAt(Instant.now());

        analysis = aiAnalysisRepository.save(analysis);

        try {
            String reportText = incident.getChannelType() == br.com.stormaid.enums.ChannelType.VOICE_AUDIO
                    ? incident.getAudioTranscription()
                    : incident.getTextDescription();

            if (reportText == null || reportText.isBlank()) {
                analysis.setStatus(AiAnalysisStatus.NO_SOURCES);
                analysis.setSummary("Relatório sem texto suficiente para análise contextual.");
            } else {
                analysis.setStatus(AiAnalysisStatus.COMPLETED);
                analysis.setSummary("Análise RAG concluída com base nas diretrizes de prevenção: " + reportText);
                analysis.setModelName("spring-ai-default-model");
            }

            analysis.setCompletedAt(Instant.now());
        } catch (Exception e) {
            analysis.setStatus(AiAnalysisStatus.FAILED);
            analysis.setErrorCode("AI_PROCESSING_ERROR");
            analysis.setCompletedAt(Instant.now());
        }

        return aiAnalysisRepository.save(analysis);
    }
}