package com.codeshare.airline.ssim.persistence.inbound.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(
    name = "SSIM_INBOUND_SEGMENT_DEI",
    schema = "SSIM_OPERATIONAL",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "UK_SSIM_SEGMENT_DEI_NK",
            columnNames = {
                "FLIGHT_LEG_ID",
                "OPERATIONAL_VARIATION",
                "BOARD_POINT",
                "OFF_POINT",
                "DEI_NUMBER"
            }
        )
    },
    indexes = {
        @Index(
            name = "IDX_SSIM_SEGMENT_DEI_FLIGHT",
            columnList = "FLIGHT_LEG_ID"
        ),
        @Index(
            name = "IDX_SSIM_SEGMENT_DEI_ROUTE",
            columnList = "BOARD_POINT,OFF_POINT"
        )
    }
)
@Getter
@Setter
@NoArgsConstructor
public class SsimInboundSegmentDei {

    /* ===================== IDENTITY ===================== */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SEGMENT_DEI_ID")
    private Long id;

    /**
     * Owning inbound flight leg (T3)
     */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(
        name = "FLIGHT_LEG_ID",
        nullable = false,
        updatable = false
    )
    private SsimInboundFlightLeg flightLeg;

    /* ===================== SSIM FIELDS ===================== */

    /**
     * Byte 1
     * Record type – always '4'
     */
    @Column(name = "RECORD_TYPE", length = 1, nullable = false)
    private String recordType;

    /**
     * Byte 2
     * Operational variation indicator
     */
    @Column(name = "OPERATIONAL_VARIATION", length = 1, nullable = false)
    private String operationalVariation;

    /**
     * Bytes 3–5
     * Airline code (marketing / operating)
     */
    @Column(name = "AIRLINE_CODE", length = 3, nullable = false)
    private String airlineCode;

    /**
     * Bytes 6–9
     */
    @Column(name = "FLIGHT_NUMBER", length = 4, nullable = false)
    private String flightNumber;

    /**
     * Bytes 10–11
     */
    @Column(name = "OPERATIONAL_SUFFIX", length = 2, nullable = false)
    private String operationalSuffix;

    /**
     * Bytes 12–14
     * Board point
     */
    @Column(name = "BOARD_POINT", length = 3, nullable = false)
    private String boardPoint;

    /**
     * Bytes 15–17
     * Off point
     */
    @Column(name = "OFF_POINT", length = 3, nullable = false)
    private String offPoint;

    /**
     * Bytes 18–20
     * DEI number (e.g. 010 = codeshare)
     */
    @Column(name = "DEI_NUMBER", length = 3, nullable = false)
    private String deiNumber;

    /**
     * Bytes 21–194
     * DEI payload (raw)
     */
    @Column(name = "DEI_PAYLOAD", length = 174, nullable = false)
    private String deiPayload;

    /**
     * Bytes 195–200
     */
    @Column(name = "RECORD_SERIAL_NUMBER", length = 6)
    private String recordSerialNumber;

    /* ===================== RAW & AUDIT ===================== */

    /**
     * Full raw 200-byte SSIM line
     */
    @Column(name = "RAW_RECORD", length = 200, nullable = false)
    private String rawRecord;

    /**
     * Parsed timestamp
     */
    @Column(name = "PARSED_AT")
    private Instant parsedAt;
}
