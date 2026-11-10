package br.com.stormaid.entity;

import br.com.stormaid.enums.AiAnalysisStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "TB_STORMAID_INCIDENT_AI_ANALYSIS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IncidentAiAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AI_ANALYSIS_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "INCIDENT_ID", nullable = false)
    private IncidentReport incident;

    @Enumerated(EnumType.STRING)
    @Column(name = "ANALYSIS_STATUS", length = 20, nullable = false)
    private AiAnalysisStatus status;

    @Lob
    @Column(name = "SUMMARY")
    private String summary;

    @Lob
    @Column(name = "EXTRACTED_INFORMATION_JSON")
    private String extractedInformationJson;

    @Lob
    @Column(name = "SOURCE_REFERENCES_JSON")
    private String sourceReferencesJson;

    @Column(name = "MODEL_NAME", length = 100)
    private String modelName;

    @Column(name = "ERROR_CODE", length = 50)
    private String errorCode;

    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "COMPLETED_AT")
    private Instant completedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = Instant.now();
        if (this.status == null) {
            this.status = AiAnalysisStatus.PENDING;
        }
    }
}