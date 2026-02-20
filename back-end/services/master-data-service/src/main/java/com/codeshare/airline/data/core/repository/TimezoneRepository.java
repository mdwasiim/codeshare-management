package com.codeshare.airline.data.core.repository;

import com.codeshare.airline.data.core.eitities.Timezone;
import com.codeshare.airline.persistence.persistence.repository.CSMDataBaseRepository;

import java.util.Optional;
import java.util.UUID;

public interface TimezoneRepository
        extends CSMDataBaseRepository<Timezone, UUID> {
    Optional<Timezone> findByTzIdentifier(String s);
}