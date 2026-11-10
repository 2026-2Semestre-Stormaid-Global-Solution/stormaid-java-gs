package br.com.stormaid.controller;

import br.com.stormaid.dto.request.IncidentCreateRequest;
import br.com.stormaid.dto.response.IncidentResponse;
import br.com.stormaid.entity.IncidentReport;
import br.com.stormaid.entity.User;
import br.com.stormaid.enums.IncidentPublicStatus;
import br.com.stormaid.enums.TranscriptionStatus;
import br.com.stormaid.repository.IncidentReportRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/incidents")
public class IncidentReportController {

    @Autowired
    private IncidentReportRepository incidentReportRepository;

    @PostMapping
    public ResponseEntity<IncidentResponse> createIncident(
            @AuthenticationPrincipal User requester,
            @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey,
            @RequestBody @Valid IncidentCreateRequest request) {

        if (idempotencyKey != null) {
            var existing = incidentReportRepository.findByRequesterIdAndIdempotencyKey(requester.getId(), idempotencyKey);
            if (existing.isPresent()) {
                IncidentReport inc = existing.get();
                IncidentResponse resp = new IncidentResponse(
                        inc.getId(),
                        inc.getRequester().getId(),
                        inc.getRecipientType(),
                        inc.getChannelType(),
                        inc.getEmergencyCategory(),
                        inc.getPublicStatus(),
                        inc.getLocation(),
                        inc.getCreatedAt()
                );
                return ResponseEntity.status(HttpStatus.OK).body(resp);
            }
        }

        IncidentReport incident = IncidentReport.builder()
                .requester(requester)
                .recipientType(request.recipientType())
                .thirdPartyName(request.thirdPartyName())
                .requesterOnSite(request.requesterOnSite())
                .channelType(request.channelType())
                .emergencyCategory(request.emergencyCategory())
                .textDescription(request.textDescription())
                .location(request.location())
                .publicStatus(IncidentPublicStatus.RECEIVED)
                .statusVersion(1L)
                .transcriptionStatus(TranscriptionStatus.NOT_APPLICABLE)
                .idempotencyKey(idempotencyKey)
                .build();

        IncidentReport savedIncident = incidentReportRepository.save(incident);

        IncidentResponse response = new IncidentResponse(
                savedIncident.getId(),
                savedIncident.getRequester().getId(),
                savedIncident.getRecipientType(),
                savedIncident.getChannelType(),
                savedIncident.getEmergencyCategory(),
                savedIncident.getPublicStatus(),
                savedIncident.getLocation(),
                savedIncident.getCreatedAt()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}