package br.com.stormaid.controller;

import br.com.stormaid.dto.response.IncidentRegulationViewResponse;
import br.com.stormaid.service.IncidentReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/internal/incidents")
@Tag(name = "Internal Incident Controller", description = "Endpoints de integração interna para a arquitetura .NET")
public class InternalIncidentController {

    private final IncidentReportService incidentReportService;

    public InternalIncidentController(IncidentReportService incidentReportService) {
        this.incidentReportService = incidentReportService;
    }

    @GetMapping("/{id}/regulation-view")
    @Operation(summary = "Obter visão de regulação do incidente para a .NET")
    public ResponseEntity<IncidentRegulationViewResponse> getRegulationView(@PathVariable Long id) {
        IncidentRegulationViewResponse response = incidentReportService.getRegulationView(id);
        return ResponseEntity.ok(response);
    }
}