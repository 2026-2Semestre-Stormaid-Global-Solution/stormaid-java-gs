package br.com.stormaid.entity;

import br.com.stormaid.embeddable.IncidentLocation;
import br.com.stormaid.enums.ChannelType;
import br.com.stormaid.enums.EmergencyCategory;
import br.com.stormaid.enums.IncidentPublicStatus;
import br.com.stormaid.enums.RecipientType;
import br.com.stormaid.enums.TranscriptionStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "TB_STORMAID_INCIDENT_REPORT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IncidentReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "INCIDENT_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "REQUESTER_USER_ID", nullable = false)
    private User requester;

    @Enumerated(EnumType.STRING)
    @Column(name = "RECIPIENT_TYPE", length = 20, nullable = false)
    private RecipientType recipientType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DEPENDENT_ID")
    private Dependent dependent;

    @Column(name = "THIRD_PARTY_NAME", length = 150)
    private String thirdPartyName;

    @Column(name = "REQUESTER_ON_SITE")
    private Boolean requesterOnSite;

    @Enumerated(EnumType.STRING)
    @Column(name = "CHANNEL_TYPE", length = 20, nullable = false)
    private ChannelType channelType;

    @Enumerated(EnumType.STRING)
    @Column(name = "EMERGENCY_CATEGORY", length = 35, nullable = false)
    private EmergencyCategory emergencyCategory;

    @Lob
    @Column(name = "TEXT_DESCRIPTION")
    private String textDescription;

    @Column(name = "AUDIO_STORAGE_KEY", length = 300)
    private String audioStorageKey;

    @Lob
    @Column(name = "AUDIO_TRANSCRIPTION")
    private String audioTranscription;

    @Enumerated(EnumType.STRING)
    @Column(name = "TRANSCRIPTION_STATUS", length = 20, nullable = false)
    private TranscriptionStatus transcriptionStatus;

    @Embedded
    private IncidentLocation location;

    @Enumerated(EnumType.STRING)
    @Column(name = "PUBLIC_STATUS", length = 30, nullable = false)
    private IncidentPublicStatus publicStatus;

    @Column(name = "STATUS_VERSION", nullable = false)
    private Long statusVersion;

    @Column(name = "IDEMPOTENCY_KEY", length = 100)
    private String idempotencyKey;

    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "UPDATED_AT", nullable = false)
    private Instant updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        if (this.publicStatus == null) {
            this.publicStatus = IncidentPublicStatus.RECEIVED;
        }
        if (this.statusVersion == null) {
            this.statusVersion = 1L;
        }
        if (this.transcriptionStatus == null) {
            this.transcriptionStatus = TranscriptionStatus.NOT_APPLICABLE;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = Instant.now();
    }
}