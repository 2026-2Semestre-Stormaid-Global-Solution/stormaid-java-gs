package br.com.stormaid.repository;

import br.com.stormaid.entity.CommunityRiskReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommunityRiskReportRepository extends JpaRepository<CommunityRiskReport, Long> {
    Page<CommunityRiskReport> findByRequesterId(Long requesterId, Pageable pageable);
    Optional<CommunityRiskReport> findByIdAndRequesterId(Long id, Long requesterId);
}