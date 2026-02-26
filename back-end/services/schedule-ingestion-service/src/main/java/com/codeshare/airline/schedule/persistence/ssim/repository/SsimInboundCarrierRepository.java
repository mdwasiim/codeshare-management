package com.codeshare.airline.schedule.persistence.ssim.repository;

import com.codeshare.airline.persistence.persistence.repository.CSMDataBaseRepository;
import com.codeshare.airline.schedule.persistence.ssim.entity.SsimInboundCarrier;

import java.util.UUID;

public interface SsimInboundCarrierRepository
        extends CSMDataBaseRepository<SsimInboundCarrier, UUID> {

}
