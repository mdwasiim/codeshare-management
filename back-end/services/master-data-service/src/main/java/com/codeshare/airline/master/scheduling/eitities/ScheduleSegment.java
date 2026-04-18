package com.codeshare.airline.scheduling.eitities;

import com.codeshare.airline.persistence.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(
        name = "SCHEDULE_SEGMENT",
        indexes = {
                @Index(name = "IDX_SEGMENT_FLIGHT", columnList = "FLIGHT_ID"),
                @Index(name = "IDX_SEGMENT_BOARD_OFF", columnList = "BOARD_POINT,OFF_POINT")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleSegment extends CSMDataAbstractEntity {

    // ---------------------------------------------------------
    // RELATIONSHIP
    // ---------------------------------------------------------

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "FLIGHT_ID", nullable = false)
    private ScheduleFlight scheduleFlight;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LEG_ID")
    private ScheduleLeg scheduleLeg;

    // ---------------------------------------------------------
    // COMMERCIAL BOARD / OFF
    // ---------------------------------------------------------

    @Column(name = "BOARD_POINT", nullable = false, length = 3)
    private String boardPoint;

    @Column(name = "OFF_POINT", nullable = false, length = 3)
    private String offPoint;

    // ---------------------------------------------------------
    // COMMERCIAL DETAILS
    // ---------------------------------------------------------

    @Column(name = "PRBD", length = 20)
    private String prbd; // Passenger Reservation Booking Designator

    @Column(name = "PRBM", length = 20)
    private String prbm; // Booking Modifier

    @Column(name = "TRAFFIC_RESTRICTION_CODE", length = 5)
    private String trafficRestrictionCode;

    @Column(name = "TERMINAL_DEPARTURE", length = 5)
    private String terminalDeparture;

    @Column(name = "TERMINAL_ARRIVAL", length = 5)
    private String terminalArrival;

    // ---------------------------------------------------------
    // RELATIONSHIPS
    // ---------------------------------------------------------

    @OneToMany(mappedBy = "scheduleSegment", cascade = CascadeType.ALL)
    private List<ScheduleDei> deis;
}