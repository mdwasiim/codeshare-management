package com.codeshare.airline.schedule.ingestion.persistence.repositories.ssim;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.schedule.ingestion.persistence.entities.ssim.SsimCarrierEntity;


public interface SsimCarrierRepository
        extends CSMDataBaseRepository<SsimCarrierEntity, Long> {

}
