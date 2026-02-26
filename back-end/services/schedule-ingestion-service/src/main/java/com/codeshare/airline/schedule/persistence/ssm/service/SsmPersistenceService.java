package com.codeshare.airline.schedule.persistence.ssm.service;

import com.codeshare.airline.schedule.persistence.ssim.entity.SsimInboundFlightLeg;
import com.codeshare.airline.schedule.persistence.ssim.entity.SsimInboundSegmentDei;

import java.util.List;

public interface SsmPersistenceService {

    void persist(SsimInboundFlightLeg flight,
                 List<SsimInboundSegmentDei> deis);
}
