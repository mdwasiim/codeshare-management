package com.codeshare.airline.ingestion.persistence.repositories.ssim;

import com.codeshare.airline.persistence.persistence.repository.CSMDataBaseRepository;
import com.codeshare.airline.ingestion.persistence.entities.ssim.SsimHeaderEntity;

import java.util.UUID;

public interface SsimHeaderRepository
        extends CSMDataBaseRepository<SsimHeaderEntity, UUID> {

}
