package com.codeshare.airline.master.messaging.repository;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.messaging.entities.MessagePriority;

import java.util.Optional;

public interface MessagePriorityRepository extends CSMDataBaseRepository<MessagePriority, Long> {

    boolean existsByPriorityCode(String priorityCode);

    Optional<MessagePriority> findByPriorityCode(String priorityCode);
}
