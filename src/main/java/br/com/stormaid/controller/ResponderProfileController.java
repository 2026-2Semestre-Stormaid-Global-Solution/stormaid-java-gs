package br.com.stormaid.controller;

import br.com.stormaid.entity.ResponderProfile;
import br.com.stormaid.entity.User;
import br.com.stormaid.service.ResponderProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/responders")
public class ResponderProfileController {

    private final ResponderProfileService responderProfileService;

    public ResponderProfileController(ResponderProfileService responderProfileService) {
        this.responderProfileService = responderProfileService;
    }

    @GetMapping("/me/profile")
    public ResponseEntity<ResponderProfile> getMyProfile(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(responderProfileService.getProfileByUser(user));
    }
}