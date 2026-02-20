package com.codeshare.airline.data.ssim.repository;

import com.codeshare.airline.data.ssim.eitities.MessageType;
import com.codeshare.airline.persistence.persistence.repository.CSMDataBaseRepository;

import java.util.Optional;
import java.util.UUID;

public interface MessageTypeRepository extends CSMDataBaseRepository<MessageType, UUID> {

    boolean existsByMessageTypeCode(String messageTypeCode);

    Optional<MessageType> findByMessageTypeCode(String messageTypeCode);
}