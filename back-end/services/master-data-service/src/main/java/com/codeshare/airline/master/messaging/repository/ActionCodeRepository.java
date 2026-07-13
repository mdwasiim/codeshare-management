package com.codeshare.airline.master.messaging.repository;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.messaging.entities.ActionCode;

import java.util.Optional;

public interface ActionCodeRepository extends CSMDataBaseRepository<ActionCode, Long> {

    boolean existsByActionCode(String actionCode);

    Optional<ActionCode> findByActionCode(String actionCode);
}
