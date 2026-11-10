package br.com.stormaid.entity;

import br.com.stormaid.embeddable.Address;
import br.com.stormaid.embeddable.EmergencyContact;
import br.com.stormaid.embeddable.VulnerabilityInfo;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "TB_STORMAID_CITIZEN_PROFILE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CitizenProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CITIZEN_PROFILE_ID")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "USER_ID", nullable = false, unique = true)
    private User user;

    @Embedded
    private Address address;

    @Embedded
    private VulnerabilityInfo vulnerability;

    @Embedded
    private EmergencyContact emergencyContact;

    @Column(name = "UPDATED_AT")
    private Instant updatedAt;

    @PrePersist
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = Instant.now();
    }
}