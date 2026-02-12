package com.codeshare.airline.ssim.ingestion.contex;

import com.codeshare.airline.ssim.persistence.inbound.entity.SsimInboundFlightLeg;
import com.codeshare.airline.ssim.persistence.inbound.entity.SsimInboundSegmentDei;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
public class FlightContext {
    SsimInboundFlightLeg flight;

    @Builder.Default
    List<SsimInboundSegmentDei> deis = new ArrayList<>();
}
