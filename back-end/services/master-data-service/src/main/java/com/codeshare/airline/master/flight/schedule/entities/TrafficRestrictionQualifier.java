package com.codeshare.airline.master.flight.schedule.entities;

import com.codeshare.airline.core.enums.common.RecordStatus;
import com.codeshare.airline.data.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(
        name = "TRAFFIC_RESTRICTION_QUALIFIER",
        uniqueConstraints = {

                @UniqueConstraint(
                        name = "UK_TRAFFIC_RESTRICTION_QUALIFIER",
                        columnNames = {
                                "TRAFFIC_RESTRICTION_CODE_ID",
                                "QUALIFIER_CODE"
                        }
                )
        },
        indexes = {

                @Index(
                        name = "IDX_TRAFFIC_RESTRICTION_QUALIFIER_STATUS",
                        columnList = "STATUS"
                ),

                @Index(
                        name = "IDX_TRAFFIC_RESTRICTION_QUALIFIER_ACTIVE",
                        columnList = "ACTIVE"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class TrafficRestrictionQualifier extends CSMDataAbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "TRAFFIC_RESTRICTION_CODE_ID",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "FK_TRAFFIC_RESTRICTION_QUALIFIER_CODE"
            )
    )
    private TrafficRestrictionCode trafficRestrictionCode;

    /**
     * IATA qualifier.
     */
    @Column(name = "QUALIFIER_CODE", nullable = false, length = 2)
    private String qualifierCode;

    @Column(name = "QUALIFIER_NAME", nullable = false, length = 150)
    private String qualifierName;

    /**
     * Official IATA meaning.
     */
    @Column(name = "IATA_DEFINITION", length = 1000)
    private String iataDefinition;

    @Column(name = "ACTIVE", nullable = false)
    private Boolean active = Boolean.TRUE;

    @Column(name = "DISPLAY_ORDER")
    private Integer displayOrder = 1;

    @Column(name = "DESCRIPTION", length = 500)
    private String description;

    @Column(name = "REMARKS", length = 1000)
    private String remarks;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false, length = 20)
    private RecordStatus recordStatus = RecordStatus.ACTIVE;

    @Column(name = "EFFECTIVE_FROM")
    private LocalDate effectiveFrom;

    @Column(name = "EFFECTIVE_TO")
    private LocalDate effectiveTo;

    @PrePersist
    @PreUpdate
    private void validateAndNormalize() {

        if (trafficRestrictionCode == null) {
            throw new IllegalStateException(
                    "Traffic Restriction Code is mandatory."
            );
        }

        if (qualifierCode == null || qualifierCode.isBlank()) {
            throw new IllegalStateException(
                    "Qualifier Code is mandatory."
            );
        }

        if (qualifierName == null || qualifierName.isBlank()) {
            throw new IllegalStateException(
                    "Qualifier Name is mandatory."
            );
        }

        qualifierCode = qualifierCode.trim().toUpperCase();
        qualifierName = qualifierName.trim();

        if (iataDefinition != null) {
            iataDefinition = iataDefinition.trim();
        }

        if (description != null) {
            description = description.trim();
        }

        if (remarks != null) {
            remarks = remarks.trim();
        }

        if (effectiveFrom != null &&
                effectiveTo != null &&
                effectiveFrom.isAfter(effectiveTo)) {

            throw new IllegalStateException(
                    "Effective From cannot be after Effective To."
            );
        }
    }
}