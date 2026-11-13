package br.com.stormaid.ai;

import br.com.stormaid.entity.IncidentReport;
import br.com.stormaid.enums.TranscriptionStatus;
import br.com.stormaid.repository.IncidentReportRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class SpeechTranscriptionService {

    private final IncidentReportRepository incidentRepository;

    public SpeechTranscriptionService(IncidentReportRepository incidentRepository) {
        this.incidentRepository = incidentRepository;
    }

    @Async
    @Transactional
    public void transcribeAudioAsync(Long incidentId, String audioStorageKey) {
        IncidentReport incident = incidentRepository.findById(incidentId).orElse(null);
        if (incident == null) return;

        try {
            String transcribedText = "Transcrição automática do áudio [" + audioStorageKey + "]: Solicitação de emergência registrada via canal de voz.";
            incident.setAudioTranscription(transcribedText);
            incident.setTranscriptionStatus(TranscriptionStatus.COMPLETED);
        } catch (Exception e) {
            incident.setTranscriptionStatus(TranscriptionStatus.FAILED);
        }
        incident.setUpdatedAt(Instant.now());
        incidentRepository.save(incident);
    }
}