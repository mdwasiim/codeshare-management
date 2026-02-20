package com.codeshare.airline.data.codeshare.eitities;

import com.codeshare.airline.core.enums.codeshare.CodeshareRouteScopeType;
import com.codeshare.airline.core.enums.common.Status;
import com.codeshare.airline.data.core.eitities.Airport;
import com.codeshare.airline.persistence.persistence.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(
        name = "MASTER_CODESHARE_ROUTE",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_CS_ROUTE",
                        columnNames = {
                                "AGREEMENT_ID",
                                "ORIGIN_AIRPORT_ID",
                                "DESTINATION_AIRPORT_ID",
                                "EFFECTIVE_FROM"
                        }
                )
        },
        indexes = {
                @Index(name = "IDX_CS_ROUTE_AGREEMENT", columnList = "AGREEMENT_ID"),
                @Index(name = "IDX_CS_ROUTE_ORIGIN", columnList = "ORIGIN_AIRPORT_ID"),
                @Index(name = "IDX_CS_ROUTE_DEST", columnList = "DESTINATION_AIRPORT_ID"),
                @Index(name = "IDX_CS_ROUTE_STATUS", columnList = "STATUS_CODE")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class CodeshareRoute extends CSMDataAbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "AGREEMENT_ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_CS_ROUTE_AGREEMENT")
    )
    private CodeshareAgreement agreement;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "ORIGIN_AIRPORT_ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_CS_ROUTE_ORIGIN")
    )
    private Airport origin;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "DESTINATION_AIRPORT_ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_CS_ROUTE_DEST")
    )
    private Airport destination;

    @OneToMany(mappedBy = "route")
    private Set<CodeshareFlightMapping> mappings;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROUTE_SCOPE_TYPE", length = 30)
    private CodeshareRouteScopeType routeScopeType;

    // true = applies both directions (A→B and B→A)
    @Column(name = "BIDIRECTIONAL", nullable = false)
    private Boolean bidirectional = Boolean.FALSE;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS_CODE", nullable = false, length = 20)
    private Status statusCode;

    @Column(name = "EFFECTIVE_FROM", nullable = false)
    private LocalDate effectiveFrom;

    @Column(name = "EFFECTIVE_TO")
    private LocalDate effectiveTo;

    @PrePersist
    @PreUpdate
    private void validate() {

        if (origin != null && destination != null &&
                origin.getId().equals(destination.getId())) {
            throw new IllegalStateException(
                    "Origin and destination cannot be the same."
            );
        }

        if (effectiveFrom != null && effectiveTo != null &&
                effectiveFrom.isAfter(effectiveTo)) {
            throw new IllegalStateException("Invalid effective period.");
        }
    }
}
