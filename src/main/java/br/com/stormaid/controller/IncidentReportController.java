package br.com.stormaid.controller;

import br.com.stormaid.dto.request.IncidentCreateRequest;
import br.com.stormaid.dto.response.IncidentResponse;
import br.com.stormaid.entity.User;
import br.com.stormaid.service.IncidentReportService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/incidents")
public class IncidentReportController {

    private final IncidentReportService incidentReportService;

    public IncidentReportController(IncidentReportService incidentReportService) {
        this.incidentReportService = incidentReportService;
    }

    @PostMapping
    public ResponseEntity<IncidentResponse> create(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid IncidentCreateRequest request
    ) {
        IncidentResponse created = incidentReportService.createIncident(user, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<Page<IncidentResponse>> list(
            @AuthenticationPrincipal User user,
            Pageable pageable
    ) {
        Page<IncidentResponse> incidents = incidentReportService.listByRequester(user, pageable);
        return ResponseEntity.ok(incidents);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IncidentResponse> getById(
            @AuthenticationPrincipal User user,
            @PathVariable Long id
    ) {
        IncidentResponse incident = incidentReportService.findByIdAndRequester(id, user);
        return ResponseEntity.ok(incident);
    }
}