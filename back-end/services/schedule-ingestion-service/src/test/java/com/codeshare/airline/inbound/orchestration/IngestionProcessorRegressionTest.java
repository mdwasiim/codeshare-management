package com.codeshare.airline.inbound.orchestration;

import com.codeshare.airline.core.enums.MessageType;
import com.codeshare.airline.inbound.domain.context.AsmIngestionContext;
import com.codeshare.airline.inbound.domain.context.ScheduleGroupedMessage;
import com.codeshare.airline.inbound.domain.context.SsimIngestionContext;
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

import java.util.Arrays;
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
                .anySatisfy(message -> assertThat(message.getRuleCode()).isEqualTo("SSM_060"))
                .anySatisfy(message -> assertThat(message.getRuleCode()).isEqualTo("SSM_070"));
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

    @Test
    void ssimStructuralValidationRequiresType2SerialAfterType1() {
        SsimIngestionContext context = SsimIngestionContext.builder()
                .messageType(MessageType.SSIM)
                .messageLines(List.of(
                        ssimHeader("000001"),
                        ssimCarrier("000003"),
                        ssimFlight("000004"),
                        ssimTrailer("000004", "000005")
                ))
                .build();

        ValidationResult result = new com.codeshare.airline.inbound.validations.validator.ssim.structural.SsimStructuralValidator()
                .validate(context);

        assertThat(result.getMessages())
                .anySatisfy(message -> assertThat(message.getRuleCode()).isEqualTo("SSIM_SERIAL_001"));
    }

    @Test
    void ssimStructuralValidationRequiresTrailerCheckReferenceToMatchPreviousRecord() {
        SsimIngestionContext context = SsimIngestionContext.builder()
                .messageType(MessageType.SSIM)
                .messageLines(List.of(
                        ssimHeader("000001"),
                        ssimCarrier("000002"),
                        ssimFlight("000003"),
                        ssimTrailer("000002", "000003")
                ))
                .build();

        ValidationResult result = new com.codeshare.airline.inbound.validations.validator.ssim.structural.SsimStructuralValidator()
                .validate(context);

        assertThat(result.getMessages())
                .anySatisfy(message -> assertThat(message.getRuleCode()).isEqualTo("SSIM_T5_009"));
    }

    private static String ssimHeader(String serial) {
        char[] record = blankRecord('1');
        put(record, 1, padRight("AIRLINE STANDARD SCHEDULE DATA SET", 34));
        put(record, 191, "001");
        put(record, 194, serial);
        return new String(record);
    }

    private static String ssimCarrier(String serial) {
        char[] record = blankRecord('2');
        put(record, 1, "L");
        put(record, 2, "QR ");
        put(record, 10, "S26");
        put(record, 14, "01APR26");
        put(record, 21, "30OCT26");
        put(record, 28, "01MAR26");
        put(record, 71, "P");
        put(record, 190, "1200");
        put(record, 194, serial);
        return new String(record);
    }

    private static String ssimFlight(String serial) {
        char[] record = blankRecord('3');
        put(record, 2, "QR ");
        put(record, 5, "0123");
        put(record, 9, "01");
        put(record, 11, "01");
        put(record, 13, "J");
        put(record, 14, "01APR26");
        put(record, 21, "30OCT26");
        put(record, 28, "1234567");
        put(record, 36, "DOH");
        put(record, 39, "0800");
        put(record, 43, "0800");
        put(record, 47, "+0300");
        put(record, 54, "LHR");
        put(record, 57, "1400");
        put(record, 61, "1400");
        put(record, 65, "+0000");
        put(record, 72, "320");
        put(record, 75, padRight("Y", 20));
        put(record, 194, serial);
        return new String(record);
    }

    private static String ssimTrailer(String checkReference, String serial) {
        char[] record = blankRecord('5');
        put(record, 2, "QR ");
        put(record, 187, checkReference);
        put(record, 193, "E");
        put(record, 194, serial);
        return new String(record);
    }

    private static char[] blankRecord(char recordType) {
        char[] record = new char[200];
        Arrays.fill(record, ' ');
        record[0] = recordType;
        return record;
    }

    private static void put(char[] record, int start, String value) {
        for (int i = 0; i < value.length(); i++) {
            record[start + i] = value.charAt(i);
        }
    }

    private static String padRight(String value, int length) {
        return String.format("%-" + length + "s", value);
    }
}
