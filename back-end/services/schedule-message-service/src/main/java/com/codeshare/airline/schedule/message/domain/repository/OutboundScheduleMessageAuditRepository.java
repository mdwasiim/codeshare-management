package com.codeshare.airline.schedule.message.domain.repository;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.schedule.message.domain.entity.OutboundScheduleMessageAuditEntity;

public interface OutboundScheduleMessageAuditRepository
        extends CSMDataBaseRepository<OutboundScheduleMessageAuditEntity, Long> {
}
