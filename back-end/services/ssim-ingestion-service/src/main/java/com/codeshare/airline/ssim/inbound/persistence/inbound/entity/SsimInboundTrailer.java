package com.codeshare.airline.ssim.inbound.persistence.inbound.entity;

import com.codeshare.airline.persistence.persistence.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(
        name = "SSIM_INBOUND_TRAILER",
        schema = "SSIM_OPERATIONAL",
        indexes = {
                @Index(
                        name = "IDX_SSIM_IN_TRAILER_FILE_ID",
                        columnList = "FILE_ID"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class SsimInboundTrailer extends CSMDataAbstractEntity {

    /* =======================================================
       RELATIONSHIP
       ======================================================= */

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "FILE_ID",
            nullable = false,
            updatable = false,
            foreignKey = @ForeignKey(name = "FK_SSIM_TRAILER_FILE")
    )
    private SsimInboundFile inboundFile;


    /* =======================================================
       IATA T5 STRUCTURE (200 BYTES)
       ======================================================= */

    // Byte 1
    @Column(name = "RECORD_TYPE", length = 1)
    private String recordType;

    // Byte 2
    @Column(name = "SPARE_BYTE_2", length = 1)
    private String spareByte2;

    // Bytes 3–5
    @Column(name = "AIRLINE_DESIGNATOR", length = 3)
    private String airlineDesignator;

    // Bytes 6–12
    @Column(name = "RELEASE_DATE_RAW", length = 7)
    private String releaseDateRaw;

    // Bytes 13–187
    @Column(name = "SPARE_13_187", length = 175)
    private String spare13To187;

    // Bytes 188–193
    @Column(name = "SERIAL_CHECK_REFERENCE", length = 6)
    private String serialCheckReference;

    // Byte 194
    @Column(name = "CONTINUATION_END_CODE", length = 1)
    private String continuationEndCode;

    // Bytes 195–200
    @Column(name = "RECORD_SERIAL_NUMBER", length = 6)
    private String recordSerialNumber;


    /* =======================================================
       RAW & AUDIT
       ======================================================= */

    @Column(name = "RAW_RECORD", length = 200)
    private String rawRecord;

    @Column(name = "PARSED_AT")
    private Instant parsedAt;
}
