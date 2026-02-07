package com.codeshare.airline.processor.processing.persistence.repository;

import com.codeshare.airline.processor.model.raw.SsimR3FlightLegRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SsimR3FlightLegRepository
        extends JpaRepository<SsimR3FlightLegRecord, UUID> {
}
