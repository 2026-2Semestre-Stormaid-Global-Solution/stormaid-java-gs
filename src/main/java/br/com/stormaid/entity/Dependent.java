package br.com.stormaid.entity;

import br.com.stormaid.embeddable.Address;
import br.com.stormaid.embeddable.VulnerabilityInfo;
import br.com.stormaid.enums.RelationshipType;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "TB_STORMAID_DEPENDENT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Dependent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DEPENDENT_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "GUARDIAN_USER_ID", nullable = false)
    private User guardian;

    @Column(name = "FULL_NAME", length = 150, nullable = false)
    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(name = "RELATIONSHIP", length = 35, nullable = false)
    private RelationshipType relationship;

    @Column(name = "BIRTH_DATE")
    private LocalDate birthDate;

    @Column(name = "SAME_ADDRESS_AS_GUARDIAN", nullable = false)
    private Boolean sameAddressAsGuardian;

    @Embedded
    private Address address;

    @Embedded
    private VulnerabilityInfo vulnerability;

    @Column(name = "ACTIVE", nullable = false)
    private Boolean active;

    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "UPDATED_AT", nullable = false)
    private Instant updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        if (this.sameAddressAsGuardian == null) {
            this.sameAddressAsGuardian = true;
        }
        if (this.active == null) {
            this.active = true;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = Instant.now();
    }
}