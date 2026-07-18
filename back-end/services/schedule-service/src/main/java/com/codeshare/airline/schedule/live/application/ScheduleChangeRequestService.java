package com.codeshare.airline.schedule.live.application;

import com.codeshare.airline.platform.core.dto.schedule.workflow.ChangeSetDTO;
import com.codeshare.airline.platform.core.enums.schedule.ApprovalMode;
import com.codeshare.airline.platform.core.enums.schedule.ApprovalStatus;
import com.codeshare.airline.platform.core.events.schedule.ChangeSetCreatedEvent;
import com.codeshare.airline.platform.core.events.schedule.ScheduleUpdatedEvent;
import com.codeshare.airline.schedule.live.domain.entity.ScheduleChangeRequestEntity;
import com.codeshare.airline.schedule.live.domain.repository.ScheduleChangeRequestRepository;
import com.codeshare.airline.schedule.live.feign.ProcessingChangeSetClient;
import com.codeshare.airline.schedule.live.integration.kafka.ScheduleUpdatedEventPublisher;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.UUID;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@Transactional
public class ScheduleChangeRequestService {

    private final ScheduleChangeRequestRepository changeRequestRepository;
    private final ProcessingChangeSetClient processingChangeSetClient;
    private final ScheduleChangeApplicationService changeApplicationService;
    private final ScheduleUpdatedEventPublisher scheduleUpdatedEventPublisher;
    private final ObjectMapper objectMapper;
    private final ScheduleApprovalPolicy scheduleApprovalPolicy;

    public ScheduleChangeRequestService(
            ScheduleChangeRequestRepository changeRequestRepository,
            ProcessingChangeSetClient processingChangeSetClient,
            ScheduleChangeApplicationService changeApplicationService,
            ScheduleUpdatedEventPublisher scheduleUpdatedEventPublisher,
            ObjectMapper objectMapper,
            ScheduleApprovalPolicy scheduleApprovalPolicy
    ) {
        this.changeRequestRepository = changeRequestRepository;
        this.processingChangeSetClient = processingChangeSetClient;
        this.changeApplicationService = changeApplicationService;
        this.scheduleUpdatedEventPublisher = scheduleUpdatedEventPublisher;
        this.objectMapper = objectMapper;
        this.scheduleApprovalPolicy = scheduleApprovalPolicy;
    }

    public void register(ChangeSetCreatedEvent event) {
        if (changeRequestRepository.findByChangeSetId(event.getChangeSetId()).isPresent()) {
            return;
        }

        ChangeSetDTO changeSet = processingChangeSetClient.getChangeSet(event.getChangeSetId());
        ApprovalMode approvalMode = resolveApprovalMode(changeSet, event);
        ScheduleChangeRequestEntity request = ScheduleChangeRequestEntity.builder()
                .changeSetId(changeSet.getChangeSetId())
                .sourceFileId(changeSet.getImportedScheduleId())
                .sourceLoadId(changeSet.getImportBatchId())
                .messageType(changeSet.getMessageType())
                .airlineCode(changeSet.getAirlineCode())
                .partnerCode(changeSet.getPartnerCode() != null ? changeSet.getPartnerCode() : event.getPartnerCode())
                .approvalMode(approvalMode)
                .approvalStatus(initialStatus(approvalMode))
                .changeSetPayload(writeJson(changeSet))
                .build();

        changeRequestRepository.save(request);

        if (approvalMode == ApprovalMode.AUTO) {
            apply(request, changeSet, event.getCorrelationId(), event.getChangeSetId(), "system:auto");
        }
    }

    public ScheduleChangeRequestEntity approve(UUID changeSetId, String approvedBy) {
        ScheduleChangeRequestEntity request = getByChangeSetId(changeSetId);
        ChangeSetDTO changeSet = readChangeSet(request.getChangeSetPayload());
        request.setApprovalStatus(ApprovalStatus.APPROVED);
        request.setApprovedAt(Instant.now());
        request.setApprovedBy(approvedBy == null || approvedBy.isBlank() ? "manual" : approvedBy.trim());
        changeRequestRepository.save(request);
        apply(request, changeSet, changeSet.getChangeSetId(), changeSet.getChangeSetId(), request.getApprovedBy());
        return request;
    }

    public ScheduleChangeRequestEntity getByChangeSetId(UUID changeSetId) {
        return changeRequestRepository.findByChangeSetId(changeSetId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Change request not found: " + changeSetId));
    }

    private void apply(
            ScheduleChangeRequestEntity request,
            ChangeSetDTO changeSet,
            UUID correlationId,
            UUID causationId,
            String appliedBy
    ) {
        try {
            changeApplicationService.apply(changeSet);
            request.setApprovalStatus(ApprovalStatus.APPLIED);
            request.setAppliedAt(Instant.now());
            request.setApprovedBy(appliedBy);
            changeRequestRepository.save(request);
            scheduleUpdatedEventPublisher.publish(ScheduleUpdatedEvent.builder()
                    .correlationId(correlationId != null ? correlationId : changeSet.getChangeSetId())
                    .causationId(causationId)
                    .changeSetId(changeSet.getChangeSetId())
                    .changeRequestId(request.getId())
                    .importedScheduleId(changeSet.getImportedScheduleId())
                    .importBatchId(changeSet.getImportBatchId())
                    .messageType(changeSet.getMessageType())
                    .airlineCode(changeSet.getAirlineCode())
                    .partnerCode(request.getPartnerCode())
                    .updatedAt(request.getAppliedAt())
                    .build());
        } catch (Exception ex) {
            request.setApprovalStatus(ApprovalStatus.FAILED);
            request.setErrorMessage(ex.getMessage());
            changeRequestRepository.save(request);
            throw ex;
        }
    }

    private ApprovalMode resolveApprovalMode(ChangeSetDTO changeSet, ChangeSetCreatedEvent event) {
        if (changeSet.getAcceptanceMode() != null) {
            return changeSet.getAcceptanceMode();
        }
        if (event.getAcceptanceMode() != null) {
            return event.getAcceptanceMode();
        }
        return scheduleApprovalPolicy.approvalMode();
    }

    private ApprovalStatus initialStatus(ApprovalMode approvalMode) {
        return approvalMode == ApprovalMode.AUTO ? ApprovalStatus.APPROVED : ApprovalStatus.PENDING_APPROVAL;
    }

    private String writeJson(ChangeSetDTO changeSet) {
        try {
            return objectMapper.writeValueAsString(changeSet);
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Unable to serialize change set", ex);
        }
    }

    private ChangeSetDTO readChangeSet(String json) {
        try {
            return objectMapper.readValue(json, ChangeSetDTO.class);
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Unable to deserialize change set", ex);
        }
    }
}

