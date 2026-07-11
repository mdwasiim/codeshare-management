package com.codeshare.airline.master.messaging.repository;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.messaging.entities.MessagePriority;

import java.util.Optional;
import java.util.UUID;

public interface MessagePriorityRepository extends CSMDataBaseRepository<MessagePriority, UUID> {

    boolean existsByPriorityCode(String priorityCode);

    Optional<MessagePriority> findByPriorityCode(String priorityCode);
}
