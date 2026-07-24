package com.codeshare.airline.master.aircraft.entities;

import com.codeshare.airline.master.geography.entities.Country;
import com.codeshare.airline.platform.core.enums.master.aircraft.CrewEmployerType;
import com.codeshare.airline.platform.data.jpa.entity.CSMMasterDataEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "COCKPIT_CREW_EMPLOYER",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_COCKPIT_CREW_EMPLOYER_CODE",
                        columnNames = "EMPLOYER_CODE"
                ),
                @UniqueConstraint(
                        name = "UK_COCKPIT_CREW_EMPLOYER_NAME",
                        columnNames = "EMPLOYER_NAME"
                )
        },
        indexes = {
                @Index(
                        name = "IDX_COCKPIT_CREW_EMPLOYER_CODE",
                        columnList = "EMPLOYER_CODE"
                ),
                @Index(
                        name = "IDX_COCKPIT_CREW_EMPLOYER_NAME",
                        columnList = "EMPLOYER_NAME"
                ),
                @Index(
                        name = "IDX_COCKPIT_CREW_EMPLOYER_TYPE",
                        columnList = "EMPLOYER_TYPE"
                ),
                @Index(
                        name = "IDX_COCKPIT_CREW_EMPLOYER_COUNTRY",
                        columnList = "COUNTRY_ID"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class CockpitCrewEmployer extends CSMMasterDataEntity {

    /**
     * Employer code.
     */
    @NotBlank
    @Column(name = "EMPLOYER_CODE", nullable = false, length = 20)
    private String employerCode;

    /**
     * Employer display name.
     */
    @NotBlank
    @Column(name = "EMPLOYER_NAME", nullable = false, length = 150)
    private String employerName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "EMPLOYER_TYPE", nullable = false, length = 30)
    private CrewEmployerType employerType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "COUNTRY_ID",
            foreignKey = @ForeignKey(name = "FK_COCKPIT_CREW_EMPLOYER_COUNTRY")
    )
    private Country country;

    @Column(name = "DISPLAY_ORDER")
    private Integer displayOrder = 1;

    @PrePersist
    @PreUpdate
    private void normalize() {

        employerCode = employerCode.trim().toUpperCase();
        employerName = employerName.trim();
    }
}