package br.com.stormaid.entity;

import br.com.stormaid.enums.ResponderInstitutionType;
import br.com.stormaid.enums.ResponderVerificationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "TB_STORMAID_RESPONDER_PROFILE", uniqueConstraints = {
        @UniqueConstraint(name = "UK_STORMAID_RESPONDER_USER", columnNames = "USER_ID"),
        @UniqueConstraint(name = "UK_STORMAID_RESPONDER_BADGE", columnNames = {"INSTITUTION_TYPE", "BADGE_NUMBER"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponderProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RESPONDER_PROFILE_ID")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "USER_ID", nullable = false, unique = true)
    private User user;

    @Column(name = "BADGE_NUMBER", length = 30, nullable = false)
    private String badgeNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "INSTITUTION_TYPE", length = 40, nullable = false)
    private ResponderInstitutionType institutionType;

    @Enumerated(EnumType.STRING)
    @Column(name = "VERIFICATION_STATUS", length = 20, nullable = false)
    private ResponderVerificationStatus verificationStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AUTHORIZED_BY_USER_ID")
    private User authorizedBy;

    @Column(name = "AUTHORIZED_AT")
    private Instant authorizedAt;

    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "UPDATED_AT", nullable = false)
    private Instant updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        if (this.verificationStatus == null) {
            this.verificationStatus = ResponderVerificationStatus.PENDING;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = Instant.now();
    }
}