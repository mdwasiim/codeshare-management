package com.codeshare.airline.master.commercial.codeshare.eitities;

import com.codeshare.airline.core.enums.codeshare.EquipmentRuleType;
import com.codeshare.airline.core.enums.common.RecordStatus;
import com.codeshare.airline.master.aircraft.eitities.AircraftType;
import com.codeshare.airline.data.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(
        name = "MASTER_CODESHARE_EQUIPMENT_RULE",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_CS_EQUIPMENT_RULE",
                        columnNames = {
                                "FLIGHT_MAPPING_ID",
                                "AIRCRAFT_TYPE_ID",
                                "EFFECTIVE_FROM"
                        }
                )
        },
        indexes = {
                @Index(name = "IDX_CS_EQUIP_MAPPING", columnList = "FLIGHT_MAPPING_ID"),
                @Index(name = "IDX_CS_EQUIP_STATUS", columnList = "STATUS"),
                @Index(name = "IDX_CS_EQUIP_AIRCRAFT", columnList = "AIRCRAFT_TYPE_ID")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class CodeshareEquipmentRule extends CSMDataAbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "FLIGHT_MAPPING_ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_CS_EQUIP_MAPPING")
    )
    private CodeshareFlightMapping flightMapping;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "AIRCRAFT_TYPE_ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_CS_EQUIP_AIRCRAFT")
    )
    private AircraftType aircraftType;

    @Enumerated(EnumType.STRING)
    @Column(name = "RULE_TYPE", nullable = false, length = 20)
    private EquipmentRuleType ruleType;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false, length = 20)
    private RecordStatus recordStatus;

    @Column(name = "EFFECTIVE_FROM", nullable = false)
    private LocalDate effectiveFrom;

    @Column(name = "EFFECTIVE_TO")
    private LocalDate effectiveTo;

    @PrePersist
    @PreUpdate
    private void validate() {

        if (flightMapping == null) {
            throw new IllegalStateException("Flight mapping is required.");
        }

        if (aircraftType == null) {
            throw new IllegalStateException("Aircraft type is required.");
        }

        if (effectiveFrom != null && effectiveTo != null &&
                effectiveFrom.isAfter(effectiveTo)) {
            throw new IllegalStateException("Invalid effective period.");
        }
    }
}
