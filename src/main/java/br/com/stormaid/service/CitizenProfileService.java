package br.com.stormaid.service;

import br.com.stormaid.entity.CitizenProfile;
import br.com.stormaid.entity.User;
import br.com.stormaid.repository.CitizenProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CitizenProfileService {

    private final CitizenProfileRepository citizenProfileRepository;

    public CitizenProfileService(CitizenProfileRepository citizenProfileRepository) {
        this.citizenProfileRepository = citizenProfileRepository;
    }

    public CitizenProfile getProfileByUser(User user) {
        return citizenProfileRepository.findByUser(user)
                .orElseGet(() -> {
                    CitizenProfile newProfile = new CitizenProfile();
                    newProfile.setUser(user);
                    return citizenProfileRepository.save(newProfile);
                });
    }

    @Transactional
    public CitizenProfile updateProfile(User user, CitizenProfile updatedData) {
        CitizenProfile profile = getProfileByUser(user);
        profile.setAddress(updatedData.getAddress());
        profile.setVulnerability(updatedData.getVulnerability());
        profile.setEmergencyContact(updatedData.getEmergencyContact());

        return citizenProfileRepository.save(profile);
    }
}