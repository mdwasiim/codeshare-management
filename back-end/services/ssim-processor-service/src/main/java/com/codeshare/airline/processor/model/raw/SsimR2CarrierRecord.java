package com.codeshare.airline.processor.entities.raw;

import com.codeshare.airline.persistence.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "ssim_inbound_r2_carrier")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@ToString(callSuper = true)
public class SsimR2CarrierRecord extends CSMDataAbstractEntity {

    /* ===================== RELATIONSHIP ===================== */

    @ManyToOne(optional = false)
    @JoinColumn(name = "dataset_id", nullable = false)
    private SsimInboundDatasetMetadata dataset;

    /* ===================== SSIM FIELDS ===================== */

    // Byte 1
    @Column(name = "record_type", length = 1, nullable = false)
    private String recordType; // '2'

    // Bytes 2–4
    @Column(name = "airline_designator", length = 3, nullable = false)
    private String airlineDesignator;

    // Bytes 5–7
    @Column(name = "airline_numeric_code", length = 3)
    private String airlineNumericCode;

    // Bytes 8–27
    @Column(name = "airline_name", length = 20)
    private String airlineName;

    // Bytes 28–29
    @Column(name = "country_code", length = 2)
    private String countryCode;

    // Bytes 30–31
    @Column(name = "currency_code", length = 2)
    private String currencyCode;

    // Bytes 32–36
    @Column(name = "icao_designator", length = 5)
    private String icaoDesignator;

    // Byte 37
    @Column(name = "duplicate_designator_marker", length = 1)
    private String duplicateDesignatorMarker;

    // Bytes 38–78
    @Column(name = "remarks", length = 41)
    private String remarks;
}


