package br.com.stormaid.repository;

import br.com.stormaid.entity.ResponderProfile;
import br.com.stormaid.enums.ResponderInstitutionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResponderProfileRepository extends JpaRepository<ResponderProfile, Long> {
    Optional<ResponderProfile> findByUserId(Long userId);
    boolean existsByInstitutionTypeAndBadgeNumber(ResponderInstitutionType institutionType, String badgeNumber);
}