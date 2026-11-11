package br.com.stormaid.controller;

import br.com.stormaid.entity.ResponderProfile;
import br.com.stormaid.entity.User;
import br.com.stormaid.enums.ResponderVerificationStatus;
import br.com.stormaid.repository.ResponderProfileRepository;
import br.com.stormaid.service.ResponderProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/internal/responders")
@Tag(name = "Internal Responder Controller", description = "Endpoints institucionais e de provisionamento de socorristas para integração")
public class InternalResponderController {

    private final ResponderProfileService responderProfileService;
    private final ResponderProfileRepository responderProfileRepository;

    public InternalResponderController(ResponderProfileService responderProfileService,
                                       ResponderProfileRepository responderProfileRepository) {
        this.responderProfileService = responderProfileService;
        this.responderProfileRepository = responderProfileRepository;
    }

    @GetMapping
    @Operation(summary = "Lista paginada de perfis de socorristas (Acesso institucional)")
    public ResponseEntity<Page<ResponderProfile>> listResponders(Pageable pageable) {
        return ResponseEntity.ok(responderProfileService.listAll(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulta perfil de socorrista por ID institucional")
    public ResponseEntity<ResponderProfile> findById(@PathVariable Long id) {
        return ResponseEntity.ok(responderProfileService.findById(id));
    }

    @PatchMapping("/{id}/authorization")
    @Operation(summary = "Aprovar ou suspender perfil de socorrista")
    public ResponseEntity<ResponderProfile> updateAuthorization(
            @PathVariable Long id,
            @RequestParam ResponderVerificationStatus status,
            @AuthenticationPrincipal User authorizer) {
        return ResponseEntity.ok(responderProfileService.updateAuthorizationStatus(id, status, authorizer));
    }

    @GetMapping("/by-user/{userId}/eligibility")
    @Operation(summary = "Verificar elegibilidade do socorrista para a integração .NET")
    public ResponseEntity<EligibilityResponse> checkEligibility(@PathVariable Long userId) {
        ResponderProfile profile = responderProfileRepository.findAll().stream()
                .filter(p -> p.getUser().getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Perfil não encontrado para o utilizador"));

        boolean eligible = profile.getVerificationStatus() == ResponderVerificationStatus.APPROVED
                && profile.getUser().getStatus().name().equals("ACTIVE");

        return ResponseEntity.ok(new EligibilityResponse(
                userId,
                profile.getId(),
                eligible,
                profile.getInstitutionType().name(),
                profile.getUser().getStatus().name(),
                profile.getVerificationStatus().name()
        ));
    }

    public record EligibilityResponse(
            Long userId,
            Long responderProfileId,
            boolean eligible,
            String institutionType,
            String userStatus,
            String verificationStatus
    ) {}
}