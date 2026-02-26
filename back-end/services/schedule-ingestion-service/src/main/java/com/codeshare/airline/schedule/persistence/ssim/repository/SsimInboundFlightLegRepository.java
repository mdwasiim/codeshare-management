package com.codeshare.airline.schedule.persistence.ssim.repository;

import com.codeshare.airline.persistence.persistence.repository.CSMDataBaseRepository;
import com.codeshare.airline.schedule.persistence.ssim.entity.SsimInboundFlightLeg;

import java.util.UUID;

public interface SsimInboundFlightLegRepository
        extends CSMDataBaseRepository<SsimInboundFlightLeg, UUID> {

}
