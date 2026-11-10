package br.com.stormaid.repository;

import br.com.stormaid.entity.CitizenProfile;
import br.com.stormaid.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CitizenProfileRepository extends JpaRepository<CitizenProfile, Long> {
    Optional<CitizenProfile> findByUser(User user);
}