package com.codeshare.airline.schedule.ingestion.persistence.repositories.ssim;

import com.codeshare.airline.data.repository.CSMDataBaseRepository;
import com.codeshare.airline.schedule.ingestion.persistence.entities.ssim.SsimCarrierEntity;

import java.util.UUID;

public interface SsimCarrierRepository
        extends CSMDataBaseRepository<SsimCarrierEntity, UUID> {

}
