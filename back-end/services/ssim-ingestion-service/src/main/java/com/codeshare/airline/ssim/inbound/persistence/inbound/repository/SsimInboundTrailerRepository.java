package com.codeshare.airline.ssim.inbound.persistence.inbound.repository;

import com.codeshare.airline.persistence.persistence.repository.CSMDataBaseRepository;
import com.codeshare.airline.ssim.inbound.persistence.inbound.entity.SsimInboundTrailer;

import java.util.UUID;

public interface SsimInboundTrailerRepository
        extends CSMDataBaseRepository<SsimInboundTrailer, UUID> {

}
