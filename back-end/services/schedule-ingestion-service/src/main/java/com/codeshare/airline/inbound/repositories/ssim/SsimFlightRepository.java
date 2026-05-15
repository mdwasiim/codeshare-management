package com.codeshare.airline.inbound.repositories.ssim;

import com.codeshare.airline.data.repository.CSMDataBaseRepository;
import com.codeshare.airline.inbound.entities.ssim.SsimFlightEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface SsimFlightRepository
        extends CSMDataBaseRepository<SsimFlightEntity, UUID>,
        JpaSpecificationExecutor<SsimFlightEntity> {
}
