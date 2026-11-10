package br.com.stormaid.controller;

import br.com.stormaid.entity.ResponderProfile;
import br.com.stormaid.entity.User;
import br.com.stormaid.enums.ResponderVerificationStatus;
import br.com.stormaid.service.ResponderProfileService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class ResponderProfileController {

    private final ResponderProfileService responderProfileService;

    public ResponderProfileController(ResponderProfileService responderProfileService) {
        this.responderProfileService = responderProfileService;
    }

    @GetMapping("/responders/me/profile")
    public ResponseEntity<ResponderProfile> getMyProfile(@AuthenticationPrincipal User user) {
        ResponderProfile profile = responderProfileService.getProfileByUser(user);
        return ResponseEntity.ok(profile);
    }

    @GetMapping("/internal/responders")
    public ResponseEntity<Page<ResponderProfile>> listResponders(Pageable pageable) {
        Page<ResponderProfile> profiles = responderProfileService.listAll(pageable);
        return ResponseEntity.ok(profiles);
    }

    @GetMapping("/internal/responders/{id}")
    public ResponseEntity<ResponderProfile> getResponderById(@PathVariable Long id) {
        ResponderProfile profile = responderProfileService.findById(id);
        return ResponseEntity.ok(profile);
    }

    @PatchMapping("/internal/responders/{id}/authorization")
    public ResponseEntity<ResponderProfile> updateAuthorization(
            @PathVariable Long id,
            @RequestParam ResponderVerificationStatus status,
            @AuthenticationPrincipal User authorizer
    ) {
        ResponderProfile updated = responderProfileService.updateAuthorizationStatus(id, status, authorizer);
        return ResponseEntity.ok(updated);
    }
}