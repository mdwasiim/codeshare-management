package com.codeshare.airline.ssim.inbound.persistence.inbound.repository;

import com.codeshare.airline.persistence.persistence.repository.CSMDataBaseRepository;
import com.codeshare.airline.ssim.inbound.persistence.inbound.entity.SsimInboundCarrier;

import java.util.UUID;

public interface SsimInboundCarrierRepository
        extends CSMDataBaseRepository<SsimInboundCarrier, UUID> {

}
