package com.codeshare.airline.inbound.entities.ssim;

import com.codeshare.airline.core.enums.schedule.MessageType;
import com.codeshare.airline.inbound.domain.enums.ProcessingStatus;
import com.codeshare.airline.inbound.domain.enums.ScheduleProfile;
import com.codeshare.airline.inbound.domain.enums.SourceType;
import com.codeshare.airline.inbound.domain.enums.TimeMode;
import com.codeshare.airline.inbound.entities.converter.TimeModeCodeConverter;
import com.codeshare.airline.data.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "ssim_file",
        indexes = {
                @Index(name = "idx_ssim_file_airline", columnList = "airline_code"),
                @Index(name = "idx_ssim_file_received", columnList = "received_timestamp"),
                @Index(name = "idx_ssim_file_status", columnList = "processing_status"),
                @Index(name = "idx_ssim_file_profile", columnList = "ssim_profile"),
                @Index(name = "idx_ssim_file_load_id", columnList = "load_id"),
                @Index(name = "idx_ssim_file_airline_checksum", columnList = "airline_code,checksum")
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_ssim_file_airline_checksum",
                        columnNames = {"airline_code", "checksum"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class SsimFileMetaDataEntity extends CSMDataAbstractEntity {

    @OneToOne(
            mappedBy = "file",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    private SsimCarrierEntity carrier;

    @OneToOne(mappedBy = "file", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private SsimHeaderEntity header;

    @OneToOne(mappedBy = "file", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private SsimTrailerEntity trailer;

    /* =======================================================
       INGESTION CORRELATION
       ======================================================= */

    @Column(name = "file_id", nullable = false, unique = true, updatable = false)
    private UUID fileId;

    @Column(name = "load_id")
    private UUID loadId;

    /* =======================================================
       AIRLINE OWNERSHIP
       ======================================================= */

    @Column(name = "airline_code", length = 3)
    private String airlineCode;

    /* =======================================================
       FILE ORIGIN
       ======================================================= */

    @Column(name = "file_name", length = 255)
    private String fileName;

    @Enumerated(EnumType.STRING)
    @Column(name = "source_type", length = 20)
    private SourceType sourceType;

    @Enumerated(EnumType.STRING)
    @Column(name = "message_type", length = 10, nullable = false)
    private MessageType messageType;
    /* =======================================================
       FILE CHARACTERISTICS
       ======================================================= */

    @Column(name = "file_size_bytes")
    private Long fileSizeBytes;

    @Column(name = "total_record_count")
    private Integer totalRecordCount;

    @Column(name = "checksum", length = 64)
    private String checksum;

    @Column(name = "character_set", length = 20)
    private String characterSet;

    /* =======================================================
       SSIM CLASSIFICATION
       ======================================================= */

    @Enumerated(EnumType.STRING)
    @Column(name = "ssim_profile", length = 20)
    private ScheduleProfile scheduleProfile;

    @Version
    @Column(name = "version")
    private Long version;

    @Convert(converter = TimeModeCodeConverter.class)
    @Column(name = "time_mode", length = 1)
    private TimeMode timeMode;

    /* =======================================================
       PROCESSING STATE
       ======================================================= */

    @Enumerated(EnumType.STRING)
    @Column(name = "processing_status", length = 20)
    private ProcessingStatus processingStatus;

    @Builder.Default
    @Column(name = "is_superseding")
    private Boolean isSuperseding = Boolean.FALSE;

    @Column(name = "superseded_file_id")
    private UUID supersededFileId;

    /* =======================================================
       PROCESSING TIMESTAMPS
       ======================================================= */

    @Column(name = "received_timestamp", nullable = false, updatable = false)
    private Instant receivedTimestamp;

    @Column(name = "parsed_timestamp")
    private Instant parsedTimestamp;

    @Column(name = "validated_timestamp")
    private Instant validatedTimestamp;

    @Column(name = "stored_timestamp")
    private Instant storedTimestamp;

    @Column(name = "failed_timestamp")
    private Instant failedTimestamp;

    /* =======================================================
       ERROR HANDLING
       ======================================================= */

    @Column(name = "error_code", length = 50)
    private String errorCode;

    @Column(name = "remarks", length = 1000)
    private String remarks;


    public void setHeader(SsimHeaderEntity header) {
        this.header = header;
        if (header != null) {
            header.setFile(this);
        }
    }

    public void setCarrier(SsimCarrierEntity carrier) {
        this.carrier = carrier;
        if (carrier != null) {
            carrier.setFile(this);
        }
    }

    public void setTrailer(SsimTrailerEntity trailer) {
        this.trailer = trailer;
        if (trailer != null) {
            trailer.setFile(this);
        }
    }
}
