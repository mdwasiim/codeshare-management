package com.codeshare.airline.schedule.ingestion.persistence.repositories.ssim;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.schedule.ingestion.persistence.entities.ssim.SsimFlightEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface SsimFlightRepository
        extends CSMDataBaseRepository<SsimFlightEntity, UUID>,
        JpaSpecificationExecutor<SsimFlightEntity> {

    long countByCarrier_File_FileId(UUID fileId);
}
