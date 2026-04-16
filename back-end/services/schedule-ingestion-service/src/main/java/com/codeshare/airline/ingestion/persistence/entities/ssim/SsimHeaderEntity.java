package com.codeshare.airline.ingestion.persistence.entities.ssim;

import com.codeshare.airline.ingestion.domain.enums.RecordType;
import com.codeshare.airline.persistence.persistence.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "ssim_header",
        indexes = {
                @Index(
                        name = "idx_ssim_header_file",
                        columnList = "file_id"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class SsimHeaderEntity extends CSMDataAbstractEntity {

    /* =======================================================
       RELATIONSHIP
       ======================================================= */

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "file_id",
            nullable = false,
            updatable = false,
            foreignKey = @ForeignKey(name = "fk_ssim_header_file")
    )
    private SsimFileMetaDataEntity file;

    /* =======================================================
       SSIM RECORD TYPE 1 (T1) – 200 BYTES
       ======================================================= */

    // SSIM T1: Byte 1
    @Enumerated(EnumType.STRING)
    @Column(name = "record_type", length = 1)
    private RecordType recordType;

    // SSIM T1: Bytes 2–35
    @Column(name = "title_of_contents", length = 34)
    private String titleOfContents;

    // SSIM T1: Bytes 36–40
    @Column(name = "spare_36_40", length = 5)
    private String spare36To40;

    // SSIM T1: Byte 41
    @Column(name = "number_of_seasons", length = 1)
    private Integer numberOfSeasons;

    // SSIM T1: Bytes 42–191
    @Column(name = "spare_42_191", length = 150)
    private String spare42To191;

    // SSIM T1: Bytes 192–194
    @Column(name = "dataset_serial_number", length = 3)
    private String datasetSerialNumber;

    // SSIM T1: Bytes 195–200
    @Column(name = "record_serial_number", length = 6)
    private String recordSerialNumber;
}