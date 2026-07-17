package com.codeshare.airline.schedule.ingestion.api.service;

import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import com.codeshare.airline.schedule.ingestion.api.response.ScheduleMessageIngestionResponse;
import com.codeshare.airline.schedule.ingestion.api.response.ScheduleMessageValidationResponse;
import com.codeshare.airline.schedule.ingestion.application.workflow.ScheduleMessageWorkflowResult;
import com.codeshare.airline.schedule.ingestion.application.workflow.ScheduleMessageWorkflowService;
import com.codeshare.airline.schedule.ingestion.domain.context.AbstractIngestionContext;
import com.codeshare.airline.schedule.ingestion.domain.enums.ProcessingStatus;
import com.codeshare.airline.platform.core.enums.schedule.SourceType;
import com.codeshare.airline.schedule.ingestion.dto.schedule.ScheduleFileMetaDataDTO;
import com.codeshare.airline.schedule.ingestion.dto.schedule.ScheduleMessageDTO;
import com.codeshare.airline.schedule.ingestion.orchestration.handler.StreamExtractorHandler;
import com.codeshare.airline.schedule.ingestion.orchestration.processor.ScheduleChapterProcessor;
import com.codeshare.airline.schedule.ingestion.source.model.ScheduleSourceFile;
import com.codeshare.airline.schedule.ingestion.validation.model.ValidationResult;
import org.springframework.beans.factory.annotation.Qualifier;
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
public class ScheduleMessageApiService {

    private final Map<MessageType, StreamExtractorHandler> extractorMap;
    private final ScheduleMessageWorkflowService workflowService;
    private final ScheduleChapterProcessor scheduleChapterProcessor;

    public ScheduleMessageApiService(
            Map<MessageType, StreamExtractorHandler> extractorMap,
            ScheduleMessageWorkflowService workflowService,
            @Qualifier("scheduleMessageChapterProcessor") ScheduleChapterProcessor scheduleChapterProcessor
    ) {
        this.extractorMap = extractorMap;
        this.workflowService = workflowService;
        this.scheduleChapterProcessor = scheduleChapterProcessor;
    }

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
                .processingStatus(ProcessingStatus.RECEIVED)
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
        if (extractor == null) {
            throw new IllegalStateException("No extractor registered for type=" + type);
        }

        ScheduleFileMetaDataDTO metadata = metadata(type, airlineCode, fileName, content);
        ValidationResult aggregate = new ValidationResult();
        List<ScheduleMessageDTO> parsedMessages = new ArrayList<>();
        int[] blockCount = {0};
        int[] parsedBlockCount = {0};

        extractor.extract(new ByteArrayInputStream(content), lines -> {
            blockCount[0]++;
            ScheduleMessageWorkflowResult workflowResult = workflowService.process(type, metadata, lines);
            workflowResult.getMessages().forEach(aggregate::add);
            if (workflowResult.hasErrors()) {
                return;
            }

            AbstractIngestionContext<?, ?> parsedContext = workflowResult.getParsedContext();
            parsedBlockCount[0]++;

            if (includeParsedMessages && parsedContext.getParsedData() instanceof ScheduleMessageDTO parsed) {
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
