package com.codeshare.airline.schedule.message.application;

import com.codeshare.airline.platform.core.dto.schedule.workflow.ChangeSetDTO;
import com.codeshare.airline.platform.core.events.schedule.DistributionRequestedEvent;
import com.codeshare.airline.platform.core.events.schedule.ScheduleUpdatedEvent;
import com.codeshare.airline.schedule.message.domain.entity.OutboundScheduleMessageEntity;
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
    private final OutboundScheduleMessageGenerator generator;
    private final DistributionRequestedEventPublisher distributionRequestedEventPublisher;

    public ScheduleMessageService(
            OutboundScheduleMessageRepository repository,
            CompareChangeSetClient compareChangeSetClient,
            ScheduleCodeListValidator codeListValidator,
            OutboundScheduleMessageGenerator generator,
            DistributionRequestedEventPublisher distributionRequestedEventPublisher
    ) {
        this.repository = repository;
        this.compareChangeSetClient = compareChangeSetClient;
        this.codeListValidator = codeListValidator;
        this.generator = generator;
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

        distributionRequestedEventPublisher.publish(DistributionRequestedEvent.builder()
                .correlationId(event.getCorrelationId() != null ? event.getCorrelationId() : event.getChangeSetId())
                .causationId(event.getChangeSetId())
                .distributionRequestId(UUID.randomUUID())
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
        return message.getOutboundMessageId();
    }

    private OutboundScheduleMessageEntity createMessage(ScheduleUpdatedEvent event) {
        try {
            ChangeSetDTO changeSet = compareChangeSetClient.getChangeSet(event.getChangeSetId());
            codeListValidator.validate(changeSet);
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
            return repository.save(message);
        } catch (OutboundScheduleMessageComplianceException ex) {
            OutboundScheduleMessageEntity failed = failedMessage(event, ex.getMessage());
            repository.save(failed);
            return failed;
        } catch (Exception ex) {
            repository.save(failedMessage(event, ex.getMessage()));
            throw ex;
        }
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
