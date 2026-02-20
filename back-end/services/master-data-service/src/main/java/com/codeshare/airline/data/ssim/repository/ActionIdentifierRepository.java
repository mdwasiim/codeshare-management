package com.codeshare.airline.data.ssim.repository;

import com.codeshare.airline.data.ssim.eitities.ActionIdentifier;
import com.codeshare.airline.persistence.persistence.repository.CSMDataBaseRepository;

import java.util.Optional;
import java.util.UUID;

public interface ActionIdentifierRepository extends CSMDataBaseRepository<ActionIdentifier, UUID> {

    boolean existsByActionCode(String actionCode);

    Optional<ActionIdentifier> findByActionCode(String actionCode);
}