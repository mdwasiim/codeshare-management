package com.codeshare.airline.schedule.ingestion.orchestration.loader;

import com.codeshare.airline.core.enums.schedule.MessageType;
import com.codeshare.airline.schedule.ingestion.orchestration.handler.StreamExtractorHandler;
import com.codeshare.airline.schedule.ingestion.orchestration.parsers.MessageParser;
import com.codeshare.airline.schedule.ingestion.orchestration.processing.ProcessingStrategyFactory;
import com.codeshare.airline.schedule.ingestion.persistence.services.error.ErrorPersistenceService;
import com.codeshare.airline.schedule.ingestion.validation.orchestrator.BusinessValidationOrchestrator;
import com.codeshare.airline.schedule.ingestion.validation.orchestrator.StructuralValidationOrchestrator;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ScheduleMessageStreamLoader extends GenericStreamLoader {

    public ScheduleMessageStreamLoader(Map<MessageType, StreamExtractorHandler> extractorMap,
                                       Map<MessageType, MessageParser<?>> parserMap,
                                       StructuralValidationOrchestrator structuralValidationOrchestrator,
                                       BusinessValidationOrchestrator businessValidationOrchestrator,
                                       ProcessingStrategyFactory strategyFactory,
                                       ErrorPersistenceService errorService) {
        super(extractorMap, parserMap, structuralValidationOrchestrator, businessValidationOrchestrator,
                strategyFactory, errorService);
    }

    @Override
    protected boolean supports(MessageType type) {
        return type == MessageType.ASM || type == MessageType.SSM;
    }
}
