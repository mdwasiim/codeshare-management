package com.codeshare.airline.master.scheduling.eitities;

import com.codeshare.airline.master.messaging.eitities.ScheduleMessage;
import com.codeshare.airline.data.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(
        name = "SCHEDULE_FLIGHT",
        indexes = {
                @Index(name = "IDX_FLIGHT_KEY",
                        columnList = "AIRLINE_DESIGNATOR,FLIGHT_NUMBER,PERIOD_FROM,PERIOD_TO")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleFlight extends CSMDataAbstractEntity {

    // ---------------------------------------------------------
    // RELATIONSHIP
    // ---------------------------------------------------------

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MESSAGE_ID", nullable = false)
    private ScheduleMessage scheduleMessage;

    // ---------------------------------------------------------
    // IDENTIFICATION
    // ---------------------------------------------------------

    @Column(name = "AIRLINE_DESIGNATOR", nullable = false, length = 3)
    private String airlineDesignator;

    @Column(name = "FLIGHT_NUMBER", nullable = false, length = 5)
    private String flightNumber;

    @Column(name = "OPERATIONAL_SUFFIX", length = 1)
    private String operationalSuffix;

    @Column(name = "FLIGHT_CHANGE_IDENTIFIER", length = 20)
    private String flightChangeIdentifier;

    // ---------------------------------------------------------
    // VALIDITY
    // ---------------------------------------------------------

    @Column(name = "PERIOD_FROM")
    private LocalDate periodFrom;

    @Column(name = "PERIOD_TO")
    private LocalDate periodTo;

    @Column(name = "DAYS_OF_OPERATION", length = 7)
    private String daysOfOperation;

    @Column(name = "FREQUENCY_RATE", length = 10)
    private String frequencyRate;

    @Column(name = "SEASON", length = 10)
    private String season;

    // ---------------------------------------------------------
    // ACTION / STATUS
    // ---------------------------------------------------------

    @Column(name = "ACTION_IDENTIFIER", length = 10)
    private String actionIdentifier; // NEW / CNL / RPL / EQT / TIM

    @Column(name = "SCHEDULE_STATUS", length = 20)
    private String scheduleStatus;

    // ---------------------------------------------------------
    // OPERATIONAL CHARACTERISTICS
    // ---------------------------------------------------------

    @Column(name = "SERVICE_TYPE", length = 1)
    private String serviceType;

    @Column(name = "AIRCRAFT_CONFIGURATION_VERSION", length = 10)
    private String aircraftConfigurationVersion;

    @Column(name = "SUBJECT_TO_GOVERNMENT_APPROVAL")
    private Boolean subjectToGovernmentApproval;

    @Column(name = "MIN_CONNECTING_TIME_OVERRIDE", length = 10)
    private String minimumConnectingTimeOverride;

    @Column(name = "UTC_VARIATION", length = 5)
    private String utcVariation;

    // ---------------------------------------------------------
    // RELATIONSHIPS
    // ---------------------------------------------------------

    @OneToMany(mappedBy = "scheduleFlight", cascade = CascadeType.ALL)
    private List<ScheduleCarrierContext> carrierContexts;

    @OneToMany(mappedBy = "scheduleFlight", cascade = CascadeType.ALL)
    private List<ScheduleLeg> legs;

    @OneToMany(mappedBy = "scheduleFlight", cascade = CascadeType.ALL)
    private List<ScheduleDei> deis;
}