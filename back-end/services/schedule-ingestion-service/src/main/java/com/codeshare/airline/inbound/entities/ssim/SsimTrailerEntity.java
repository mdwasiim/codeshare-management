package com.codeshare.airline.inbound.entities.ssim;

import com.codeshare.airline.data.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "ssim_trailer",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_ssim_trailer_file",
                        columnNames = "file_id"
                )
        },
        indexes = {
                @Index(name = "idx_ssim_trailer_file", columnList = "file_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class SsimTrailerEntity extends CSMDataAbstractEntity {

    /* =======================================================
       RELATIONSHIP
       ======================================================= */

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "file_id",
            nullable = false,
            updatable = false,
            foreignKey = @ForeignKey(name = "fk_ssim_trailer_file")
    )
    private SsimFileMetaDataEntity file;

    /* =======================================================
       SSIM RECORD TYPE 5 (T5) – 200 BYTES
       ======================================================= */

    // SSIM T5: Byte 1
    @Column(name = "record_type", length = 1, nullable = false)
    private String recordType;

    // SSIM T5: Byte 2
    @Column(name = "spare_byte_2", length = 1)
    private String spareByte2;

    // SSIM T5: Bytes 3–5
    @Column(name = "airline_designator", length = 3)
    private String airlineDesignator;

    // SSIM T5: Bytes 6–12
    @Column(name = "release_date_raw", length = 7)
    private String releaseDateRaw;

    // SSIM T5: Bytes 13–187
    @Column(name = "spare_13_187", length = 175)
    private String spare13To187;

    // SSIM T5: Bytes 188–193
    @Column(name = "serial_check_reference", length = 6)
    private String serialCheckReference;

    // SSIM T5: Byte 194
    @Column(name = "continuation_end_code", length = 1)
    private String continuationEndCode;

    // SSIM T5: Bytes 195–200
    @Column(name = "record_serial_number", length = 6)
    private String recordSerialNumber;

}
