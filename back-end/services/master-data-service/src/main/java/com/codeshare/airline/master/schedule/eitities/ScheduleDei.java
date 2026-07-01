package com.codeshare.airline.master.schedule.eitities;

import com.codeshare.airline.data.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "SCHEDULE_DEI",
        indexes = {
                @Index(name = "IDX_DEI_NUMBER", columnList = "DEI_NUMBER"),
                @Index(name = "IDX_DEI_FLIGHT", columnList = "FLIGHT_ID"),
                @Index(name = "IDX_DEI_LEG", columnList = "LEG_ID"),
                @Index(name = "IDX_DEI_SEGMENT", columnList = "SEGMENT_ID"),
                @Index(name = "IDX_DEI_CARRIER", columnList = "CARRIER_CONTEXT_ID")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleDei extends CSMDataAbstractEntity {

    // ---------------------------------------------------------
    // IDENTIFICATION
    // ---------------------------------------------------------

    @Column(name = "DEI_NUMBER", nullable = false, length = 3)
    private String deiNumber;

    @Column(name = "DEI_VALUE", length = 2000)
    private String deiValue;

    @Column(name = "DEI_QUALIFIER", length = 50)
    private String deiQualifier; // for 710–712 style qualifiers (optional)

    // ---------------------------------------------------------
    // SCOPE RELATIONSHIPS
    // ---------------------------------------------------------

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CARRIER_CONTEXT_ID")
    private ScheduleCarrierContext carrierContext;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FLIGHT_ID")
    private ScheduleFlight scheduleFlight;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LEG_ID")
    private ScheduleLeg scheduleLeg;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SEGMENT_ID")
    private ScheduleSegment scheduleSegment;
}