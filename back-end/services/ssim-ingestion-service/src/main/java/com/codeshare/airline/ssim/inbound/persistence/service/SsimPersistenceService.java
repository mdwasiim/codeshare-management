package com.codeshare.airline.ssim.inbound.persistence.service;

import com.codeshare.airline.ssim.inbound.persistence.inbound.entity.SsimInboundFlightLeg;
import com.codeshare.airline.ssim.inbound.persistence.inbound.entity.SsimInboundSegmentDei;

import java.util.List;

public interface SsimPersistenceService {

    void persist(SsimInboundFlightLeg flight,
                 List<SsimInboundSegmentDei> deis);
}
