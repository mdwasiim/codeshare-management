package com.codeshare.airline.processor.model.raw;

import com.codeshare.airline.persistence.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "ssim_inbound_r3_segment_data")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@ToString(callSuper = true)
public class SsimR3SegmentDataRecord extends CSMDataAbstractEntity {

    /* ===================== RELATIONSHIP ===================== */

    @ManyToOne(optional = false)
    @JoinColumn(name = "flight_leg_id", nullable = false)
    private SsimR3FlightLegRecord flightLeg;

    /* ===================== SSIM FIELDS ===================== */

    // Byte 1
    @Column(name = "record_type", length = 1, nullable = false)
    private String recordType; // '4'

    // Not part of SSIM — system ordering
    @Column(name = "sequence_number", nullable = false)
    private Integer sequence; // order matters

    // Bytes 10–12
    @Column(name = "board_point", length = 3)
    private String boardPoint;

    // Bytes 13–15
    @Column(name = "off_point", length = 3)
    private String offPoint;

    // Bytes 16–18
    @Column(name = "data_element_identifier", length = 3, nullable = false)
    private String dataElementIdentifier;

    // Bytes 19–78 (exactly 60 chars)
    @Column(name = "data_element_value", length = 60, nullable = false)
    private String value;
}
