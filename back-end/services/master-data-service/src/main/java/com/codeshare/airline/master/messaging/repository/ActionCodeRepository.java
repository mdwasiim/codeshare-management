package com.codeshare.airline.master.messaging.repository;

import com.codeshare.airline.data.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.messaging.entities.ActionCode;

import java.util.Optional;
import java.util.UUID;

public interface ActionCodeRepository extends CSMDataBaseRepository<ActionCode, UUID> {

    boolean existsByActionCode(String actionCode);

    Optional<ActionCode> findByActionCode(String actionCode);
}
