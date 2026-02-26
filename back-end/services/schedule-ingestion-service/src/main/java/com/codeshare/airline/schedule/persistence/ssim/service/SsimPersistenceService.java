package com.codeshare.airline.schedule.persistence.ssim.service;

import com.codeshare.airline.schedule.persistence.ssim.entity.SsimInboundFlightLeg;
import com.codeshare.airline.schedule.persistence.ssim.entity.SsimInboundSegmentDei;

import java.util.List;

public interface SsimPersistenceService {

    void persist(SsimInboundFlightLeg flight,
                 List<SsimInboundSegmentDei> deis);
}
