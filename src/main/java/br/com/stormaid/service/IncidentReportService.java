package br.com.stormaid.service;

import br.com.stormaid.entity.IncidentReport;
import br.com.stormaid.entity.User;
import br.com.stormaid.enums.IncidentPublicStatus;
import br.com.stormaid.enums.TranscriptionStatus;
import br.com.stormaid.repository.IncidentReportRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.com.stormaid.messaging.IncidentEventPublisher;

import java.time.Instant;

@Service
public class IncidentReportService {

    private final IncidentReportRepository incidentReportRepository;
    private final IncidentEventPublisher eventPublisher;

    public IncidentReportService(IncidentReportRepository incidentReportRepository, IncidentEventPublisher eventPublisher) {
        this.incidentReportRepository = incidentReportRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public IncidentReport createIncident(User requester, IncidentReport incident) {
        incident.setRequester(requester);
        incident.setPublicStatus(IncidentPublicStatus.RECEIVED);
        incident.setStatusVersion(1L);
        incident.setCreatedAt(Instant.now());
        incident.setUpdatedAt(Instant.now());

        if (incident.getTranscriptionStatus() == null) {
            incident.setTranscriptionStatus(TranscriptionStatus.NOT_APPLICABLE);
        }

        IncidentReport saved = incidentReportRepository.save(incident);

        eventPublisher.publishIncidentCreated(saved);

        return saved;
    }

    public Page<IncidentReport> listByRequester(User requester, Pageable pageable) {
        return incidentReportRepository.findByRequester(requester, pageable);
    }

    public IncidentReport findByIdAndRequester(Long id, User requester) {
        return incidentReportRepository.findById(id)
                .filter(incident -> incident.getRequester().getId().equals(requester.getId()))
                .orElseThrow(() -> new RuntimeException("Ocorrência não encontrada ou não autorizada"));
    }
}