package com.codeshare.airline.schedule.processing.application;

import com.codeshare.airline.platform.core.dto.schedule.workflow.ImportBatchDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ImportedScheduleDTO;
import com.codeshare.airline.platform.core.events.schedule.CompareRequestedEvent;
import com.codeshare.airline.platform.core.events.schedule.ProcessingRequestedEvent;
import com.codeshare.airline.schedule.processing.application.validation.BusinessValidationResult;
import com.codeshare.airline.schedule.processing.application.validation.ScheduleBusinessValidationService;
import com.codeshare.airline.schedule.processing.domain.entity.ProcessingBusinessErrorEntity;
import com.codeshare.airline.schedule.processing.domain.entity.ProcessingJobEntity;
import com.codeshare.airline.schedule.processing.domain.enums.ProcessingJobStatus;
import com.codeshare.airline.schedule.processing.domain.repository.ProcessingBusinessErrorRepository;
import com.codeshare.airline.schedule.processing.domain.repository.ProcessingJobRepository;
import com.codeshare.airline.schedule.processing.feign.ImportBatchClient;
import com.codeshare.airline.schedule.processing.integration.kafka.CompareRequestedEventPublisher;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@Transactional
public class ScheduleProcessingJobService {

    private final ImportBatchClient importBatchClient;
    private final ProcessingJobRepository processingJobRepository;
    private final ProcessingBusinessErrorRepository processingBusinessErrorRepository;
    private final ScheduleBusinessValidationService scheduleBusinessValidationService;
    private final CompareRequestedEventPublisher compareRequestedEventPublisher;

    public ScheduleProcessingJobService(
            ImportBatchClient importBatchClient,
            ProcessingJobRepository processingJobRepository,
            ProcessingBusinessErrorRepository processingBusinessErrorRepository,
            ScheduleBusinessValidationService scheduleBusinessValidationService,
            CompareRequestedEventPublisher compareRequestedEventPublisher
    ) {
        this.importBatchClient = importBatchClient;
        this.processingJobRepository = processingJobRepository;
        this.processingBusinessErrorRepository = processingBusinessErrorRepository;
        this.scheduleBusinessValidationService = scheduleBusinessValidationService;
        this.compareRequestedEventPublisher = compareRequestedEventPublisher;
    }

    public UUID process(ProcessingRequestedEvent event) {
        ProcessingJobEntity job = processingJobRepository.findByImportBatchId(event.getImportBatchId())
                .orElseGet(() -> createJob(event));

        if (job.getStatus() == ProcessingJobStatus.COMPARE_REQUESTED) {
            return job.getProcessingJobId();
        }

        try {
            job.setStatus(ProcessingJobStatus.VALIDATING);
            job.setStartedAt(job.getStartedAt() != null ? job.getStartedAt() : Instant.now());
            processingJobRepository.save(job);

            ImportedScheduleDTO importedSchedule = loadImportedSchedule(event);
            BusinessValidationResult validationResult = scheduleBusinessValidationService.validate(importedSchedule);
            processingBusinessErrorRepository.deleteByProcessingJob(job);
            if (validationResult.hasErrors()) {
                validationResult.issues().stream()
                        .map(issue -> ProcessingBusinessErrorEntity.builder()
                                .processingJob(job)
                                .ruleCode(issue.ruleCode())
                                .message(issue.message())
                                .recordType(issue.recordType())
                                .recordKey(issue.recordKey())
                                .build())
                        .forEach(processingBusinessErrorRepository::save);

                job.setStatus(ProcessingJobStatus.VALIDATION_FAILED);
                job.setErrorMessage("Business validation failed with " + validationResult.issues().size() + " error(s)");
                job.setCompletedAt(Instant.now());
                processingJobRepository.save(job);
                return job.getProcessingJobId();
            }

            job.setStatus(ProcessingJobStatus.VALIDATED);
            job.setCompletedAt(Instant.now());
            processingJobRepository.save(job);

            UUID compareRequestId = UUID.randomUUID();
            compareRequestedEventPublisher.publish(CompareRequestedEvent.builder()
                    .correlationId(event.getCorrelationId() != null ? event.getCorrelationId() : event.getProcessingRequestId())
                    .causationId(event.getProcessingRequestId())
                    .compareRequestId(compareRequestId)
                    .processingJobId(job.getProcessingJobId())
                    .importedScheduleId(job.getImportedScheduleId())
                    .importBatchId(job.getImportBatchId())
                    .messageType(job.getMessageType())
                    .airlineCode(job.getAirlineCode())
                    .partnerCode(job.getPartnerCode())
                    .sourceName(job.getSourceName())
                    .requestedAt(Instant.now())
                    .build());

            job.setStatus(ProcessingJobStatus.COMPARE_REQUESTED);
            processingJobRepository.save(job);
            return job.getProcessingJobId();
        } catch (Exception ex) {
            job.setStatus(ProcessingJobStatus.FAILED);
            job.setErrorMessage(ex.getMessage());
            job.setCompletedAt(Instant.now());
            processingJobRepository.save(job);
            throw ex;
        }
    }

    private ProcessingJobEntity createJob(ProcessingRequestedEvent event) {
        ProcessingJobEntity job = ProcessingJobEntity.builder()
                .processingJobId(UUID.randomUUID())
                .importedScheduleId(event.getImportedScheduleId())
                .importBatchId(event.getImportBatchId())
                .messageType(event.getMessageType())
                .airlineCode(event.getAirlineCode())
                .partnerCode(event.getPartnerCode())
                .sourceName(event.getSourceName())
                .requestedAt(event.getRequestedAt() != null ? event.getRequestedAt() : Instant.now())
                .status(ProcessingJobStatus.REQUESTED)
                .build();
        return processingJobRepository.save(job);
    }

    private ImportedScheduleDTO loadImportedSchedule(ProcessingRequestedEvent event) {
        ImportBatchDTO importBatch = importBatchClient.getImportBatch(event.getImportBatchId());
        if (importBatch != null && importBatch.getImportedSchedule() != null) {
            return importBatch.getImportedSchedule();
        }
        return importBatchClient.getImportedSchedule(event.getMessageType(), event.getImportedScheduleId());
    }
}
