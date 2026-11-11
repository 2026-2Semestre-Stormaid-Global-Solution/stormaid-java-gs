package br.com.stormaid.dto.event;

import br.com.stormaid.embeddable.IncidentLocation;
import br.com.stormaid.enums.ChannelType;
import br.com.stormaid.enums.EmergencyCategory;
import br.com.stormaid.enums.IncidentPublicStatus;
import br.com.stormaid.enums.RecipientType;

import java.time.Instant;

public record IncidentCreatedEvent(
        Long incidentId,
        Long requesterUserId,
        RecipientType recipientType,
        ChannelType channelType,
        EmergencyCategory emergencyCategory,
        IncidentPublicStatus publicStatus,
        IncidentLocation location,
        Instant createdAt
) {}