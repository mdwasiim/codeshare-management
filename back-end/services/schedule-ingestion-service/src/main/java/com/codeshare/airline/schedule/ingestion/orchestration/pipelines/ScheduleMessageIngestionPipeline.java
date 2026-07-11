package com.codeshare.airline.schedule.ingestion.orchestration.pipelines;

import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import com.codeshare.airline.schedule.ingestion.orchestration.loader.ScheduleMessageStreamLoader;
import com.codeshare.airline.schedule.ingestion.persistence.services.common.ScheduleFileService;
import com.codeshare.airline.schedule.ingestion.persistence.services.error.ErrorPersistenceService;
import com.codeshare.airline.schedule.ingestion.validation.orchestrator.FileValidatorOrchestrator;
import org.springframework.stereotype.Component;

@Component
public class ScheduleMessageIngestionPipeline extends GenericIngestionPipeline {

    public ScheduleMessageIngestionPipeline(ScheduleMessageStreamLoader genericStreamLoader,
                                            ScheduleFileService scheduleService,
                                            FileValidatorOrchestrator fileValidatorOrchestrator,
                                            ErrorPersistenceService errorPersistenceService) {
        super(genericStreamLoader, scheduleService, fileValidatorOrchestrator, errorPersistenceService);
    }

    @Override
    protected boolean supports(MessageType type) {
        return type == MessageType.ASM || type == MessageType.SSM;
    }
}
