package com.codeshare.airline.ssim.inbound.parsing.processor.dto;

import com.codeshare.airline.ssim.inbound.persistence.inbound.entity.SsimInboundFlightLeg;
import com.codeshare.airline.ssim.inbound.persistence.inbound.entity.SsimInboundSegmentDei;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ParsedFlightDTO {

    private final String fileId;

    private final String flightNumber;
    private final String carrier;

    private final SsimInboundFlightLeg legData; // or specific T3 domain object
    private final List<SsimInboundSegmentDei> segments;
}
