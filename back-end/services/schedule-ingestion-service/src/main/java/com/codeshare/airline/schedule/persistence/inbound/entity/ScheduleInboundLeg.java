package com.codeshare.airline.schedule.persistence.inbound.entity;

import com.codeshare.airline.persistence.persistence.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Entity
@Table(
        name = "SCHEDULE_INBOUND_LEG",
        schema = "SCHEDULE_OPERATIONAL"
)
@Getter
@Setter
@NoArgsConstructor
public class ScheduleInboundLeg extends CSMDataAbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "FLIGHT_ID",
            foreignKey = @ForeignKey(name = "FK_SCH_LEG_FLIGHT")
    )
    private ScheduleInboundFlight flight;

    @Column(name = "LEG_SEQUENCE", nullable = false)
    private Integer legSequence;

    @Column(name = "ORIGIN", length = 3, nullable = false)
    private String origin;

    @Column(name = "DESTINATION", length = 3, nullable = false)
    private String destination;

    @Column(name = "DEPARTURE_TIME")
    private LocalTime departureTime;

    @Column(name = "ARRIVAL_TIME")
    private LocalTime arrivalTime;
}