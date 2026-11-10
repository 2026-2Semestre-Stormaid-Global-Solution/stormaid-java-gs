package br.com.stormaid.repository;

import br.com.stormaid.entity.IncidentReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IncidentReportRepository extends JpaRepository<IncidentReport, Long> {
    Page<IncidentReport> findByRequesterId(Long requesterId, Pageable pageable);
    Optional<IncidentReport> findByRequesterIdAndIdempotencyKey(Long requesterId, String idempotencyKey);
}