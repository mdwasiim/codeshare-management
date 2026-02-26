package com.codeshare.airline.schedule.persistence.ssim.repository;

import com.codeshare.airline.persistence.persistence.repository.CSMDataBaseRepository;
import com.codeshare.airline.schedule.persistence.ssim.entity.SsimInboundSegmentDei;

import java.util.UUID;

public interface SsimInboundSegmentDeiRepository
        extends CSMDataBaseRepository<SsimInboundSegmentDei, UUID> {

}
