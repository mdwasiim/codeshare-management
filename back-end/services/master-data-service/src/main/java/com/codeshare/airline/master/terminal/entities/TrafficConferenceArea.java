package com.codeshare.airline.master.terminal.entities;

import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import com.codeshare.airline.platform.data.jpa.entity.CSMDataAbstractEntity;
import com.codeshare.airline.master.geography.entities.Region;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity(name = "Traffic_Conference_Area")
@Table(
        name = "TRAFFIC_CONFERENCE_AREA",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_TRAFFIC_CONFERENCE_AREA", columnNames = "AREA_CODE")
        },
        indexes = {
                @Index(name = "IDX_TRAFFIC_CONFERENCE_AREA_STATUS", columnList = "STATUS")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class TrafficConferenceArea extends CSMDataAbstractEntity {
    @Column(name = "AREA_CODE", nullable = false, length = 10)
    private String areaCode;

    @Column(name = "AREA_NAME", nullable = false, length = 100)
    private String areaName;

    @Column(name = "IATA_AREA_CODE", length = 10)
    private String iataAreaCode;


    @Column(name = "DESCRIPTION", length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false, length = 20)
    private RecordStatus recordStatus;

    @Column(name = "EFFECTIVE_FROM")
    private LocalDate effectiveFrom;

    @Column(name = "EFFECTIVE_TO")
    private LocalDate effectiveTo;

    @PrePersist
    @PreUpdate
    private void normalizeAndValidate() {
        if (areaCode != null) {
            areaCode = areaCode.trim().toUpperCase();
        }

        if (areaName != null) {
            areaName = areaName.trim();
        }

        if (effectiveFrom != null && effectiveTo != null &&
                effectiveFrom.isAfter(effectiveTo)) {
            throw new IllegalStateException("Invalid effective period.");
        }
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "REGION_ID",
            foreignKey = @ForeignKey(name = "FK_TRAFFIC_CONFERENCE_AREA_REGION")
    )
    private Region region;
}