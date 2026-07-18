package com.codeshare.airline.schedule.message.application;

import com.codeshare.airline.platform.core.dto.schedule.workflow.ChangeSetDTO;
import com.codeshare.airline.platform.core.events.schedule.DistributionRequestedEvent;
import com.codeshare.airline.platform.core.events.schedule.ScheduleUpdatedEvent;
import com.codeshare.airline.schedule.message.domain.entity.OutboundScheduleMessageEntity;
import com.codeshare.airline.schedule.message.domain.enums.OutboundScheduleMessageAuditEventType;
import com.codeshare.airline.schedule.message.domain.enums.OutboundScheduleMessageStatus;
import com.codeshare.airline.schedule.message.domain.repository.OutboundScheduleMessageRepository;
import com.codeshare.airline.schedule.message.feign.CompareChangeSetClient;
import com.codeshare.airline.schedule.message.integration.kafka.DistributionRequestedEventPublisher;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@Transactional
public class ScheduleMessageService {

    private final OutboundScheduleMessageRepository repository;
    private final CompareChangeSetClient compareChangeSetClient;
    private final ScheduleCodeListValidator codeListValidator;
    private final ScheduleTimeValidator timeValidator;
    private final OutboundScheduleMessageGenerator generator;
    private final OutboundScheduleMessageAuditService auditService;
    private final DistributionRequestedEventPublisher distributionRequestedEventPublisher;

    public ScheduleMessageService(
            OutboundScheduleMessageRepository repository,
            CompareChangeSetClient compareChangeSetClient,
            ScheduleCodeListValidator codeListValidator,
            ScheduleTimeValidator timeValidator,
            OutboundScheduleMessageGenerator generator,
            OutboundScheduleMessageAuditService auditService,
            DistributionRequestedEventPublisher distributionRequestedEventPublisher
    ) {
        this.repository = repository;
        this.compareChangeSetClient = compareChangeSetClient;
        this.codeListValidator = codeListValidator;
        this.timeValidator = timeValidator;
        this.generator = generator;
        this.auditService = auditService;
        this.distributionRequestedEventPublisher = distributionRequestedEventPublisher;
    }

    public UUID generate(ScheduleUpdatedEvent event) {
        OutboundScheduleMessageEntity message = repository.findByChangeSetId(event.getChangeSetId())
                .orElseGet(() -> createMessage(event));

        if (message.getStatus() == OutboundScheduleMessageStatus.DISTRIBUTION_REQUESTED) {
            return message.getOutboundMessageId();
        }

        if (message.getStatus() == OutboundScheduleMessageStatus.FAILED) {
            return message.getOutboundMessageId();
        }

        UUID distributionRequestId = UUID.randomUUID();
        distributionRequestedEventPublisher.publish(DistributionRequestedEvent.builder()
                .correlationId(event.getCorrelationId() != null ? event.getCorrelationId() : event.getChangeSetId())
                .causationId(event.getChangeSetId())
                .distributionRequestId(distributionRequestId)
                .outboundMessageId(message.getOutboundMessageId())
                .changeSetId(message.getChangeSetId())
                .changeRequestId(message.getChangeRequestId())
                .importedScheduleId(message.getImportedScheduleId())
                .importBatchId(message.getImportBatchId())
                .messageType(message.getMessageType())
                .airlineCode(message.getAirlineCode())
                .partnerCode(message.getPartnerCode())
                .requestedAt(Instant.now())
                .build());

        message.setStatus(OutboundScheduleMessageStatus.DISTRIBUTION_REQUESTED);
        message.setDistributionRequestedAt(Instant.now());
        repository.save(message);
        auditService.record(message, event, OutboundScheduleMessageAuditEventType.DISTRIBUTION_REQUESTED,
                "Distribution request published: " + distributionRequestId);
        return message.getOutboundMessageId();
    }

    private OutboundScheduleMessageEntity createMessage(ScheduleUpdatedEvent event) {
        try {
            ChangeSetDTO changeSet = compareChangeSetClient.getChangeSet(event.getChangeSetId());
            codeListValidator.validate(changeSet);
            timeValidator.validate(changeSet);
            OutboundScheduleMessageEntity message = OutboundScheduleMessageEntity.builder()
                    .outboundMessageId(UUID.randomUUID())
                    .changeSetId(event.getChangeSetId())
                    .changeRequestId(event.getChangeRequestId())
                    .importedScheduleId(event.getImportedScheduleId())
                    .importBatchId(event.getImportBatchId())
                    .messageType(event.getMessageType())
                    .airlineCode(event.getAirlineCode())
                    .partnerCode(event.getPartnerCode())
                    .payload(generator.generate(changeSet))
                    .generatedAt(Instant.now())
                    .status(OutboundScheduleMessageStatus.GENERATED)
                    .build();
            OutboundScheduleMessageEntity saved = repository.save(message);
            auditService.record(saved, event, OutboundScheduleMessageAuditEventType.PAYLOAD_GENERATED,
                    "Outbound payload generated");
            return saved;
        } catch (OutboundScheduleMessageComplianceException ex) {
            OutboundScheduleMessageEntity failed = failedMessage(event, ex.getMessage());
            OutboundScheduleMessageEntity saved = repository.save(failed);
            auditService.record(saved, event, failureEventType(ex.getMessage()), ex.getMessage());
            return saved;
        } catch (Exception ex) {
            OutboundScheduleMessageEntity failed = repository.save(failedMessage(event, ex.getMessage()));
            auditService.record(failed, event, OutboundScheduleMessageAuditEventType.GENERATION_FAILED, ex.getMessage());
            throw ex;
        }
    }

    private OutboundScheduleMessageAuditEventType failureEventType(String message) {
        if (message != null && message.contains("validation failure")) {
            return OutboundScheduleMessageAuditEventType.VALIDATION_FAILED;
        }
        return OutboundScheduleMessageAuditEventType.GENERATION_FAILED;
    }

    private OutboundScheduleMessageEntity failedMessage(ScheduleUpdatedEvent event, String errorMessage) {
        return OutboundScheduleMessageEntity.builder()
                .outboundMessageId(UUID.randomUUID())
                .changeSetId(event.getChangeSetId())
                .changeRequestId(event.getChangeRequestId())
                .importedScheduleId(event.getImportedScheduleId())
                .importBatchId(event.getImportBatchId())
                .messageType(event.getMessageType())
                .airlineCode(event.getAirlineCode())
                .partnerCode(event.getPartnerCode())
                .payload("GENERATION_FAILED")
                .generatedAt(Instant.now())
                .status(OutboundScheduleMessageStatus.FAILED)
                .errorMessage(errorMessage)
                .build();
    }
}
