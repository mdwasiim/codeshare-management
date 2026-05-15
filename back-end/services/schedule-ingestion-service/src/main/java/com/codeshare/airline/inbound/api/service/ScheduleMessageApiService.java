package com.codeshare.airline.inbound.api.service;

import com.codeshare.airline.core.enums.MessageType;
import com.codeshare.airline.inbound.api.response.ScheduleMessageIngestionResponse;
import com.codeshare.airline.inbound.api.response.ScheduleMessageValidationResponse;
import com.codeshare.airline.inbound.domain.context.AbstractIngestionContext;
import com.codeshare.airline.inbound.domain.enums.ProcessingStatus;
import com.codeshare.airline.inbound.domain.enums.SourceType;
import com.codeshare.airline.inbound.dto.schedule.ScheduleFileMetaDataDTO;
import com.codeshare.airline.inbound.dto.schedule.ScheduleMessageDTO;
import com.codeshare.airline.inbound.orchestration.handler.StreamExtractorHandler;
import com.codeshare.airline.inbound.orchestration.parsers.MessageParser;
import com.codeshare.airline.inbound.orchestration.processor.ScheduleChapterProcessor;
import com.codeshare.airline.inbound.source.inbound.ScheduleSourceFile;
import com.codeshare.airline.inbound.validations.model.ValidationMessage;
import com.codeshare.airline.inbound.validations.model.ValidationResult;
import com.codeshare.airline.inbound.validations.orchestrator.BusinessValidationOrchestrator;
import com.codeshare.airline.inbound.validations.orchestrator.StructuralValidationOrchestrator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ScheduleMessageApiService {

    private final Map<MessageType, StreamExtractorHandler> extractorMap;
    private final Map<MessageType, MessageParser<?>> parserMap;
    private final StructuralValidationOrchestrator structuralValidationOrchestrator;
    private final BusinessValidationOrchestrator businessValidationOrchestrator;
    private final ScheduleChapterProcessor scheduleChapterProcessor;

    public ScheduleMessageValidationResponse validate(
            MessageType type,
            String airlineCode,
            String fileName,
            byte[] content
    ) {
        return validateOrParse(type, airlineCode, fileName, content, false);
    }

    public ScheduleMessageValidationResponse parse(
            MessageType type,
            String airlineCode,
            String fileName,
            byte[] content
    ) {
        return validateOrParse(type, airlineCode, fileName, content, true);
    }

    public ScheduleMessageIngestionResponse ingest(
            MessageType type,
            String airlineCode,
            String fileName,
            byte[] content
    ) {
        assertSupported(type);
        assertContent(content);

        UUID fileId = UUID.randomUUID();
        UUID loadId = UUID.randomUUID();
        String resolvedFileName = resolveFileName(fileName, type);

        ScheduleSourceFile sourceFile = ScheduleSourceFile.builder()
                .fileId(fileId)
                .loadId(loadId)
                .airlineCode(airlineCode)
                .fileName(resolvedFileName)
                .sourceType(SourceType.REST)
                .messageType(type)
                .fileSizeBytes((long) content.length)
                .checksum(sha256(content))
                .streamSupplier(() -> new ByteArrayInputStream(content))
                .build();

        ProcessingStatus status = scheduleChapterProcessor.process(sourceFile);

        return ScheduleMessageIngestionResponse.builder()
                .fileId(fileId)
                .loadId(loadId)
                .fileName(resolvedFileName)
                .airlineCode(airlineCode)
                .messageType(type)
                .status(status)
                .build();
    }

    public byte[] textToBytes(String content) {
        return content.getBytes(StandardCharsets.UTF_8);
    }

    @SuppressWarnings("unchecked")
    private ScheduleMessageValidationResponse validateOrParse(
            MessageType type,
            String airlineCode,
            String fileName,
            byte[] content,
            boolean includeParsedMessages
    ) {
        assertSupported(type);
        assertContent(content);

        StreamExtractorHandler extractor = extractorMap.get(type);
        MessageParser<?> parser = parserMap.get(type);
        if (extractor == null || parser == null) {
            throw new IllegalStateException("No extractor/parser registered for type=" + type);
        }

        ScheduleFileMetaDataDTO metadata = metadata(type, airlineCode, fileName, content);
        ValidationResult aggregate = new ValidationResult();
        List<ScheduleMessageDTO> parsedMessages = new ArrayList<>();
        int[] blockCount = {0};
        int[] parsedBlockCount = {0};

        extractor.extract(new ByteArrayInputStream(content), lines -> {
            blockCount[0]++;

            ValidationResult structural = structuralValidationOrchestrator.validate(
                    contextForStructural(type, metadata, lines)
            );
            aggregate.merge(structural);
            if (structural.hasErrors()) {
                return;
            }

            AbstractIngestionContext<?, ?> parsedContext;
            try {
                parsedContext = ((MessageParser<AbstractIngestionContext<?, ?>>) parser).parse(lines, metadata);
            } catch (Exception ex) {
                aggregate.add(ValidationMessage.parsingError(ex.getMessage()));
                return;
            }
            parsedBlockCount[0]++;

            ValidationResult business = businessValidationOrchestrator.validate(parsedContext);
            aggregate.merge(business);
            if (!business.hasErrors() && includeParsedMessages && parsedContext.getParsedData() instanceof ScheduleMessageDTO parsed) {
                parsedMessages.add(parsed);
            }
        });

        return ScheduleMessageValidationResponse.builder()
                .messageType(type)
                .fileName(resolveFileName(fileName, type))
                .airlineCode(airlineCode)
                .blockCount(blockCount[0])
                .parsedBlockCount(parsedBlockCount[0])
                .valid(!aggregate.hasErrors())
                .messages(aggregate.getMessages())
                .parsedMessages(includeParsedMessages ? parsedMessages : List.of())
                .build();
    }

    private AbstractIngestionContext<?, ?> contextForStructural(
            MessageType type,
            ScheduleFileMetaDataDTO metadata,
            List<String> lines
    ) {
        return switch (type) {
            case ASM -> com.codeshare.airline.inbound.domain.context.AsmIngestionContext.builder()
                    .messageType(MessageType.ASM)
                    .metadata(metadata)
                    .messageLines(lines)
                    .build();
            case SSM -> com.codeshare.airline.inbound.domain.context.SsmIngestionContext.builder()
                    .messageType(MessageType.SSM)
                    .metadata(metadata)
                    .messageLines(lines)
                    .build();
            case SSIM -> throw new IllegalArgumentException("SSIM is not supported by this API");
        };
    }

    private ScheduleFileMetaDataDTO metadata(MessageType type, String airlineCode, String fileName, byte[] content) {
        return ScheduleFileMetaDataDTO.builder()
                .fileId(UUID.randomUUID())
                .loadId(UUID.randomUUID())
                .fileName(resolveFileName(fileName, type))
                .messageType(type)
                .sourceType(SourceType.REST)
                .airlineCode(airlineCode)
                .fileSizeBytes((long) content.length)
                .checksum(sha256(content))
                .receivedAt(Instant.now())
                .build();
    }

    private void assertSupported(MessageType type) {
        if (type != MessageType.ASM && type != MessageType.SSM) {
            throw new IllegalArgumentException("Only ASM and SSM are supported by this API");
        }
    }

    private void assertContent(byte[] content) {
        if (content == null || content.length == 0) {
            throw new IllegalArgumentException("Schedule content is empty");
        }
    }

    private String resolveFileName(String fileName, MessageType type) {
        if (fileName == null || fileName.isBlank()) {
            return type.name().toLowerCase() + "-manual-upload.txt";
        }
        return fileName;
    }

    private String sha256(byte[] content) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(digest.digest(content));
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("SHA-256 is not available", ex);
        }
    }
}
