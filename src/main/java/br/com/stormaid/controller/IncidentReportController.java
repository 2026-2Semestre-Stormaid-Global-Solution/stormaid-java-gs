package br.com.stormaid.controller;

import br.com.stormaid.entity.IncidentReport;
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
    public ResponseEntity<IncidentReport> create(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid IncidentReport incident
    ) {
        IncidentReport created = incidentReportService.createIncident(user, incident);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<Page<IncidentReport>> list(
            @AuthenticationPrincipal User user,
            Pageable pageable
    ) {
        Page<IncidentReport> incidents = incidentReportService.listByRequester(user, pageable);
        return ResponseEntity.ok(incidents);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IncidentReport> getById(
            @AuthenticationPrincipal User user,
            @PathVariable Long id
    ) {
        IncidentReport incident = incidentReportService.findByIdAndRequester(id, user);
        return ResponseEntity.ok(incident);
    }
}