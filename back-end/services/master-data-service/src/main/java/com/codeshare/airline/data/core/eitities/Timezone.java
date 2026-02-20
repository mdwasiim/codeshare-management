package com.codeshare.airline.data.core.eitities;

import com.codeshare.airline.core.enums.common.Status;
import com.codeshare.airline.persistence.persistence.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZoneId;
import java.time.ZoneOffset;

@Entity
@Table(
        name = "MASTER_TIMEZONE",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_TIMEZONE_IDENTIFIER",
                        columnNames = "TZ_IDENTIFIER"
                )
        },
        indexes = {
                @Index(name = "IDX_TIMEZONE_STATUS", columnList = "STATUS_CODE"),
                @Index(name = "IDX_TIMEZONE_DST", columnList = "OBSERVES_DST")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class Timezone extends CSMDataAbstractEntity {

    @Column(name = "TZ_IDENTIFIER", nullable = false, length = 100)
    private String tzIdentifier; // e.g., Asia/Qatar

    @Column(name = "STANDARD_UTC_OFFSET_MIN", nullable = false)
    private Integer standardUtcOffsetMinutes; // e.g., +180

    @Column(name = "OBSERVES_DST", nullable = false)
    private Boolean observesDst = Boolean.FALSE;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS_CODE", nullable = false, length = 20)
    private Status statusCode;

    @PrePersist
    @PreUpdate
    private void normalizeAndValidate() {

        if (tzIdentifier == null || tzIdentifier.isBlank()) {
            throw new IllegalArgumentException("Timezone identifier is required.");
        }

        tzIdentifier = tzIdentifier.trim();

        ZoneId zoneId;

        try {
            zoneId = ZoneId.of(tzIdentifier);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid IANA timezone identifier.");
        }

        // Auto-calculate standard offset (deterministic)
        ZoneOffset offset = zoneId.getRules()
                .getStandardOffset(java.time.Instant.EPOCH);

        standardUtcOffsetMinutes = offset.getTotalSeconds() / 60;

        // Auto-detect DST support
        observesDst = zoneId.getRules().getTransitionRules().size() > 0;

        if (statusCode == null) {
            throw new IllegalArgumentException("Status is required.");
        }
    }
}

