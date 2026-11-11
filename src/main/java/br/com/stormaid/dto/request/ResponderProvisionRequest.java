package br.com.stormaid.dto.request;

import br.com.stormaid.enums.ResponderInstitutionType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ResponderProvisionRequest {
    @NotBlank
    @Size(min = 3, max = 150)
    private String fullName;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String phoneNumber;

    @NotBlank
    @Size(min = 6, max = 100)
    private String temporaryPassword;

    @NotBlank
    @Size(min = 3, max = 30)
    private String badgeNumber;

    @NotNull
    private ResponderInstitutionType institutionType;
}