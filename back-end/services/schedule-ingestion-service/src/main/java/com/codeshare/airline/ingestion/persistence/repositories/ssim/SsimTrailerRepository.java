package com.codeshare.airline.ingestion.persistence.repositories.ssim;

import com.codeshare.airline.persistence.persistence.repository.CSMDataBaseRepository;
import com.codeshare.airline.ingestion.persistence.entities.ssim.SsimTrailerEntity;

import java.util.UUID;

public interface SsimTrailerRepository
        extends CSMDataBaseRepository<SsimTrailerEntity, UUID> {

}
