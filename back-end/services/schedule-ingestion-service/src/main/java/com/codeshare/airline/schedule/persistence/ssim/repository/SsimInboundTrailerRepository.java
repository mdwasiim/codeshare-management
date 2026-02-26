package com.codeshare.airline.schedule.persistence.ssim.repository;

import com.codeshare.airline.persistence.persistence.repository.CSMDataBaseRepository;
import com.codeshare.airline.schedule.persistence.ssim.entity.SsimInboundTrailer;

import java.util.UUID;

public interface SsimInboundTrailerRepository
        extends CSMDataBaseRepository<SsimInboundTrailer, UUID> {

}
