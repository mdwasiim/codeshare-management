package com.codeshare.airline.schedule.ingestion.persistence.repositories.ssim;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.schedule.ingestion.persistence.entities.ssim.SsimHeaderEntity;


public interface SsimHeaderRepository
        extends CSMDataBaseRepository<SsimHeaderEntity, Long> {

}
