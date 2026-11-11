package br.com.stormaid.service;

import br.com.stormaid.dto.request.ResponderAuthorizationRequest;
import br.com.stormaid.dto.request.ResponderProvisionRequest;
import br.com.stormaid.dto.response.ResponderEligibilityResponse;
import br.com.stormaid.dto.response.ResponderProfileResponse;
import br.com.stormaid.entity.ResponderProfile;
import br.com.stormaid.entity.User;
import br.com.stormaid.enums.ResponderVerificationStatus;
import br.com.stormaid.repository.ResponderProfileRepository;
import br.com.stormaid.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class ResponderProfileService {

    private final ResponderProfileRepository responderProfileRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ResponderProfileService(ResponderProfileRepository responderProfileRepository,
                                   UserRepository userRepository,
                                   PasswordEncoder passwordEncoder) {
        this.responderProfileRepository = responderProfileRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public ResponderProfileResponse provisionResponder(ResponderProvisionRequest request) {
        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(request.getTemporaryPassword()));
        user.setStatus(br.com.stormaid.enums.UserStatus.ACTIVE);
        User savedUser = userRepository.save(user);

        ResponderProfile profile = new ResponderProfile();
        profile.setUser(savedUser);
        profile.setBadgeNumber(request.getBadgeNumber());
        profile.setInstitutionType(request.getInstitutionType());
        profile.setVerificationStatus(ResponderVerificationStatus.PENDING);
        profile.setCreatedAt(Instant.now());
        profile.setUpdatedAt(Instant.now());

        ResponderProfile savedProfile = responderProfileRepository.save(profile);

        return mapToResponse(savedProfile);
    }

    public Page<ResponderProfileResponse> listAll(Pageable pageable) {
        return responderProfileRepository.findAll(pageable).map(this::mapToResponse);
    }

    @Transactional
    public ResponderProfileResponse updateAuthorization(Long id, ResponderAuthorizationRequest request) {
        ResponderProfile profile = responderProfileRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Perfil de socorrista não encontrado com ID: " + id));

        profile.setVerificationStatus(request.getVerificationStatus());
        profile.setUpdatedAt(Instant.now());

        if (request.getVerificationStatus() == ResponderVerificationStatus.APPROVED) {
            profile.setAuthorizedAt(Instant.now());
        }

        ResponderProfile updated = responderProfileRepository.save(profile);
        return mapToResponse(updated);
    }

    public ResponderEligibilityResponse checkEligibility(Long userId) {
        ResponderProfile profile = responderProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Perfil não encontrado para o utilizador: " + userId));

        User user = profile.getUser();

        boolean eligible = user.getStatus() == br.com.stormaid.enums.UserStatus.ACTIVE &&
                profile.getVerificationStatus() == ResponderVerificationStatus.APPROVED;

        ResponderEligibilityResponse response = new ResponderEligibilityResponse();
        response.setUserId(user.getId());
        response.setResponderProfileId(profile.getId());
        response.setEligible(eligible);
        response.setInstitutionType(profile.getInstitutionType());
        response.setUserStatus(user.getStatus());
        response.setVerificationStatus(profile.getVerificationStatus());

        return response;
    }

    private ResponderProfileResponse mapToResponse(ResponderProfile profile) {
        ResponderProfileResponse res = new ResponderProfileResponse();
        res.setId(profile.getId());
        res.setUserId(profile.getUser().getId());
        res.setFullName(profile.getUser().getFullName());
        res.setEmail(profile.getUser().getEmail());
        res.setPhoneNumber(profile.getUser().getPhoneNumber());
        res.setBadgeNumber(profile.getBadgeNumber());
        res.setInstitutionType(profile.getInstitutionType());
        res.setVerificationStatus(profile.getVerificationStatus());
        res.setAuthorizedAt(profile.getAuthorizedAt());
        res.setCreatedAt(profile.getCreatedAt());
        if (profile.getAuthorizedBy() != null) {
            res.setAuthorizedByUserId(profile.getAuthorizedBy().getId());
        }
        return res;
    }

    public ResponderProfile getProfileByUser(User user) {
        return responderProfileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Perfil de socorrista não encontrado para o usuário atual"));
    }
}