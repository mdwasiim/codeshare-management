package com.codeshare.airline.data.airport.georegion.repository;

import com.codeshare.airline.data.airport.georegion.eitities.DstRule;
import com.codeshare.airline.persistence.persistence.repository.CSMDataBaseRepository;

import java.util.UUID;

public interface DstRuleRepository
        extends CSMDataBaseRepository<DstRule, UUID> {
}