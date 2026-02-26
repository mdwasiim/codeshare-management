package com.codeshare.airline.schedule.persistence.ssim.repository;

import com.codeshare.airline.persistence.persistence.repository.CSMDataBaseRepository;
import com.codeshare.airline.schedule.persistence.ssim.entity.SsimInboundHeader;

import java.util.UUID;

public interface SsimInboundHeaderRepository
        extends CSMDataBaseRepository<SsimInboundHeader, UUID> {

}
