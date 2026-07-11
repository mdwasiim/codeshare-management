package com.codeshare.airline.master.messaging.repository;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.messaging.entities.RejectReason;

import java.util.Optional;
import java.util.UUID;

public interface RejectReasonRepository extends CSMDataBaseRepository<RejectReason, UUID> {

    boolean existsByRejectReasonCode(String rejectReasonCode);

    Optional<RejectReason> findByRejectReasonCode(String rejectReasonCode);
}
