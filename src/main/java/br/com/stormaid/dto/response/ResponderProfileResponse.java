package br.com.stormaid.dto.response;

import br.com.stormaid.enums.ResponderInstitutionType;
import br.com.stormaid.enums.ResponderVerificationStatus;
import lombok.Data;
import java.time.Instant;

@Data
public class ResponderProfileResponse {
    private Long id;
    private Long userId;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String badgeNumber;
    private ResponderInstitutionType institutionType;
    private ResponderVerificationStatus verificationStatus;
    private Long authorizedByUserId;
    private Instant authorizedAt;
    private Instant createdAt;
}