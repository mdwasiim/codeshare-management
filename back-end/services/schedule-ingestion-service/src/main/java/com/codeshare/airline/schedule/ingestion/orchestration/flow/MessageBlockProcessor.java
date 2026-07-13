package com.codeshare.airline.schedule.ingestion.orchestration.flow;

import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import com.codeshare.airline.schedule.ingestion.application.workflow.ScheduleMessageWorkflowResult;
import com.codeshare.airline.schedule.ingestion.application.workflow.ScheduleMessageWorkflowService;
import com.codeshare.airline.schedule.ingestion.domain.context.AbstractIngestionContext;
import com.codeshare.airline.schedule.ingestion.domain.enums.ValidationStage;
import com.codeshare.airline.schedule.ingestion.dto.schedule.ScheduleFileMetaDataDTO;
import com.codeshare.airline.schedule.ingestion.orchestration.processing.ProcessingStrategy;
import com.codeshare.airline.schedule.ingestion.orchestration.processing.ProcessingStrategyFactory;
import com.codeshare.airline.schedule.ingestion.persistence.services.error.ErrorPersistenceService;
import com.codeshare.airline.schedule.ingestion.validation.model.ValidationMessage;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class MessageBlockProcessor {

    private final ScheduleMessageWorkflowService workflowService;
    private final ProcessingStrategyFactory strategyFactory;
    private final ErrorPersistenceService errorService;

    public MessageBlockProcessor(
            ScheduleMessageWorkflowService workflowService,
            ProcessingStrategyFactory strategyFactory,
            ErrorPersistenceService errorService
    ) {
        this.workflowService = workflowService;
        this.strategyFactory = strategyFactory;
        this.errorService = errorService;
    }

    public void process(List<String> lines, ScheduleFileMetaDataDTO metadata, MessageType type, AtomicBoolean hasErrors) {
        ScheduleMessageWorkflowResult workflowResult = workflowService.process(type, metadata, lines);
        if (workflowResult.hasErrors()) {
            hasErrors.set(true);
            errorService.persist(
                    workflowResult.getErrorContext(),
                    workflowResult.getFailureStage(),
                    workflowResult.getMessages()
            );
            return;
        }

        AbstractIngestionContext<?, ?> parsedContext = workflowResult.getParsedContext();

        ProcessingStrategy<AbstractIngestionContext<?, ?>> strategy;
        try {
            strategy = strategyFactory.get(parsedContext);
        } catch (Exception ex) {
            hasErrors.set(true);
            errorService.persist(
                    parsedContext,
                    ValidationStage.PROCESSING,
                    List.of(ValidationMessage.systemError(ex.getMessage()))
            );
            return;
        }

        try {
            strategy.process(parsedContext);
        } catch (Exception ex) {
            hasErrors.set(true);
            errorService.persist(
                    parsedContext,
                    ValidationStage.PROCESSING,
                    List.of(ValidationMessage.systemError(ex.getMessage()))
            );
        }
    }

    public AbstractIngestionContext<?, ?> buildPreParseContext(MessageType type, ScheduleFileMetaDataDTO metadata, List<String> lines) {
        return workflowService.buildPreParseContext(type, metadata, lines);
    }
}
