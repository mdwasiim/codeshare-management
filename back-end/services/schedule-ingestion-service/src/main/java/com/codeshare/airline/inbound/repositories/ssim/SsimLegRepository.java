package com.codeshare.airline.inbound.repositories.ssim;

import com.codeshare.airline.data.repository.CSMDataBaseRepository;
import com.codeshare.airline.inbound.entities.ssim.SsimFlightEntity;

import java.util.UUID;

public interface SsimLegRepository
        extends CSMDataBaseRepository<SsimFlightEntity, UUID> {

}
