package br.com.stormaid.entity;

import br.com.stormaid.enums.CommunityRiskCategory;
import br.com.stormaid.enums.CommunityRiskStatus;
import br.com.stormaid.enums.LocationSource;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "TB_STORMAID_COMMUNITY_RISK_REPORT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommunityRiskReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RISK_REPORT_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "REQUESTER_USER_ID", nullable = false)
    private User requester;

    @Enumerated(EnumType.STRING)
    @Column(name = "RISK_CATEGORY", length = 35, nullable = false)
    private CommunityRiskCategory category;

    @Column(name = "DESCRIPTION", length = 2000, nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "LOCATION_SOURCE", length = 30)
    private LocationSource locationSource;

    @Column(name = "LATITUDE", precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(name = "LONGITUDE", precision = 11, scale = 7)
    private BigDecimal longitude;

    @Column(name = "ADDRESS_TEXT", length = 300)
    private String addressText;

    @Column(name = "REFERENCE_POINT", length = 250)
    private String referencePoint;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", length = 25, nullable = false)
    private CommunityRiskStatus status;

    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "UPDATED_AT", nullable = false)
    private Instant updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        if (this.status == null) {
            this.status = CommunityRiskStatus.REPORTED;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = Instant.now();
    }
}