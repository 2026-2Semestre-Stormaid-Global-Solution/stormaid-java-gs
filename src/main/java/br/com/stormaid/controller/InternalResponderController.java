package br.com.stormaid.controller;

import br.com.stormaid.dto.request.ResponderAuthorizationRequest;
import br.com.stormaid.dto.request.ResponderProvisionRequest;
import br.com.stormaid.dto.response.ResponderProfileResponse;
import br.com.stormaid.service.ResponderProfileService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/internal/responders")
public class InternalResponderController {

    private final ResponderProfileService responderProfileService;

    public InternalResponderController(ResponderProfileService responderProfileService) {
        this.responderProfileService = responderProfileService;
    }

    @PostMapping
    @PreAuthorize("hasRole('REGULATOR')")
    public ResponseEntity<ResponderProfileResponse> provisionResponder(@Valid @RequestBody ResponderProvisionRequest request) {
        ResponderProfileResponse response = responderProfileService.provisionResponder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('REGULATOR')")
    public ResponseEntity<Page<ResponderProfileResponse>> listResponders(Pageable pageable) {
        return ResponseEntity.ok(responderProfileService.listAll(pageable));
    }

    @PatchMapping("/{id}/authorization")
    @PreAuthorize("hasRole('REGULATOR')")
    public ResponseEntity<ResponderProfileResponse> updateAuthorization(
            @PathVariable Long id,
            @Valid @RequestBody ResponderAuthorizationRequest request) {
        ResponderProfileResponse response = responderProfileService.updateAuthorization(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-user/{userId}/eligibility")
    public ResponseEntity<Object> checkEligibility(@PathVariable Long userId) {
        return ResponseEntity.ok(responderProfileService.checkEligibility(userId));
    }
}