package com.codeshare.airline.processor.processing.persistence.repository;

import com.codeshare.airline.processor.model.raw.SsimInboundDatasetMetadata;
import com.codeshare.airline.processor.pipeline.enm.SsimLoadStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface SsimInboundDatasetRepository extends JpaRepository<SsimInboundDatasetMetadata, UUID> {

    void updateStatus(UUID datasetId, SsimLoadStatus status, Instant now);

    Optional<SsimInboundDatasetMetadata> findByDatasetSerialNumber(String datasetSerialNumber);
}
