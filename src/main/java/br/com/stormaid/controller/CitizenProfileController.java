package br.com.stormaid.controller;

import br.com.stormaid.entity.CitizenProfile;
import br.com.stormaid.entity.User;
import br.com.stormaid.service.CitizenProfileService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/citizens/me/profile")
public class CitizenProfileController {

    private final CitizenProfileService citizenProfileService;

    public CitizenProfileController(CitizenProfileService citizenProfileService) {
        this.citizenProfileService = citizenProfileService;
    }

    @GetMapping
    public ResponseEntity<CitizenProfile> getProfile(@AuthenticationPrincipal User user) {
        CitizenProfile profile = citizenProfileService.getProfileByUser(user);
        return ResponseEntity.ok(profile);
    }

    @PutMapping
    public ResponseEntity<CitizenProfile> updateProfile(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid CitizenProfile updatedData
    ) {
        CitizenProfile updatedProfile = citizenProfileService.updateProfile(user, updatedData);
        return ResponseEntity.ok(updatedProfile);
    }
}