package com.codeshare.airline.schedule.ingestion.persistence.entities.ssim;

import com.codeshare.airline.data.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "ssim_segment_dei",
        indexes = {
                @Index(name = "idx_ssim_dei_flight", columnList = "flight_id"),
                @Index(name = "idx_ssim_dei_route", columnList = "board_point,off_point"),
                @Index(name = "idx_ssim_dei_code", columnList = "data_element_identifier"),
                @Index(name = "idx_ssim_dei_flight_code", columnList = "flight_id,data_element_identifier"),
                @Index(name = "idx_ssim_dei_segment_code", columnList = "board_point,off_point,data_element_identifier")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class SsimDataElementEntity extends CSMDataAbstractEntity {

    /* =======================================================
       RELATIONSHIP
       ======================================================= */

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "flight_id",
            nullable = false,
            updatable = false,
            foreignKey = @ForeignKey(name = "fk_ssim_dei_flight_leg")
    )
    private SsimFlightEntity flight;

    /* =======================================================
       SSIM RECORD TYPE 4 (DEI) – 200 BYTES
       ======================================================= */

    // SSIM DEI: Byte 1
    @Column(name = "record_type", length = 1)
    private String recordType;

    // SSIM DEI: Byte 2
    @Column(name = "operational_suffix", length = 1)
    private String operationalSuffix;

    // SSIM DEI: Bytes 3–5
    @Column(name = "airline_code", length = 3)
    private String airlineCode;

    // SSIM DEI: Bytes 6–9
    @Column(name = "flight_number", length = 4)
    private String flightNumber;

    // SSIM DEI: Bytes 10–11
    @Column(name = "itinerary_variation_identifier", length = 2)
    private String itineraryVariationIdentifier;

    // SSIM DEI: Bytes 12–13
    @Column(name = "leg_sequence_number", length = 2)
    private String legSequenceNumber;

    // SSIM DEI: Byte 14
    @Column(name = "service_type", length = 1)
    private String serviceType;

    /* =======================================================
       15–28 STRUCTURE
       ======================================================= */

    // SSIM DEI: Bytes 15–27
    @Column(name = "spare_15_27", length = 13)
    private String spare15To27;

    // SSIM DEI: Byte 28
    @Column(name = "itinerary_variation_overflow", length = 1)
    private String itineraryVariationOverflow;

    /* =======================================================
       29–39 SEGMENT IDENTIFIERS
       ======================================================= */

    // SSIM DEI: Byte 29
    @Column(name = "board_point_indicator", length = 1)
    private String boardPointIndicator;

    // SSIM DEI: Byte 30
    @Column(name = "off_point_indicator", length = 1)
    private String offPointIndicator;

    // SSIM DEI: Bytes 31–33
    @Column(name = "data_element_identifier", length = 3)
    private String dataElementIdentifier;

    // SSIM DEI: Bytes 34–36
    @Column(name = "board_point", length = 3)
    private String boardPoint;

    // SSIM DEI: Bytes 37–39
    @Column(name = "off_point", length = 3)
    private String offPoint;

    /* =======================================================
       40–194 DATA PAYLOAD
       ======================================================= */

    // SSIM DEI: Bytes 40–194
    @Column(name = "dei_data", length = 155)
    private String deiData;

    /* =======================================================
       195–200 FOOTER
       ======================================================= */

    // SSIM DEI: Bytes 195–200
    @Column(name = "record_serial_number", length = 6)
    private String recordSerialNumber;

}
