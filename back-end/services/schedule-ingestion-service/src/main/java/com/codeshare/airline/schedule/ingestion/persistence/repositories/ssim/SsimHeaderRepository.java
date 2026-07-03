package com.codeshare.airline.schedule.ingestion.persistence.repositories.ssim;

import com.codeshare.airline.data.repository.CSMDataBaseRepository;
import com.codeshare.airline.schedule.ingestion.persistence.entities.ssim.SsimHeaderEntity;

import java.util.UUID;

public interface SsimHeaderRepository
        extends CSMDataBaseRepository<SsimHeaderEntity, UUID> {

}
