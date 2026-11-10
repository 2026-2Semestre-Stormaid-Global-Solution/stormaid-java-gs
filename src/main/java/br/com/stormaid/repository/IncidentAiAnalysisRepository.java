package br.com.stormaid.repository;

import br.com.stormaid.entity.IncidentAiAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncidentAiAnalysisRepository extends JpaRepository<IncidentAiAnalysis, Long> {
    List<IncidentAiAnalysis> findByIncidentIdOrderByCreatedAtDesc(Long incidentId);
}
