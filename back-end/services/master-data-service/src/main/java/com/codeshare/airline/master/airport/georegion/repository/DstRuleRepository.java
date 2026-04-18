package com.codeshare.airline.master.airport.georegion.repository;

import com.codeshare.airline.master.airport.georegion.eitities.DstRule;
import com.codeshare.airline.persistence.repository.CSMDataBaseRepository;

import java.util.UUID;

public interface DstRuleRepository
        extends CSMDataBaseRepository<DstRule, UUID> {
}