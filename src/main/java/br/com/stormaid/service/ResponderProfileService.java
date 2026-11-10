package br.com.stormaid.service;

import br.com.stormaid.entity.ResponderProfile;
import br.com.stormaid.entity.User;
import br.com.stormaid.enums.ResponderVerificationStatus;
import br.com.stormaid.repository.ResponderProfileRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class ResponderProfileService {

    private final ResponderProfileRepository responderProfileRepository;

    public ResponderProfileService(ResponderProfileRepository responderProfileRepository) {
        this.responderProfileRepository = responderProfileRepository;
    }

    public ResponderProfile getProfileByUser(User user) {
        return responderProfileRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Perfil de socorrista não encontrado para o utilizador autenticado"));
    }

    public Page<ResponderProfile> listAll(Pageable pageable) {
        return responderProfileRepository.findAll(pageable);
    }

    public ResponderProfile findById(Long id) {
        return responderProfileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Perfil de socorrista não encontrado com o ID: " + id));
    }

    @Transactional
    public ResponderProfile updateAuthorizationStatus(Long id, ResponderVerificationStatus status, User authorizer) {
        ResponderProfile profile = findById(id);
        profile.setVerificationStatus(status);

        if (status == ResponderVerificationStatus.APPROVED) {
            profile.setAuthorizedBy(authorizer);
            profile.setAuthorizedAt(Instant.now());
        }

        profile.setUpdatedAt(Instant.now());
        return responderProfileRepository.save(profile);
    }
}