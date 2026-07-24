package com.codeshare.airline.platform.core.dto.schedule.workflow;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleCodeshareSnapshotDTO {
    private Long codeshareId;
    private Integer sequenceOrder;
    private String marketingAirlineCode;
    private String marketingFlightNumber;
    private String marketingOperationalSuffix;
    private String boardPoint;
    private String offPoint;
    private String marketingBookingDesignator;
    private String sourceDeiCode;
    private boolean codeshare;
}
