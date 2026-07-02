package com.codeshare.airline.master.reference.entities;

import com.codeshare.airline.core.enums.common.RecordStatus;
import com.codeshare.airline.data.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(
        name = "LANGUAGE",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_LANGUAGE_CODE", columnNames = "LANGUAGE_CODE")
        },
        indexes = {
                @Index(name = "IDX_LANGUAGE_CODE", columnList = "LANGUAGE_CODE"),
                @Index(name = "IDX_LANGUAGE_STATUS", columnList = "STATUS")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class Language extends CSMDataAbstractEntity {

    @Column(name = "LANGUAGE_CODE", nullable = false, length = 10)
    private String languageCode;

    @Column(name = "LANGUAGE_NAME", nullable = false, length = 100)
    private String languageName;

    @Column(name = "DESCRIPTION", length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false, length = 20)
    private RecordStatus recordStatus = RecordStatus.ACTIVE;

    @Column(name = "EFFECTIVE_FROM")
    private LocalDate effectiveFrom;

    @Column(name = "EFFECTIVE_TO")
    private LocalDate effectiveTo;

    @PrePersist
    @PreUpdate
    private void normalizeAndValidate() {
        if (languageCode == null || languageCode.isBlank()) {
            throw new IllegalStateException("Language code is mandatory.");
        }

        if (languageName == null || languageName.isBlank()) {
            throw new IllegalStateException("Language name is mandatory.");
        }

        languageCode = languageCode.trim().toUpperCase();
        languageName = languageName.trim();

        if (description != null) {
            description = description.trim();
        }

        if (effectiveFrom != null && effectiveTo != null && effectiveFrom.isAfter(effectiveTo)) {
            throw new IllegalStateException("Effective From cannot be after Effective To.");
        }
    }
}
