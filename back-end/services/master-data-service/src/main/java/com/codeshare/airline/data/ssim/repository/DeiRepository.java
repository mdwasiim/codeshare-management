package com.codeshare.airline.data.ssim.repository;

import com.codeshare.airline.data.ssim.eitities.Dei;
import com.codeshare.airline.persistence.persistence.repository.CSMDataBaseRepository;

import java.util.Optional;
import java.util.UUID;

public interface DeiRepository
        extends CSMDataBaseRepository<Dei, UUID> {

    Optional<Dei> findByDeiNumber(String deiNumber);
}