package com.codeshare.airline.schedule.ingestion.application.workflow;

import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import com.codeshare.airline.schedule.ingestion.domain.context.AbstractIngestionContext;
import com.codeshare.airline.schedule.ingestion.domain.enums.ValidationStage;
import com.codeshare.airline.schedule.ingestion.dto.schedule.ScheduleFileMetaDataDTO;
import com.codeshare.airline.schedule.ingestion.orchestration.context.PreParseContextFactory;
import com.codeshare.airline.schedule.ingestion.orchestration.parsers.MessageParser;
import com.codeshare.airline.schedule.ingestion.validation.model.ValidationMessage;
import com.codeshare.airline.schedule.ingestion.validation.model.ValidationResult;
import com.codeshare.airline.schedule.ingestion.validation.orchestrator.BusinessValidationOrchestrator;
import com.codeshare.airline.schedule.ingestion.validation.orchestrator.StructuralValidationOrchestrator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ScheduleMessageWorkflowService {

    private final Map<MessageType, MessageParser<?>> parserMap;
    private final Map<MessageType, PreParseContextFactory<?>> contextFactoryMap;
    private final StructuralValidationOrchestrator structuralValidationOrchestrator;
    private final BusinessValidationOrchestrator businessValidationOrchestrator;
    private final boolean businessValidationEnabled;

    public ScheduleMessageWorkflowService(
            Map<MessageType, MessageParser<?>> parserMap,
            Map<MessageType, PreParseContextFactory<?>> contextFactoryMap,
            StructuralValidationOrchestrator structuralValidationOrchestrator,
            BusinessValidationOrchestrator businessValidationOrchestrator,
            @Value("${ingestion.validation.business-enabled:true}") boolean businessValidationEnabled
    ) {
        this.parserMap = parserMap;
        this.contextFactoryMap = contextFactoryMap;
        this.structuralValidationOrchestrator = structuralValidationOrchestrator;
        this.businessValidationOrchestrator = businessValidationOrchestrator;
        this.businessValidationEnabled = businessValidationEnabled;
    }

    public ScheduleMessageWorkflowResult process(
            MessageType type,
            ScheduleFileMetaDataDTO metadata,
            List<String> lines
    ) {
        MessageParser<?> parser = requiredParser(type);
        AbstractIngestionContext<?, ?> preParseContext = buildPreParseContext(type, metadata, lines);
        List<ValidationMessage> messages = new ArrayList<>();

        ValidationResult structural = structuralValidationOrchestrator.validate(preParseContext);
        messages.addAll(structural.getMessages());
        if (structural.hasErrors()) {
            return ScheduleMessageWorkflowResult.builder()
                    .errorContext(preParseContext)
                    .failureStage(ValidationStage.STRUCTURAL)
                    .messages(List.copyOf(messages))
                    .build();
        }

        AbstractIngestionContext<?, ?> parsedContext;
        try {
            parsedContext = parser.parse(lines, metadata);
        } catch (Exception ex) {
            messages.add(ValidationMessage.parsingError(ex.getMessage()));
            return ScheduleMessageWorkflowResult.builder()
                    .errorContext(preParseContext)
                    .failureStage(ValidationStage.PARSING)
                    .messages(List.copyOf(messages))
                    .build();
        }

        if (businessValidationEnabled) {
            ValidationResult business = businessValidationOrchestrator.validate(parsedContext);
            messages.addAll(business.getMessages());
            if (business.hasErrors()) {
                return ScheduleMessageWorkflowResult.builder()
                        .errorContext(parsedContext)
                        .parsedContext(parsedContext)
                        .failureStage(ValidationStage.VALIDATION)
                        .messages(List.copyOf(messages))
                        .build();
            }
        }

        return ScheduleMessageWorkflowResult.builder()
                .parsedContext(parsedContext)
                .messages(List.copyOf(messages))
                .build();
    }

    public AbstractIngestionContext<?, ?> buildPreParseContext(
            MessageType type,
            ScheduleFileMetaDataDTO metadata,
            List<String> lines
    ) {
        PreParseContextFactory<?> factory = requiredContextFactory(type);
        return factory.build(metadata, lines);
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
