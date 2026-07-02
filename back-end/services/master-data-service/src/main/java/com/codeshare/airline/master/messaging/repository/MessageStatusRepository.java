package com.codeshare.airline.master.messaging.repository;

import com.codeshare.airline.data.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.messaging.entities.MessageStatus;

import java.util.Optional;
import java.util.UUID;

public interface MessageStatusRepository extends CSMDataBaseRepository<MessageStatus, UUID> {

    boolean existsByMessageStatusCode(String messageStatusCode);

    Optional<MessageStatus> findByMessageStatusCode(String messageStatusCode);
}
