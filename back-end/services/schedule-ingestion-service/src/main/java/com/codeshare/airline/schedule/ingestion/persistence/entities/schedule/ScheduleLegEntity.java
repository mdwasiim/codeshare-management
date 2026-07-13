package com.codeshare.airline.schedule.ingestion.persistence.entities.schedule;

import com.codeshare.airline.platform.data.jpa.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "schedule_leg",
        indexes = {
                @Index(name = "idx_sch_leg_flight", columnList = "flight_id"),
                @Index(name = "idx_sch_leg_route", columnList = "origin,destination"),
                @Index(name = "idx_sch_leg_sequence", columnList = "leg_sequence")
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_flight_leg_sequence",
                        columnNames = {"flight_id", "leg_sequence"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class ScheduleLegEntity extends CSMDataAbstractEntity {

    /* ================= RELATIONSHIP ================= */

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "flight_id", nullable = false)
    private ScheduleFlightEntity flight;

    @OneToMany(mappedBy = "leg", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sequenceOrder ASC")
    @BatchSize(size = 100)
    private List<ScheduleDataElementEntity> deis = new ArrayList<>();

    /* ================= SEQUENCE ================= */

    @Column(name = "leg_sequence", nullable = false)
    private Integer legSequence;

    /* ================= ROUTING ================= */

    @Column(name = "origin", length = 3, nullable = false)
    private String origin;

    @Column(name = "destination", length = 3, nullable = false)
    private String destination;

    /* ================= TIMING ================= */

    @Column(name = "departure_time")
    private LocalTime departureTime;

    @Column(name = "arrival_time")
    private LocalTime arrivalTime;

    @Column(name = "departure_day_offset")
    private Integer departureDayOffset;

    @Column(name = "arrival_day_offset")
    private Integer arrivalDayOffset;

    /* ================= EQUIPMENT OVERRIDE ================= */

    @Column(name = "aircraft_type", length = 4)
    private String aircraftType;

    @Column(name = "service_type", length = 2)
    private String serviceType;

    @Column(name = "aircraft_configuration", length = 10)
    private String aircraftConfiguration;

    /* ================= HELPERS ================= */

    public void addDei(ScheduleDataElementEntity dei) {
        if (dei != null) {
            deis.add(dei);
            dei.attachToLeg(this);
        }
    }

    public List<ScheduleDataElementEntity> getSafeDeis() {
        return deis != null ? deis : new ArrayList<>();
    }
}
