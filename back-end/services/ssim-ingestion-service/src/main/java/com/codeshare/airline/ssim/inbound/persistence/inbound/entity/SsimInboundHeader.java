package com.codeshare.airline.ssim.inbound.persistence.inbound.entity;

import com.codeshare.airline.persistence.persistence.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(
        name = "SSIM_INBOUND_HEADER",
        schema = "SSIM_OPERATIONAL",
        indexes = {
                @Index(
                        name = "IDX_SSIM_IN_HEADER_FILE_ID",
                        columnList = "FILE_ID"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class SsimInboundHeader extends CSMDataAbstractEntity {

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "FILE_ID",
            nullable = false,
            updatable = false,
            foreignKey = @ForeignKey(name = "FK_SSIM_HEADER_FILE")
    )
    private SsimInboundFile inboundFile;

    // Byte 1
    @Column(name = "RECORD_TYPE", length = 1)
    private String recordType;

    // Bytes 2–35 (34 chars)
    @Column(name = "TITLE_OF_CONTENTS", length = 34)
    private String titleOfContents;

    // Bytes 36–40 (5 chars)
    @Column(name = "SPARE_36_40", length = 5)
    private String spare36To40;

    // Byte 41 (1 char)
    @Column(name = "NUMBER_OF_SEASONS", length = 1)
    private String numberOfSeasons;

    // Bytes 42–191 (150 chars)
    @Column(name = "SPARE_42_191", length = 150)
    private String spare42To191;

    // Bytes 192–194 (3 chars)
    @Column(name = "DATASET_SERIAL_NUMBER", length = 3)
    private String datasetSerialNumber;

    // Bytes 195–200 (6 chars)
    @Column(name = "RECORD_SERIAL_NUMBER", length = 6)
    private String recordSerialNumber;

    @Column(name = "RAW_RECORD", length = 200)
    private String rawRecord;

    @Column(name = "PARSED_TIMESTAMP")
    private Instant parsedTimestamp;
}

