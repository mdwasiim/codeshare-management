package com.codeshare.airline.processor.persistence.repository;

import com.codeshare.airline.processor.model.raw.SsimR4DateVariationRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SsimR4DateVariationRepository
        extends JpaRepository<SsimR4DateVariationRecord, UUID> {
}
