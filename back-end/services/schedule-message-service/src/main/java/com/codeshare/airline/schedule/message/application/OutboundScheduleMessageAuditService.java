package com.codeshare.airline.schedule.message.application;

import com.codeshare.airline.platform.core.events.schedule.ScheduleUpdatedEvent;
import com.codeshare.airline.schedule.message.domain.entity.OutboundScheduleMessageAuditEntity;
import com.codeshare.airline.schedule.message.domain.entity.OutboundScheduleMessageEntity;
import com.codeshare.airline.schedule.message.domain.enums.OutboundScheduleMessageAuditEventType;
import com.codeshare.airline.schedule.message.domain.repository.OutboundScheduleMessageAuditRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class OutboundScheduleMessageAuditService {

    private final OutboundScheduleMessageAuditRepository repository;

    public OutboundScheduleMessageAuditService(OutboundScheduleMessageAuditRepository repository) {
        this.repository = repository;
    }

    public void record(OutboundScheduleMessageEntity message,
                       ScheduleUpdatedEvent event,
                       OutboundScheduleMessageAuditEventType eventType,
                       String details) {

        repository.save(OutboundScheduleMessageAuditEntity.builder()
                .outboundMessageId(message.getOutboundMessageId())
                .changeSetId(message.getChangeSetId())
                .changeRequestId(message.getChangeRequestId())
                .importedScheduleId(message.getImportedScheduleId())
                .importBatchId(message.getImportBatchId())
                .messageType(message.getMessageType())
                .airlineCode(message.getAirlineCode())
                .partnerCode(message.getPartnerCode())
                .eventType(eventType)
                .eventAt(Instant.now())
                .correlationId(correlationId(event))
                .causationId(event != null ? event.getChangeSetId() : null)
                .details(details)
                .build());
    }

    private UUID correlationId(ScheduleUpdatedEvent event) {
        if (event == null) {
            return null;
        }
        return event.getCorrelationId() != null ? event.getCorrelationId() : event.getChangeSetId();
    }
}
