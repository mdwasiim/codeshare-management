package com.codeshare.airline.ingestion.persistence.repositories.ssim;

import com.codeshare.airline.ingestion.persistence.entities.ssim.SsimDataElementEntity;
import com.codeshare.airline.persistence.persistence.repository.CSMDataBaseRepository;

import java.util.UUID;

public interface SsimDeiRepository
        extends CSMDataBaseRepository<SsimDataElementEntity, UUID> {

}
