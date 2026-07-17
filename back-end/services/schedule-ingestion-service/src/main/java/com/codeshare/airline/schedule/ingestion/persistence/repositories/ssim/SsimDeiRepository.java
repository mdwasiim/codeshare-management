package com.codeshare.airline.schedule.ingestion.persistence.repositories.ssim;

import com.codeshare.airline.schedule.ingestion.persistence.entities.ssim.SsimDataElementEntity;
import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;


public interface SsimDeiRepository
        extends CSMDataBaseRepository<SsimDataElementEntity, Long> {

}
