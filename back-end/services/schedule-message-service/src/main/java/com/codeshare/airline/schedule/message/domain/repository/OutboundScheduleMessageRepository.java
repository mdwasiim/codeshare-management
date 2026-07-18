package com.codeshare.airline.schedule.message.domain.repository;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.schedule.message.domain.entity.OutboundScheduleMessageEntity;

import java.util.Optional;
import java.util.UUID;

public interface OutboundScheduleMessageRepository extends CSMDataBaseRepository<OutboundScheduleMessageEntity, Long> {

    Optional<OutboundScheduleMessageEntity> findByChangeSetId(UUID changeSetId);

    Optional<OutboundScheduleMessageEntity> findByOutboundMessageId(UUID outboundMessageId);
}
