package com.codeshare.airline.ssim.processor.service;

import com.codeshare.airline.ssim.persistence.inbound.entity.SsimInboundFlightLeg;
import com.codeshare.airline.ssim.persistence.inbound.entity.SsimInboundSegmentDei;

import java.util.List;

public interface SsimPersistenceService {

    void persist(SsimInboundFlightLeg flight,
                 List<SsimInboundSegmentDei> deis);
}
