package com.codeshare.airline.ssim.inbound.persistence.inbound.repository;

import com.codeshare.airline.persistence.persistence.repository.CSMDataBaseRepository;
import com.codeshare.airline.ssim.inbound.persistence.inbound.entity.SsimInboundFlightLeg;

import java.util.UUID;

public interface SsimInboundFlightLegRepository
        extends CSMDataBaseRepository<SsimInboundFlightLeg, UUID> {

}
