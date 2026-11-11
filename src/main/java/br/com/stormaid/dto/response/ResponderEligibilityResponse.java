package br.com.stormaid.dto.response;

import br.com.stormaid.enums.ResponderInstitutionType;
import br.com.stormaid.enums.ResponderVerificationStatus;
import br.com.stormaid.enums.UserStatus;
import lombok.Data;

@Data
public class ResponderEligibilityResponse {
    private Long userId;
    private Long responderProfileId;
    private boolean eligible;
    private ResponderInstitutionType institutionType;
    private UserStatus userStatus;
    private ResponderVerificationStatus verificationStatus;
}