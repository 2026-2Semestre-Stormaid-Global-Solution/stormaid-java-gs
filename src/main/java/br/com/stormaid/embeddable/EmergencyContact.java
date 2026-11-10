package br.com.stormaid.embeddable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class EmergencyContact {

    @Column(name = "EMERGENCY_CONTACT_NAME", length = 120)
    private String name;

    @Column(name = "EMERGENCY_CONTACT_PHONE", length = 20)
    private String phoneNumber;

    @Column(name = "EMERGENCY_CONTACT_RELATIONSHIP", length = 40)
    private String relationship;
}