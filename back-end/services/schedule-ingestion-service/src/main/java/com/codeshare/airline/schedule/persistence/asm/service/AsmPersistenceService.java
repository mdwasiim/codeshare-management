package com.codeshare.airline.schedule.persistence.asm.service;

import com.codeshare.airline.schedule.persistence.ssim.entity.SsimInboundFlightLeg;
import com.codeshare.airline.schedule.persistence.ssim.entity.SsimInboundSegmentDei;

import java.util.List;

public interface AsmPersistenceService {

    void persist(SsimInboundFlightLeg flight,
                 List<SsimInboundSegmentDei> deis);
}
