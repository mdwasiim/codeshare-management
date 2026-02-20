package com.codeshare.airline.ssim.inbound.persistence.inbound.repository;

import com.codeshare.airline.persistence.persistence.repository.CSMDataBaseRepository;
import com.codeshare.airline.ssim.inbound.persistence.inbound.entity.SsimInboundHeader;

import java.util.UUID;

public interface SsimInboundHeaderRepository
        extends CSMDataBaseRepository<SsimInboundHeader, UUID> {

}
