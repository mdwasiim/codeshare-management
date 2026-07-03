package com.codeshare.airline.schedule.ingestion.persistence.repositories.ssim;

import com.codeshare.airline.data.repository.CSMDataBaseRepository;
import com.codeshare.airline.schedule.ingestion.persistence.entities.ssim.SsimTrailerEntity;

import java.util.UUID;

public interface SsimTrailerRepository
        extends CSMDataBaseRepository<SsimTrailerEntity, UUID> {

}
