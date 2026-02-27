package com.codeshare.airline.schedule.parsing.ssim.dto;

import com.codeshare.airline.schedule.persistence.ssim.entity.SsimInboundFlightLeg;
import com.codeshare.airline.schedule.persistence.ssim.entity.SsimInboundSegmentDei;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ParsedFlight {

    private final String fileId;

    private final String flightNumber;
    private final String carrier;

    private final SsimInboundFlightLeg legData; // or specific T3 domain object
    private final List<SsimInboundSegmentDei> segments;
}
