package com.codeshare.airline.processor.persistence.repository;

import com.codeshare.airline.processor.model.raw.SsimR5ContinuationRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SsimR5DateVariationContinuationRepository
        extends JpaRepository<SsimR5ContinuationRecord, UUID> {
}
