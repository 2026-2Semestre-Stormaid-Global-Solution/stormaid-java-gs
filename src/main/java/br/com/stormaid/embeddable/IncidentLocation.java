package br.com.stormaid.embeddable;

import br.com.stormaid.enums.LocationSource;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Embeddable
@Getter
@Setter
public class IncidentLocation {

    @Enumerated(EnumType.STRING)
    @Column(name = "LOCATION_SOURCE", length = 30)
    private LocationSource source;

    @Column(name = "LATITUDE", precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(name = "LONGITUDE", precision = 11, scale = 7)
    private BigDecimal longitude;

    @Column(name = "ADDRESS_TEXT", length = 300)
    private String addressText;

    @Column(name = "REFERENCE_POINT", length = 250)
    private String referencePoint;

    @Column(name = "ACCURACY_METERS", precision = 9, scale = 2)
    private BigDecimal accuracyMeters;

    @Column(name = "LOCATION_CAPTURED_AT")
    private Instant locationCapturedAt;
}