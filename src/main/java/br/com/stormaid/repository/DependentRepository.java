package br.com.stormaid.repository;

import br.com.stormaid.entity.Dependent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DependentRepository extends JpaRepository<Dependent, Long> {
    Page<Dependent> findByGuardianIdAndActiveTrue(Long guardianId, Pageable pageable);
    Optional<Dependent> findByIdAndGuardianId(Long id, Long guardianId);
}