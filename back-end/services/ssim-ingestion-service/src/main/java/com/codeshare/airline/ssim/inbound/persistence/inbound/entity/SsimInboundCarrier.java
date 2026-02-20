package com.codeshare.airline.ssim.inbound.persistence.inbound.entity;


import com.codeshare.airline.persistence.persistence.entity.CSMDataAbstractEntity;
import com.codeshare.airline.ssim.inbound.domain.enums.SsimTimeMode;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(
        name = "SSIM_INBOUND_CARRIER",
        schema = "SSIM_OPERATIONAL",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_SSIM_INBOUND_CARRIER_FILE_ID",
                        columnNames = "FILE_ID"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class SsimInboundCarrier extends CSMDataAbstractEntity {

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "FILE_ID",
            nullable = false,
            updatable = false,
            foreignKey = @ForeignKey(name = "FK_SSIM_CARRIER_FILE")
    )
    private SsimInboundFile inboundFile;

    // Byte 1
    @Column(name = "RECORD_TYPE", length = 1)
    private String recordType;

    // Byte 2
    @Enumerated(EnumType.STRING)
    @Column(name = "TIME_MODE", length = 1)
    private SsimTimeMode timeMode;

    // Bytes 3–5
    @Column(name = "AIRLINE_CODE", length = 3)
    private String airlineCode;

    // Bytes 6–10
    @Column(name = "SPARE_6_10", length = 5)
    private String spare6To10;

    // Bytes 11–13
    @Column(name = "SEASON", length = 3)
    private String season;

    // Byte 14
    @Column(name = "SPARE_14", length = 1)
    private String spare14;

    // Bytes 15–21
    @Column(name = "VALIDITY_START_RAW", length = 7)
    private String validityStartRaw;

    // Bytes 22–28
    @Column(name = "VALIDITY_END_RAW", length = 7)
    private String validityEndRaw;

    // Bytes 29–35
    @Column(name = "CREATION_DATE_RAW", length = 7)
    private String creationDateRaw;

    // Bytes 36–64
    @Column(name = "TITLE_OF_DATA", length = 29)
    private String titleOfData;

    // Bytes 65–71
    @Column(name = "RELEASE_DATE_RAW", length = 7)
    private String releaseDateRaw;

    // Byte 72
    @Column(name = "SCHEDULE_STATUS", length = 1)
    private String scheduleStatus;

    // Bytes 73–107
    @Column(name = "CREATOR_REFERENCE", length = 35)
    private String creatorReference;

    // Byte 108
    @Column(name = "DUPLICATE_DESIGNATOR_MARKER", length = 1)
    private String duplicateDesignatorMarker;

    // Bytes 109–169
    @Column(name = "GENERAL_INFORMATION", length = 61)
    private String generalInformation;

    // Bytes 170–188
    @Column(name = "INFLIGHT_SERVICE_INFO", length = 19)
    private String inflightServiceInfo;

    // Bytes 189–190
    @Column(name = "ELECTRONIC_TICKETING_INFO", length = 2)
    private String electronicTicketingInfo;

    // Bytes 191–194
    @Column(name = "CREATION_TIME_RAW", length = 4)
    private String creationTimeRaw;

    // Bytes 195–200
    @Column(name = "RECORD_SERIAL_NUMBER", length = 6)
    private String recordSerialNumber;

    @Column(name = "RAW_RECORD", length = 200)
    private String rawRecord;

    @Column(name = "PARSED_TIMESTAMP")
    private Instant parsedTimestamp;
}
