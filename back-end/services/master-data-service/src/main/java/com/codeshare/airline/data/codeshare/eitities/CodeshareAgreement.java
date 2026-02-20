package com.codeshare.airline.data.codeshare.eitities;

import com.codeshare.airline.core.enums.codeshare.CodeshareDisclosureType;
import com.codeshare.airline.core.enums.codeshare.CodeshareInventoryType;
import com.codeshare.airline.core.enums.codeshare.CodeshareScopeType;
import com.codeshare.airline.core.enums.codeshare.CodeshareCommercialModel;
import com.codeshare.airline.core.enums.common.Status;
import com.codeshare.airline.data.core.eitities.AirlineCarrier;
import com.codeshare.airline.persistence.persistence.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(
        name = "MASTER_CODESHARE_AGREEMENT",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_CS_AGREEMENT_PAIR",
                        columnNames = {
                                "MARKETING_AIRLINE_ID",
                                "OPERATING_AIRLINE_ID",
                                "EFFECTIVE_FROM"
                        }
                )
        },
        indexes = {
                @Index(name = "IDX_CS_AGREEMENT_MARKETING", columnList = "MARKETING_AIRLINE_ID"),
                @Index(name = "IDX_CS_AGREEMENT_OPERATING", columnList = "OPERATING_AIRLINE_ID"),
                @Index(name = "IDX_CS_AGREEMENT_STATUS", columnList = "STATUS_CODE")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class CodeshareAgreement extends CSMDataAbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "MARKETING_AIRLINE_ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_CS_MARKETING_AIRLINE")
    )
    private AirlineCarrier marketingAirline;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "OPERATING_AIRLINE_ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_CS_OPERATING_AIRLINE")
    )
    private AirlineCarrier operatingAirline;

    @OneToMany(mappedBy = "agreement")
    private Set<CodeshareRoute> routes;

    @OneToMany(mappedBy = "agreement")
    private Set<CodeshareFlightMapping> flightMappings;

    @Enumerated(EnumType.STRING)
    @Column(name = "DISCLOSURE_TYPE", nullable = false, length = 30)
    private CodeshareDisclosureType disclosureType;

    @Enumerated(EnumType.STRING)
    @Column(name = "CODESHARE_TYPE", nullable = false, length = 20)
    private CodeshareCommercialModel codeshareCommercialModel;
    // FREE_SALE / BLOCK_SPACE / HYBRID

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS_CODE", nullable = false, length = 20)
    private Status statusCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "SCOPE_TYPE", nullable = false, length = 30)
    private CodeshareScopeType scopeType;

    @Enumerated(EnumType.STRING)
    @Column(name = "INVENTORY_TYPE", nullable = false, length = 30)
    private CodeshareInventoryType inventoryType;

    @Column(name = "AGREEMENT_CODE", nullable = false, length = 50, unique = true)
    private String agreementCode;

    @Column(name = "EFFECTIVE_FROM", nullable = false)
    private LocalDate effectiveFrom;

    @Column(name = "EFFECTIVE_TO")
    private LocalDate effectiveTo;

    @PrePersist
    @PreUpdate
    private void validate() {

        if (marketingAirline.equals(operatingAirline)) {
            throw new IllegalStateException("Marketing and operating airline cannot be the same.");
        }

        if (effectiveFrom != null && effectiveTo != null &&
                effectiveFrom.isAfter(effectiveTo)) {
            throw new IllegalStateException("Invalid effective period.");
        }
    }
}
