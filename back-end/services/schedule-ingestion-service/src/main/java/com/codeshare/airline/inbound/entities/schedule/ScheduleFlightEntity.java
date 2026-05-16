package com.codeshare.airline.inbound.entities.schedule;

import com.codeshare.airline.data.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "schedule_flight",
        indexes = {
                @Index(name = "idx_sch_flight_msg", columnList = "sub_message_id"),
                @Index(name = "idx_sch_flight_number", columnList = "carrier,flight_number")
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_sub_msg_flight_seq",
                        columnNames = {"sub_message_id", "flight_sequence_number"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class ScheduleFlightEntity extends CSMDataAbstractEntity {

    /* ================= RELATIONSHIP ================= */

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sub_message_id", nullable = false)
    private ScheduleSubMessageEntity subMessage;

    @Column(name = "flight_sequence_number", nullable = false)
    private Integer flightSequenceNumber;

    /* ================= IDENTITY ================= */

    @Column(name = "carrier", length = 3, nullable = false)
    private String carrier;

    @Column(name = "flight_number", length = 5, nullable = false)
    private String flightNumber;

    @Column(name = "suffix", length = 1)
    private String suffix;

    @Column(name = "operation_date_raw", length = 20)
    private String operationDateRaw;

    @Column(name = "board_point", length = 3)
    private String boardPoint;

    @Column(name = "off_point", length = 3)
    private String offPoint;

    /* ================= EQUIPMENT ================= */

    @Column(name = "aircraft_type", length = 4)
    private String aircraftType;

    @Column(name = "service_type", length = 2)
    private String serviceType;

    @Column(name = "aircraft_configuration", length = 10)
    private String aircraftConfiguration;

    @Column(name = "booking_designator", length = 10)
    private String bookingDesignator;

    /* ================= CHILDREN ================= */

    @OneToMany(mappedBy = "flight", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("legSequence ASC")
    private List<ScheduleLegEntity> legs = new ArrayList<>();

    @OneToMany(mappedBy = "flight", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sequenceOrder ASC")
    private List<ScheduleDataElementEntity> deis = new ArrayList<>();

    @OneToMany(mappedBy = "flight", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("startDate ASC")
    private List<SchedulePeriodEntity> periods = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "schedule_flight_si",
            joinColumns = @JoinColumn(name = "flight_id"))
    @Column(name = "supplementary_info", length = 500)
    private List<String> supplementaryInfo = new ArrayList<>();

    /* ================= HELPERS ================= */

    public void addLeg(ScheduleLegEntity leg) {
        if (leg != null) {
            legs.add(leg);
            leg.setFlight(this);
        }
    }

    public void addDei(ScheduleDataElementEntity dei) {
        deis.add(dei);
        dei.setFlight(this);
    }

    public void addPeriod(SchedulePeriodEntity period) {
        periods.add(period);
        period.setFlight(this);
    }

    public List<ScheduleLegEntity> getSafeLegs() {
        return legs != null ? legs : new ArrayList<>();
    }

    public List<ScheduleDataElementEntity> getSafeDeis() {
        return deis != null ? deis : new ArrayList<>();
    }
}
