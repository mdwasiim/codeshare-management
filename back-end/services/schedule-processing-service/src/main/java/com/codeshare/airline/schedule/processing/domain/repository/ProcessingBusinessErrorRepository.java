package com.codeshare.airline.schedule.processing.domain.repository;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.schedule.processing.domain.entity.ProcessingBusinessErrorEntity;
import com.codeshare.airline.schedule.processing.domain.entity.ProcessingJobEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProcessingBusinessErrorRepository extends CSMDataBaseRepository<ProcessingBusinessErrorEntity, Long> {

    List<ProcessingBusinessErrorEntity> findByProcessingJob(ProcessingJobEntity processingJob);

    void deleteByProcessingJob(ProcessingJobEntity processingJob);
}
