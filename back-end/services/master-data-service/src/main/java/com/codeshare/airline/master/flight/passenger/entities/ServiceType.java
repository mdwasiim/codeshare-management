package com.codeshare.airline.master.flight.passenger.entities;

import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import com.codeshare.airline.platform.data.jpa.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(
        name = "SERVICE_TYPE",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_SERVICE_TYPE",
                        columnNames = "SERVICE_TYPE_CODE"
                )
        },
        indexes = {

                @Index(
                        name = "IDX_SERVICE_TYPE_STATUS",
                        columnList = "STATUS"
                ),

                @Index(
                        name = "IDX_SERVICE_TYPE_ACTIVE",
                        columnList = "ACTIVE"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class ServiceType extends CSMDataAbstractEntity {

    /**
     * IATA Service Type
     *
     * Examples
     * J
     * G
     * F
     * C
     */
    @Column(name = "SERVICE_TYPE_CODE", nullable = false, length = 5)
    private String serviceTypeCode;

    @Column(name = "SERVICE_TYPE_NAME", nullable = false, length = 100)
    private String serviceTypeName;

    /**
     * Passenger
     * Cargo
     * Mixed
     */
    @Column(name = "CATEGORY", length = 50)
    private String category;

    /**
     * Official IATA definition.
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

        if (serviceTypeCode == null || serviceTypeCode.isBlank()) {
            throw new IllegalStateException("Service Type Code is mandatory.");
        }

        if (serviceTypeName == null || serviceTypeName.isBlank()) {
            throw new IllegalStateException("Service Type Name is mandatory.");
        }

        serviceTypeCode = serviceTypeCode.trim().toUpperCase();
        serviceTypeName = serviceTypeName.trim();

        if (category != null) {
            category = category.trim().toUpperCase();
        }

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