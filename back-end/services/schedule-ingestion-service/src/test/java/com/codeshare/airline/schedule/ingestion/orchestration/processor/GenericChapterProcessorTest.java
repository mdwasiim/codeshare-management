package com.codeshare.airline.schedule.ingestion.orchestration.processor;

import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import com.codeshare.airline.platform.core.enums.schedule.SourceType;
import com.codeshare.airline.schedule.ingestion.domain.enums.ProcessingStatus;
import com.codeshare.airline.schedule.ingestion.dto.schedule.ScheduleFileMetaDataDTO;
import com.codeshare.airline.schedule.ingestion.integration.kafka.ImportCompletedEventPublisher;
import com.codeshare.airline.schedule.ingestion.integration.kafka.ProcessingRequestedEventPublisher;
import com.codeshare.airline.schedule.ingestion.orchestration.pipelines.GenericIngestionPipeline;
import com.codeshare.airline.schedule.ingestion.persistence.services.common.ScheduleFileService;
import com.codeshare.airline.schedule.ingestion.shared.exceptions.BusinessValidationException;
import com.codeshare.airline.schedule.ingestion.source.model.ScheduleSourceFile;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GenericChapterProcessorTest {

    private final GenericIngestionPipeline pipeline = mock(GenericIngestionPipeline.class);
    private final ScheduleFileService scheduleFileService = mock(ScheduleFileService.class);
    private final ImportCompletedEventPublisher importCompletedEventPublisher = mock(ImportCompletedEventPublisher.class);
    private final ProcessingRequestedEventPublisher processingRequestedEventPublisher = mock(ProcessingRequestedEventPublisher.class);
    private final GenericChapterProcessor processor = new TestChapterProcessor(
            pipeline,
            scheduleFileService,
            importCompletedEventPublisher,
            processingRequestedEventPublisher
    );

    @Test
    void processDoesNotPublishWorkflowEventsWhenIngestionIsPartial() {
        ScheduleSourceFile sourceFile = ScheduleSourceFile.builder()
                .fileId(UUID.randomUUID())
                .fileName("sample_SSIM.ssim")
                .airlineCode("QR")
                .sourceType(SourceType.SFTP)
                .messageType(MessageType.SSIM)
                .build();
        ScheduleFileMetaDataDTO metadata = ScheduleFileMetaDataDTO.builder()
                .fileId(UUID.randomUUID())
                .fileName("sample_SSIM.ssim")
                .airlineCode("QR")
                .sourceType(SourceType.SFTP)
                .messageType(MessageType.SSIM)
                .processingStatus(ProcessingStatus.RECEIVED)
                .build();

        when(scheduleFileService.createInbound(sourceFile, MessageType.SSIM)).thenReturn(metadata);
        when(pipeline.execute(sourceFile, metadata, MessageType.SSIM)).thenReturn(ProcessingStatus.PARTIAL);

        assertThatThrownBy(() -> processor.process(sourceFile))
                .isInstanceOf(BusinessValidationException.class)
                .hasMessageContaining("status=PARTIAL");

        verify(processingRequestedEventPublisher, never()).publish(metadata);
        verify(importCompletedEventPublisher, never()).publish(metadata);
    }

    private static class TestChapterProcessor extends GenericChapterProcessor {

        private TestChapterProcessor(GenericIngestionPipeline pipeline,
                                     ScheduleFileService scheduleService,
                                     ImportCompletedEventPublisher importCompletedEventPublisher,
                                     ProcessingRequestedEventPublisher processingRequestedEventPublisher) {
            super(pipeline, scheduleService, importCompletedEventPublisher, processingRequestedEventPublisher);
        }

        @Override
        protected boolean supports(MessageType type) {
            return type == MessageType.SSIM;
        }
    }
}
