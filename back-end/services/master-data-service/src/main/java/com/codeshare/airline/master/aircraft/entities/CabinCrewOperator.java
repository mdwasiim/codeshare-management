package com.codeshare.airline.master.aircraft.entities;

import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import com.codeshare.airline.platform.core.enums.master.aircraft.CrewEmployerType;
import com.codeshare.airline.platform.data.jpa.entity.CSMDataAbstractEntity;
import com.codeshare.airline.master.airlines.entities.AirlineCarrier;
import com.codeshare.airline.master.geography.entities.Country;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(
        name = "CABIN_CREW_EMPLOYER",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_CABIN_CREW_EMPLOYER_CODE",
                        columnNames = "EMPLOYER_CODE"
                )
        },
        indexes = {
                @Index(
                        name = "IDX_CABIN_CREW_EMPLOYER_STATUS",
                        columnList = "STATUS"
                ),
                @Index(
                        name = "IDX_CABIN_CREW_EMPLOYER_TYPE",
                        columnList = "EMPLOYER_TYPE"
                ),
                @Index(
                        name = "IDX_CABIN_CREW_EMPLOYER_ACTIVE",
                        columnList = "ACTIVE"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class CabinCrewOperator extends CSMDataAbstractEntity {

    @Column(name = "EMPLOYER_CODE", nullable = false, length = 20)
    private String employerCode;

    @Column(name = "EMPLOYER_NAME", nullable = false, length = 150)
    private String employerName;

    @Enumerated(EnumType.STRING)
    @Column(name = "EMPLOYER_TYPE", nullable = false, length = 30)
    private CrewEmployerType employerType;

    @Column(name = "IATA_CODE", length = 3)
    private String iataCode;

    @Column(name = "ICAO_CODE", length = 3)
    private String icaoCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "COUNTRY_ID",
            foreignKey = @ForeignKey(name = "FK_CABIN_CREW_EMPLOYER_COUNTRY")
    )
    private Country country;

    /**
     * Optional.
     * Applicable when the employer itself is an airline.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "AIRLINE_ID",
            foreignKey = @ForeignKey(name = "FK_CABIN_CREW_EMPLOYER_AIRLINE")
    )
    private AirlineCarrier airline;

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
    private void normalizeAndValidate() {

        if (employerCode == null || employerCode.isBlank()) {
            throw new IllegalStateException("Employer Code is mandatory.");
        }

        if (employerName == null || employerName.isBlank()) {
            throw new IllegalStateException("Employer Name is mandatory.");
        }

        if (employerType == null) {
            throw new IllegalStateException("Employer Type is mandatory.");
        }

        employerCode = employerCode.trim().toUpperCase();
        employerName = employerName.trim();

        if (iataCode != null) {
            iataCode = iataCode.trim().toUpperCase();
        }

        if (icaoCode != null) {
            icaoCode = icaoCode.trim().toUpperCase();
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