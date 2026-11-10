package br.com.stormaid.dto.request;

import br.com.stormaid.embeddable.IncidentLocation;
import br.com.stormaid.enums.ChannelType;
import br.com.stormaid.enums.EmergencyCategory;
import br.com.stormaid.enums.RecipientType;
import jakarta.validation.constraints.NotNull;

public record IncidentCreateRequest(
        @NotNull RecipientType recipientType,
        Long dependentId,
        String thirdPartyName,
        Boolean requesterOnSite,
        @NotNull ChannelType channelType,
        @NotNull EmergencyCategory emergencyCategory,
        String textDescription,
        @NotNull IncidentLocation location
) {}