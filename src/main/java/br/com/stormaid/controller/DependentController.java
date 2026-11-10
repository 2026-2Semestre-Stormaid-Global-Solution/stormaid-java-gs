package br.com.stormaid.controller;

import br.com.stormaid.entity.Dependent;
import br.com.stormaid.entity.User;
import br.com.stormaid.service.DependentService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/dependents")
public class DependentController {

    private final DependentService dependentService;

    public DependentController(DependentService dependentService) {
        this.dependentService = dependentService;
    }

    @PostMapping
    public ResponseEntity<Dependent> create(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid Dependent dependent
    ) {
        Dependent created = dependentService.createDependent(user, dependent);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<Page<Dependent>> list(
            @AuthenticationPrincipal User user,
            Pageable pageable
    ) {
        Page<Dependent> dependents = dependentService.listByGuardian(user, pageable);
        return ResponseEntity.ok(dependents);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dependent> getById(
            @AuthenticationPrincipal User user,
            @PathVariable Long id
    ) {
        Dependent dependent = dependentService.findByIdAndGuardian(id, user);
        return ResponseEntity.ok(dependent);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Dependent> update(
            @AuthenticationPrincipal User user,
            @PathVariable Long id,
            @RequestBody @Valid Dependent dependent
    ) {
        Dependent updated = dependentService.updateDependent(id, user, dependent);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal User user,
            @PathVariable Long id
    ) {
        dependentService.deleteDependent(id, user);
        return ResponseEntity.noContent().build();
    }
}