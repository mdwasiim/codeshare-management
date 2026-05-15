package com.codeshare.airline.inbound.orchestration;

import com.codeshare.airline.core.enums.MessageType;
import com.codeshare.airline.inbound.domain.context.AsmIngestionContext;
import com.codeshare.airline.inbound.domain.context.ScheduleGroupedMessage;
import com.codeshare.airline.inbound.domain.context.SsmIngestionContext;
import com.codeshare.airline.inbound.domain.enums.AsmMessageType;
import com.codeshare.airline.inbound.domain.enums.ProcessingStatus;
import com.codeshare.airline.inbound.domain.enums.SourceType;
import com.codeshare.airline.inbound.dto.schedule.ScheduleFileMetaDataDTO;
import com.codeshare.airline.inbound.dto.schedule.ScheduleMessageDTO;
import com.codeshare.airline.inbound.orchestration.handler.parser.AsmContextBuilder;
import com.codeshare.airline.inbound.orchestration.parsers.ScheduleParser;
import com.codeshare.airline.inbound.orchestration.processor.ScheduleChapterProcessor;
import com.codeshare.airline.inbound.source.inbound.ExchangeConstants;
import com.codeshare.airline.inbound.source.inbound.ScheduleSourceFile;
import com.codeshare.airline.inbound.validations.model.ValidationResult;
import com.codeshare.airline.inbound.validations.validator.asm.structural.AsmStructuralValidator;
import com.codeshare.airline.inbound.validations.validator.ssm.structural.SsmStructuralValidator;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class IngestionProcessorRegressionTest {

    @Test
    void scheduleProcessorPublishesPartialStatusFromChapterProcessor() {
        ScheduleSourceFile sourceFile = ScheduleSourceFile.builder()
                .messageType(MessageType.SSM)
                .fileName("partial.ssm")
                .build();
        ScheduleChapterProcessor chapterProcessor = mock(ScheduleChapterProcessor.class);
        Exchange exchange = mock(Exchange.class);
        Message message = mock(Message.class);

        when(exchange.getMessage()).thenReturn(message);
        when(message.getBody(ScheduleSourceFile.class)).thenReturn(sourceFile);
        when(chapterProcessor.process(sourceFile)).thenReturn(ProcessingStatus.PARTIAL);

        new ScheduleIngestionProcessor(chapterProcessor).process(exchange);

        verify(exchange).setProperty(ExchangeConstants.PROCESS_STATUS, ProcessingStatus.PARTIAL);
    }

    @Test
    void asmStructuralValidationValidatesFinalBlockWhenSeparatorWasRemovedByExtractor() {
        List<String> lines = List.of(
                "ASM",
                "LT",
                "12JAN0000E1/REF 1/1",
                "NEW",
                "QR1234 DOHLHR 12JAN"
        );

        AsmIngestionContext context = AsmIngestionContext.builder()
                .messageType(MessageType.ASM)
                .messageLines(lines)
                .build();

        ValidationResult result = new AsmStructuralValidator().validate(context);

        assertThat(result.hasErrors()).isTrue();
        assertThat(result.getMessages())
                .anySatisfy(message -> assertThat(message.getRuleCode()).isEqualTo("ASM_061"))
                .anySatisfy(message -> assertThat(message.getRuleCode()).isEqualTo("ASM_070"));
    }

    @Test
    void ssmStructuralValidationValidatesFinalBlockWhenSeparatorWasRemovedByExtractor() {
        List<String> lines = List.of(
                "SSM",
                "LT",
                "12JAN0000E1/REF 1/1",
                "NEW",
                "QR1234"
        );

        SsmIngestionContext context = SsmIngestionContext.builder()
                .messageType(MessageType.SSM)
                .messageLines(lines)
                .build();

        ValidationResult result = new SsmStructuralValidator().validate(context);

        assertThat(result.hasErrors()).isTrue();
        assertThat(result.getMessages())
                .anySatisfy(message -> assertThat(message.getRuleCode()).isEqualTo("SSIM_060"))
                .anySatisfy(message -> assertThat(message.getRuleCode()).isEqualTo("SSIM_070"));
    }

    @Test
    void asmContextBuilderDetectsSubtypeAfterHeaderLines() {
        ScheduleParser<ScheduleMessageDTO> parser = new ScheduleParser<>() {
            @Override
            public ScheduleGroupedMessage groupMessage(List<String> lines) {
                return new ScheduleGroupedMessage(null, null, lines);
            }

            @Override
            public ScheduleMessageDTO parseMessage(ScheduleGroupedMessage grouped) {
                return new ScheduleMessageDTO();
            }
        };

        ScheduleFileMetaDataDTO metadata = ScheduleFileMetaDataDTO.builder()
                .messageType(MessageType.ASM)
                .sourceType(SourceType.LOCAL)
                .airlineCode("QR")
                .build();

        AsmIngestionContext context = new AsmContextBuilder(parser).parse(
                List.of("ASM", "LT", "12JAN0000E1/REF 1/1", "RPL", "QR1234 DOHLHR 12JAN"),
                metadata
        );

        assertThat(context.getSubMessageType()).isEqualTo(AsmMessageType.RPL);
    }
}
