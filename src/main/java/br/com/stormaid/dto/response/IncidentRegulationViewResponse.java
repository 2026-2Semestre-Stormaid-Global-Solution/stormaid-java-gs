package br.com.stormaid.dto.response;

import br.com.stormaid.enums.ChannelType;
import br.com.stormaid.enums.EmergencyCategory;
import br.com.stormaid.enums.RecipientType;
import br.com.stormaid.enums.TranscriptionStatus;
import lombok.Data;

@Data
public class IncidentRegulationViewResponse {
    private Long incidentId;
    private RecipientType recipientType;
    private ChannelType channelType;
    private EmergencyCategory emergencyCategory;
    private LocationDto location;
    private String report;
    private TranscriptionStatus transcriptionStatus;
    private VictimNeedsResponse victimNeeds;

    @Data
    public static class LocationDto {
        private Double latitude;
        private Double longitude;
        private String addressText;
    }

    @Data
    public static class VictimNeedsResponse {
        private String mobilityStatus;
        private Boolean hasVisualImpairment;
        private Boolean hasHearingImpairment;
        private Boolean dependsOnEssentialEquipment;
        private String essentialEquipmentDescription;
        private String accessibilityNotes;
    }
}