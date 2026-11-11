package br.com.stormaid.dto.request;

import br.com.stormaid.enums.ResponderVerificationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ResponderAuthorizationRequest {
    @NotNull
    private ResponderVerificationStatus verificationStatus;
}