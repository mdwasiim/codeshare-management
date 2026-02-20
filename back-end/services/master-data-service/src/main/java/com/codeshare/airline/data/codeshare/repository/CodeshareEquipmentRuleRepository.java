package com.codeshare.airline.data.codeshare.repository;

import com.codeshare.airline.data.codeshare.eitities.CodeshareEquipmentRule;
import com.codeshare.airline.persistence.persistence.repository.CSMDataBaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface CodeshareEquipmentRuleRepository
        extends CSMDataBaseRepository<CodeshareEquipmentRule, UUID> {

    // Get all equipment rules for a flight mapping
    List<CodeshareEquipmentRule> findByFlightMappingId(UUID flightMappingId);

    @Query("""
    SELECT r FROM CodeshareEquipmentRule r
    WHERE r.flightMapping.id = :mappingId
      AND r.effectiveFrom <= :date
      AND (r.effectiveTo IS NULL OR r.effectiveTo >= :date)
""")
    List<CodeshareEquipmentRule> findActiveRules(
            @Param("mappingId") UUID mappingId,
            @Param("date") LocalDate date
    );
}