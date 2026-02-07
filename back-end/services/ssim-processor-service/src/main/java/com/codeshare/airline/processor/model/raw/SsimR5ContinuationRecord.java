package com.codeshare.airline.processor.model.raw;

import com.codeshare.airline.persistence.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "ssim_inbound_r5_continuation")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@ToString(callSuper = true)
public class SsimR5ContinuationRecord extends CSMDataAbstractEntity {

    /* ===================== RELATIONSHIP ===================== */

    @ManyToOne(optional = false)
    @JoinColumn(name = "date_variation_id", nullable = false)
    private SsimR4DateVariationRecord r4;

    /* ===================== SSIM FIELDS ===================== */

    /**
     * SSIM Record Type.
     * Always '5' (continuation of Date Variation).
     */
    @Column(name = "record_type", length = 1, nullable = false)
    private String recordType;

    /**
     * Raw continuation payload (bytes 2–78).
     * Stored losslessly as per SSIM spec.
     */
    @Column(name = "continuation_data", length = 77, nullable = false)
    private String continuationData;
}
