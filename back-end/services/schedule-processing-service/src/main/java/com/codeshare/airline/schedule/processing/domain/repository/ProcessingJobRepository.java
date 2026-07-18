package com.codeshare.airline.schedule.processing.domain.repository;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.schedule.processing.domain.entity.ProcessingJobEntity;
import com.codeshare.airline.schedule.processing.domain.enums.ProcessingJobStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProcessingJobRepository extends CSMDataBaseRepository<ProcessingJobEntity, Long> {

    Optional<ProcessingJobEntity> findByProcessingJobId(UUID processingJobId);

    Optional<ProcessingJobEntity> findByImportBatchId(UUID importBatchId);

    List<ProcessingJobEntity> findByAirlineCodeAndStatus(String airlineCode, ProcessingJobStatus status);
}
