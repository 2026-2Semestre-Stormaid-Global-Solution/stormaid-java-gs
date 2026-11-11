package br.com.stormaid.controller;

import br.com.stormaid.entity.IncidentReport;
import br.com.stormaid.repository.IncidentReportRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/internal/incidents")
@Tag(name = "Internal Incident Controller", description = "Endpoints de integração interna para a arquitetura .NET[cite: 48]")
public class InternalIncidentController {

    private final IncidentReportRepository incidentReportRepository;

    public InternalIncidentController(IncidentReportRepository incidentReportRepository) {
        this.incidentReportRepository = incidentReportRepository;
    }

    @GetMapping("/{id}/regulation-view")
    @Operation(summary = "Obter visão de regulação do incidente para a .NET[cite: 48, 50]")
    public ResponseEntity<Object> getRegulationView(@PathVariable Long id) {
        IncidentReport incident = incidentReportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ocorrência não encontrada"));

        return ResponseEntity.ok(incident);
    }
}