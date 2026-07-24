package com.codeshare.airline.master.geography.entities;

import com.codeshare.airline.platform.data.jpa.entity.CSMMasterDataEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.zone.ZoneRules;

@Entity
@Table(
        name = "TIMEZONE",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_TIMEZONE_IDENTIFIER",
                        columnNames = "TZ_IDENTIFIER"
                )
        },
        indexes = {
                @Index(name = "IDX_TIMEZONE_IDENTIFIER", columnList = "TZ_IDENTIFIER"),
                @Index(name = "IDX_TIMEZONE_STATUS", columnList = "STATUS"),
                @Index(name = "IDX_TIMEZONE_DST", columnList = "OBSERVES_DST")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class Timezone extends CSMMasterDataEntity {

    @Column(name = "TZ_IDENTIFIER", nullable = false, length = 100)
    private String tzIdentifier;

    @Column(name = "TIMEZONE_NAME", length = 150)
    private String timezoneName;

    @Column(name = "ABBREVIATION", length = 20)
    private String abbreviation;

    @Column(name = "STANDARD_UTC_OFFSET_MIN", nullable = false)
    private Integer standardUtcOffsetMinutes;

    @Column(name = "OBSERVES_DST", nullable = false)
    private Boolean observesDst = Boolean.FALSE;

    @PrePersist
    @PreUpdate
    private void normalize() {

        if (tzIdentifier == null || tzIdentifier.isBlank()) {
            throw new IllegalArgumentException("Timezone identifier is required.");
        }

        tzIdentifier = tzIdentifier.trim();

        ZoneId zoneId;

        try {
            zoneId = ZoneId.of(tzIdentifier);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Invalid IANA timezone identifier.");
        }

        ZoneRules rules = zoneId.getRules();

        ZoneOffset offset = rules.getStandardOffset(Instant.EPOCH);

        standardUtcOffsetMinutes = offset.getTotalSeconds() / 60;

        observesDst = !rules.getTransitionRules().isEmpty();

        if (timezoneName == null || timezoneName.isBlank()) {
            timezoneName = tzIdentifier;
        } else {
            timezoneName = timezoneName.trim();
        }

        if (abbreviation == null || abbreviation.isBlank()) {
            abbreviation = zoneId.getDisplayName(java.time.format.TextStyle.SHORT, java.util.Locale.ENGLISH);
        } else {
            abbreviation = abbreviation.trim().toUpperCase();
        }
    }
}