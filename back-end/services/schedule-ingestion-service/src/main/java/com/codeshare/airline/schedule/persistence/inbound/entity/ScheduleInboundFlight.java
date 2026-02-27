package com.codeshare.airline.schedule.persistence.inbound.entity;

import com.codeshare.airline.persistence.persistence.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "SCHEDULE_INBOUND_FLIGHT",
        schema = "SCHEDULE_OPERATIONAL"
)
@Getter
@Setter
@NoArgsConstructor
public class ScheduleInboundFlight extends CSMDataAbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "BLOCK_ID",
            foreignKey = @ForeignKey(name = "FK_SCH_FLIGHT_BLOCK")
    )
    private ScheduleInboundBlock block;

    @Column(name = "CARRIER", length = 3, nullable = false)
    private String carrier;

    @Column(name = "FLIGHT_NUMBER", length = 5, nullable = false)
    private String flightNumber;

    @Column(name = "SUFFIX", length = 1)
    private String suffix;

    // 🔹 SSM only
    @Column(name = "OPERATION_DATE")
    private LocalDate operationDate;

    // 🔹 ASM only
    @Column(name = "PERIOD_FROM")
    private LocalDate periodFrom;

    @Column(name = "PERIOD_TO")
    private LocalDate periodTo;

    @Column(name = "DAYS_OF_OPERATION", length = 7)
    private String daysOfOperation;

    // Common
    @Column(name = "AIRCRAFT_TYPE", length = 4)
    private String aircraftType;

    @OneToMany(
            mappedBy = "flight",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ScheduleInboundLeg> legs = new ArrayList<>();

    @OneToMany(
            mappedBy = "flight",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ScheduleInboundDei> deis = new ArrayList<>();
}