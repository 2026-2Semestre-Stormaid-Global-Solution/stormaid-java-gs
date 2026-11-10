package br.com.stormaid.controller;

import br.com.stormaid.entity.CommunityRiskReport;
import br.com.stormaid.entity.User;
import br.com.stormaid.service.CommunityRiskReportService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/community-risks")
public class CommunityRiskReportController {

    private final CommunityRiskReportService riskReportService;

    public CommunityRiskReportController(CommunityRiskReportService riskReportService) {
        this.riskReportService = riskReportService;
    }

    @PostMapping
    public ResponseEntity<CommunityRiskReport> create(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid CommunityRiskReport riskReport
    ) {
        CommunityRiskReport created = riskReportService.createRiskReport(user, riskReport);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<Page<CommunityRiskReport>> list(
            @AuthenticationPrincipal User user,
            Pageable pageable
    ) {
        Page<CommunityRiskReport> reports = riskReportService.listByRequester(user, pageable);
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommunityRiskReport> getById(
            @AuthenticationPrincipal User user,
            @PathVariable Long id
    ) {
        CommunityRiskReport report = riskReportService.findByIdAndRequester(id, user);
        return ResponseEntity.ok(report);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal User user,
            @PathVariable Long id
    ) {
        riskReportService.deleteRiskReport(id, user);
        return ResponseEntity.noContent().build();
    }
}