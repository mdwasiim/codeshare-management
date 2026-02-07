package com.codeshare.airline.processor.entities.raw;

import com.codeshare.airline.persistence.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "ssim_inbound_r1_header")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@ToString(callSuper = true)
public class SsimR1HeaderRecord extends CSMDataAbstractEntity {

    @OneToOne
    @JoinColumn(name = "dataset_id", nullable = false, unique = true)
    private SsimInboundDatasetMetadata dataset;

    // Byte 1
    @Column(name = "record_type", length = 1, nullable = false)
    private String recordType; // '1'

    // Bytes 2–4
    @Column(name = "airline_designator", length = 3, nullable = false)
    private String airlineDesignator;

    // Byte 5 (reserved)
    @Column(name = "reserved_05", length = 1)
    private String reserved05;

    // Bytes 6–11
    @Column(name = "dataset_serial_number", length = 6, nullable = false)
    private String datasetSerialNumber;

    // Byte 12 (reserved)
    @Column(name = "reserved_12", length = 1)
    private String reserved12;

    // Bytes 13–18
    @Column(name = "creation_date", length = 6, nullable = false)
    private String creationDate; // YYMMDD

    // Byte 19 (reserved)
    @Column(name = "reserved_19", length = 1)
    private String reserved19;

    // Bytes 20–23
    @Column(name = "creation_time", length = 4, nullable = false)
    private String creationTime; // HHMM

    // Byte 24 (reserved)
    @Column(name = "reserved_24", length = 1)
    private String reserved24;

    // Byte 25
    @Column(name = "schedule_type", length = 1, nullable = false)
    private String scheduleType;

    // Bytes 26–31
    @Column(name = "period_start_date", length = 6, nullable = false)
    private String periodStartDate;

    // Byte 32 (reserved)
    @Column(name = "reserved_32", length = 1)
    private String reserved32;

    // Bytes 33–38
    @Column(name = "period_end_date", length = 6, nullable = false)
    private String periodEndDate;

    // Byte 39 (reserved)
    @Column(name = "reserved_39", length = 1)
    private String reserved39;

    // Bytes 40–41
    @Column(name = "version_number", length = 2, nullable = false)
    private String versionNumber;

    // Byte 42 (reserved)
    @Column(name = "reserved_42", length = 1)
    private String reserved42;

    // Byte 43
    @Column(name = "continuation_indicator", length = 1)
    private String continuationIndicator;

    // Bytes 44–78
    @Column(name = "general_information", length = 35)
    private String generalInformation;
}
