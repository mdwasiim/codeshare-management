package com.codeshare.airline.ssim.persistence.inbound.entity;

import com.codeshare.airline.ssim.source.SsimTimeMode;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(
    name = "SSIM_INBOUND_CARRIER",
    schema = "SSIM_OPERATIONAL",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "UK_SSIM_CARRIER_FILE",
            columnNames = "FILE_ID"
        )
    },
    indexes = {
        @Index(
            name = "IDX_SSIM_CARRIER_FILE",
            columnList = "FILE_ID"
        ),
        @Index(
            name = "IDX_SSIM_CARRIER_AIRLINE",
            columnList = "AIRLINE_CODE"
        )
    }
)
@Getter
@Setter
@NoArgsConstructor
public class SsimInboundCarrier {

    /* ===================== IDENTITY ===================== */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CARRIER_ID")
    private Long id;

    /**
     * Owning inbound SSIM file
     * Exactly ONE T2 per SSIM file
     */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "FILE_ID", nullable = false, updatable = false)
    private SsimInboundFile file;

    /* ===================== SSIM FIELDS ===================== */

    /**
     * Byte 1
     * Record type – always '2'
     */
    @Column(name = "RECORD_TYPE", length = 1, nullable = false)
    private String recordType;

    /**
     * Byte 2
     * Time mode
     * U = UTC, L = Local
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "TIME_MODE", length = 1, nullable = false)
    private SsimTimeMode timeMode;

    /**
     * Bytes 3–5
     * Operating airline code
     */
    @Column(name = "AIRLINE_CODE", length = 3, nullable = false)
    private String airlineCode;

    /**
     * Bytes 6–9
     * Spare / filler
     */
    @Column(name = "SPARE_6_9", length = 4, nullable = false)
    private String spare6To9;

    /**
     * Bytes 10–16
     * Validity start date (YYMMDD or DDMMMYY depending on profile)
     * Stored raw for SSIM traceability
     */
    @Column(name = "VALIDITY_START_RAW", length = 7, nullable = false)
    private String validityStartRaw;

    /**
     * Bytes 17–23
     * Validity end date (raw)
     */
    @Column(name = "VALIDITY_END_RAW", length = 7, nullable = false)
    private String validityEndRaw;

    /**
     * Parsed validity start date (derived)
     */
    @Column(name = "VALIDITY_START_DATE")
    private LocalDate validityStartDate;

    /**
     * Parsed validity end date (derived)
     */
    @Column(name = "VALIDITY_END_DATE")
    private LocalDate validityEndDate;

    /**
     * Bytes 24–30
     * File creation date (raw)
     */
    @Column(name = "CREATION_DATE_RAW", length = 7, nullable = false)
    private String creationDateRaw;

    /**
     * Bytes 31–60
     * Airline name
     */
    @Column(name = "AIRLINE_NAME", length = 30, nullable = false)
    private String airlineName;

    /**
     * Byte 61
     * Release status
     * (P = Provisional, F = Final, etc.)
     */
    @Column(name = "RELEASE_STATUS", length = 1, nullable = false)
    private String releaseStatus;

    /**
     * Bytes 62–91
     * Creator reference
     */
    @Column(name = "CREATOR_REFERENCE", length = 30)
    private String creatorReference;

    /**
     * Bytes 92–93
     * E-ticketing indicator
     */
    @Column(name = "ETICKET_INFO", length = 2)
    private String eTicketInfo;

    /**
     * Bytes 94–99
     * Creation time (HHMMSS)
     */
    @Column(name = "CREATION_TIME_RAW", length = 6)
    private String creationTimeRaw;

    /**
     * Bytes 100–194
     * Spare / filler
     */
    @Column(name = "SPARE_100_194", length = 95, nullable = false)
    private String spare100To194;

    /**
     * Bytes 195–200
     * Record serial number
     */
    @Column(name = "RECORD_SERIAL_NUMBER", length = 6)
    private String recordSerialNumber;

    /**
     * Full raw 200-byte SSIM record
     */
    @Column(name = "RAW_RECORD", length = 200, nullable = false)
    private String rawRecord;

    /* ===================== AUDIT ===================== */

    /**
     * Parsed timestamp
     */
    @Column(name = "PARSED_AT")
    private Instant parsedAt;
}
