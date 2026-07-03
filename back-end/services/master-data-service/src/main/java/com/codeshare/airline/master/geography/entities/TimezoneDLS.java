package com.codeshare.airline.master.geography.entities;

import com.codeshare.airline.core.enums.common.RecordStatus;
import com.codeshare.airline.data.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "TIMEZONE_DST",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_DST_TZ_EFFECTIVE_FROM",
                        columnNames = {"TIMEZONE_ID", "EFFECTIVE_FROM"}
                )
        },
        indexes = {
                @Index(name = "IDX_TIMEZONE_DST_TIMEZONE", columnList = "TIMEZONE_ID"),
                @Index(name = "IDX_TIMEZONE_DST_EFFECTIVE", columnList = "EFFECTIVE_FROM, EFFECTIVE_TO"),
                @Index(name = "IDX_TIMEZONE_DST_STATUS", columnList = "STATUS")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class TimezoneDLS extends CSMDataAbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "TIMEZONE_ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_TIMEZONE_DST_TIMEZONE")
    )
    private Timezone timezone;

    @Column(name = "DST_START", nullable = false)
    private LocalDateTime dstStart;

    @Column(name = "DST_END", nullable = false)
    private LocalDateTime dstEnd;

    @Column(name = "DST_OFFSET_MINUTES", nullable = false)
    private Integer dstOffsetMinutes; // usually 60

    @Column(name = "EFFECTIVE_FROM", nullable = false)
    private LocalDateTime effectiveFrom;

    @Column(name = "EFFECTIVE_TO")
    private LocalDateTime effectiveTo;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false, length = 20)
    private RecordStatus recordStatus;

    @PrePersist
    @PreUpdate
    private void validateRule() {

        if (dstStart == null || dstEnd == null) {
            throw new IllegalStateException("DST start and end must not be null.");
        }

        // IMPORTANT: DST may cross year boundary (e.g., Oct → Mar)
        if (dstOffsetMinutes == null || dstOffsetMinutes <= 0) {
            throw new IllegalStateException("DST offset must be positive.");
        }

        if (effectiveTo != null && effectiveFrom.isAfter(effectiveTo)) {
            throw new IllegalStateException("EffectiveFrom must be before EffectiveTo.");
        }
    }
}