package com.codeshare.airline.schedule.ingestion.persistence.repositories.ssim;

import com.codeshare.airline.schedule.ingestion.persistence.entities.ssim.SsimDataElementEntity;
import com.codeshare.airline.data.repository.CSMDataBaseRepository;

import java.util.UUID;

public interface SsimDeiRepository
        extends CSMDataBaseRepository<SsimDataElementEntity, UUID> {

}
