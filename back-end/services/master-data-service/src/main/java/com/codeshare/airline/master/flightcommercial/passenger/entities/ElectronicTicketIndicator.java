package com.codeshare.airline.master.flightcommercial.passenger.entities;

import com.codeshare.airline.core.enums.common.RecordStatus;
import com.codeshare.airline.data.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(
        name = "ELECTRONIC_TICKET_INDICATOR",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_ELECTRONIC_TICKET_INDICATOR",
                        columnNames = "INDICATOR_CODE"
                )
        },
        indexes = {

                @Index(
                        name = "IDX_ELECTRONIC_TICKET_INDICATOR_STATUS",
                        columnList = "STATUS"
                ),

                @Index(
                        name = "IDX_ELECTRONIC_TICKET_INDICATOR_ACTIVE",
                        columnList = "ACTIVE"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class ElectronicTicketIndicator extends CSMDataAbstractEntity {

    /**
     * IATA Electronic Ticket Indicator.
     *
     * Y = Electronic Ticket Supported
     * N = Electronic Ticket Not Supported
     */
    @Column(name = "INDICATOR_CODE", nullable = false, length = 1)
    private String indicatorCode;

    @Column(name = "INDICATOR_NAME", nullable = false, length = 100)
    private String indicatorName;

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

        if (indicatorCode == null || indicatorCode.isBlank()) {
            throw new IllegalStateException("Indicator Code is mandatory.");
        }

        if (indicatorName == null || indicatorName.isBlank()) {
            throw new IllegalStateException("Indicator Name is mandatory.");
        }

        indicatorCode = indicatorCode.trim().toUpperCase();

        if (!Set.of("Y", "N").contains(indicatorCode)) {
            throw new IllegalStateException(
                    "Indicator Code must be 'Y' or 'N'."
            );
        }

        indicatorName = indicatorName.trim();

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