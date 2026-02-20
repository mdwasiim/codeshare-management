package com.codeshare.airline.ssim.inbound.persistence.inbound.repository;

import com.codeshare.airline.persistence.persistence.repository.CSMDataBaseRepository;
import com.codeshare.airline.ssim.inbound.persistence.inbound.entity.SsimInboundSegmentDei;

import java.util.UUID;

public interface SsimInboundSegmentDeiRepository
        extends CSMDataBaseRepository<SsimInboundSegmentDei, UUID> {

}
