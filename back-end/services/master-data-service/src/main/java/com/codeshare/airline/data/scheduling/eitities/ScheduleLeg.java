package com.codeshare.airline.data.scheduling.eitities;

import com.codeshare.airline.persistence.persistence.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

@Entity
@Table(
        name = "SCHEDULE_LEG",
        indexes = {
                @Index(name = "IDX_LEG_FLIGHT", columnList = "FLIGHT_ID"),
                @Index(name = "IDX_LEG_ORIGIN_DEST", columnList = "ORIGIN_STATION,DESTINATION_STATION")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleLeg extends CSMDataAbstractEntity {

    // ---------------------------------------------------------
    // RELATIONSHIP
    // ---------------------------------------------------------

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "FLIGHT_ID", nullable = false)
    private ScheduleFlight scheduleFlight;

    // ---------------------------------------------------------
    // IDENTIFICATION
    // ---------------------------------------------------------

    @Column(name = "LEG_SEQUENCE_NUMBER", nullable = false)
    private Integer legSequenceNumber;

    // ---------------------------------------------------------
    // ROUTING
    // ---------------------------------------------------------

    @Column(name = "ORIGIN_STATION", nullable = false, length = 3)
    private String originStation;

    @Column(name = "DESTINATION_STATION", nullable = false, length = 3)
    private String destinationStation;

    // ---------------------------------------------------------
    // EQUIPMENT
    // ---------------------------------------------------------

    @Column(name = "AIRCRAFT_TYPE", length = 3)
    private String aircraftType;

    @Column(name = "AIRCRAFT_CONFIGURATION_VERSION", length = 10)
    private String aircraftConfigurationVersion;

    @Column(name = "AIRCRAFT_OWNER", length = 10)
    private String aircraftOwner;

    @Column(name = "COCKPIT_CREW_EMPLOYER", length = 10)
    private String cockpitCrewEmployer;

    @Column(name = "CABIN_CREW_EMPLOYER", length = 10)
    private String cabinCrewEmployer;

    // ---------------------------------------------------------
    // TIMES (LOCAL - Used for SSIM / GDS)
    // ---------------------------------------------------------

    @Column(name = "STD_LOCAL", length = 4)
    private LocalTime scheduledTimeDepartureLocal;

    @Column(name = "STA_LOCAL", length = 4)
    private LocalTime  scheduledTimeArrivalLocal;

    @Column(name = "PASSENGER_STD_LOCAL", length = 4)
    private LocalTime passengerStdLocal;

    @Column(name = "PASSENGER_STA_LOCAL", length = 4)
    private LocalTime  passengerStaLocal;

    // ---------------------------------------------------------
    // TIMES (UTC - Used for Operations / Integration)
    // ---------------------------------------------------------

    @Column(name = "STD_UTC", length = 4)
    private LocalTime  scheduledTimeDepartureUtc;

    @Column(name = "STA_UTC", length = 4)
    private LocalTime  scheduledTimeArrivalUtc;

    @Column(name = "PASSENGER_STD_UTC", length = 4)
    private LocalTime  passengerStdUtc;

    @Column(name = "PASSENGER_STA_UTC", length = 4)
    private LocalTime  passengerStaUtc;

    // ---------------------------------------------------------
    // UTC OFFSET (DST Safe)
    // ---------------------------------------------------------

    @Column(name = "ORIGIN_UTC_OFFSET", length = 6)
    private String originUtcOffset; // e.g. +03:00

    @Column(name = "DESTINATION_UTC_OFFSET", length = 6)
    private String destinationUtcOffset;

    @Column(name = "OVERMIDNIGHT_INDICATOR")
    private Boolean overmidnightIndicator;

    @Column(name = "LAYOVER_TIME", length = 5)
    private String layoverTime;

    // ---------------------------------------------------------
    // OPERATIONAL FLAGS
    // ---------------------------------------------------------

    @Column(name = "SERVICE_TYPE", length = 1)
    private String serviceType;

    @Column(name = "PLANE_CHANGE_INDICATOR")
    private Boolean planeChangeIndicator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DUPLICATE_OF_LEG_ID")
    private ScheduleLeg duplicateOf;

    // ---------------------------------------------------------
    // RELATIONSHIPS
    // ---------------------------------------------------------

    @OneToMany(mappedBy = "scheduleLeg", cascade = CascadeType.ALL)
    private List<ScheduleSegment> segments;

    @OneToMany(mappedBy = "scheduleLeg", cascade = CascadeType.ALL)
    private List<ScheduleDei> deis;
}