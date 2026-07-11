package com.codeshare.airline.schedule.ingestion.orchestration.flow;

import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import com.codeshare.airline.schedule.ingestion.domain.context.AbstractIngestionContext;
import com.codeshare.airline.schedule.ingestion.domain.enums.ValidationStage;
import com.codeshare.airline.schedule.ingestion.dto.schedule.ScheduleFileMetaDataDTO;
import com.codeshare.airline.schedule.ingestion.orchestration.context.PreParseContextFactory;
import com.codeshare.airline.schedule.ingestion.orchestration.parsers.MessageParser;
import com.codeshare.airline.schedule.ingestion.orchestration.processing.ProcessingStrategy;
import com.codeshare.airline.schedule.ingestion.orchestration.processing.ProcessingStrategyFactory;
import com.codeshare.airline.schedule.ingestion.persistence.services.error.ErrorPersistenceService;
import com.codeshare.airline.schedule.ingestion.validation.model.ValidationMessage;
import com.codeshare.airline.schedule.ingestion.validation.model.ValidationResult;
import com.codeshare.airline.schedule.ingestion.validation.orchestrator.BusinessValidationOrchestrator;
import com.codeshare.airline.schedule.ingestion.validation.orchestrator.StructuralValidationOrchestrator;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class MessageBlockProcessor {

    private final Map<MessageType, MessageParser<?>> parserMap;
    private final Map<MessageType, PreParseContextFactory<?>> contextFactoryMap;
    private final StructuralValidationOrchestrator structuralValidationOrchestrator;
    private final BusinessValidationOrchestrator businessValidationOrchestrator;
    private final ProcessingStrategyFactory strategyFactory;
    private final ErrorPersistenceService errorService;

    public MessageBlockProcessor(
            Map<MessageType, MessageParser<?>> parserMap,
            Map<MessageType, PreParseContextFactory<?>> contextFactoryMap,
            StructuralValidationOrchestrator structuralValidationOrchestrator,
            BusinessValidationOrchestrator businessValidationOrchestrator,
            ProcessingStrategyFactory strategyFactory,
            ErrorPersistenceService errorService
    ) {
        this.parserMap = parserMap;
        this.contextFactoryMap = contextFactoryMap;
        this.structuralValidationOrchestrator = structuralValidationOrchestrator;
        this.businessValidationOrchestrator = businessValidationOrchestrator;
        this.strategyFactory = strategyFactory;
        this.errorService = errorService;
    }

    public void process(List<String> lines, ScheduleFileMetaDataDTO metadata, MessageType type, AtomicBoolean hasErrors) {
        MessageParser<?> parser = requiredParser(type);
        AbstractIngestionContext<?, ?> preParseContext = buildPreParseContext(type, metadata, lines);

        ValidationResult structural = structuralValidationOrchestrator.validate(preParseContext);
        if (structural.hasErrors()) {
            hasErrors.set(true);
            errorService.persist(preParseContext, ValidationStage.STRUCTURAL, structural.getMessages());
            return;
        }

        AbstractIngestionContext<?, ?> parsedContext;
        try {
            parsedContext = parser.parse(lines, metadata);
        } catch (Exception ex) {
            hasErrors.set(true);
            errorService.persist(
                    preParseContext,
                    ValidationStage.PARSING,
                    List.of(ValidationMessage.parsingError(ex.getMessage()))
            );
            return;
        }

        ValidationResult business = businessValidationOrchestrator.validate(parsedContext);
        if (business.hasErrors()) {
            hasErrors.set(true);
            errorService.persist(parsedContext, ValidationStage.VALIDATION, business.getMessages());
            return;
        }

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
        return requiredContextFactory(type).build(metadata, lines);
    }

    private MessageParser<?> requiredParser(MessageType type) {
        MessageParser<?> parser = parserMap.get(type);
        if (parser == null) {
            throw new IllegalStateException("No parser registered for type=" + type);
        }
        return parser;
    }

    private PreParseContextFactory<?> requiredContextFactory(MessageType type) {
        PreParseContextFactory<?> factory = contextFactoryMap.get(type);
        if (factory == null) {
            throw new IllegalStateException("No pre-parse context factory registered for type=" + type);
        }
        return factory;
    }
}
