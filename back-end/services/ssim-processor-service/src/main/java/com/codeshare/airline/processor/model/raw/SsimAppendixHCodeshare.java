package com.codeshare.airline.processor.model.raw;

import com.codeshare.airline.persistence.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "ssim_appendix_h_codeshare",
        indexes = {
                @Index(name = "idx_ssim_apph_operating_leg", columnList = "operating_leg_id")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class SsimAppendixHCodeshare extends CSMDataAbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "operating_leg_id", nullable = false)
    private SsimR3FlightLegRecord operatingLeg;

    @Column(name = "marketing_carrier", nullable = false, length = 3)
    private String marketingCarrier;

    @Column(name = "marketing_flight_number", nullable = false, length = 6)
    private String marketingFlightNumber;

    @Column(name = "priority_order")
    private Integer priorityOrder;

    @Column(name = "disclosure_indicator", length = 1)
    private String disclosureIndicator;

    @Column(name = "codeshare_type", length = 2)
    private String codeshareType;
}
