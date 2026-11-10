package br.com.stormaid.embeddable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Address {

    @Column(name = "STREET", length = 150)
    private String street;

    @Column(name = "ADDRESS_NUMBER", length = 20)
    private String number;

    @Column(name = "COMPLEMENT", length = 100)
    private String complement;

    @Column(name = "NEIGHBORHOOD", length = 100)
    private String neighborhood;

    @Column(name = "POSTAL_CODE", length = 8)
    private String postalCode;

    @Column(name = "CITY", length = 100)
    private String city;

    @Column(name = "STATE", length = 2)
    private String state;

    @Column(name = "REFERENCE_POINT", length = 250)
    private String referencePoint;
}