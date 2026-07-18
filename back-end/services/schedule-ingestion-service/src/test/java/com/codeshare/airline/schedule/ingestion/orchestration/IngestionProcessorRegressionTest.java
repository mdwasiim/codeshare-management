package com.codeshare.airline.schedule.ingestion.orchestration;

import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import com.codeshare.airline.schedule.ingestion.domain.context.AsmIngestionContext;
import com.codeshare.airline.schedule.ingestion.domain.context.ScheduleGroupedMessage;
import com.codeshare.airline.schedule.ingestion.domain.context.SsimIngestionContext;
import com.codeshare.airline.schedule.ingestion.domain.context.SsmIngestionContext;
import com.codeshare.airline.schedule.ingestion.domain.enums.ActionType;
import com.codeshare.airline.schedule.ingestion.domain.enums.AsmMessageType;
import com.codeshare.airline.schedule.ingestion.domain.enums.ProcessingStatus;
import com.codeshare.airline.platform.core.enums.schedule.SourceType;
import com.codeshare.airline.schedule.ingestion.domain.enums.TimeMode;
import com.codeshare.airline.schedule.ingestion.dto.common.base.ScheduleEquipmentDTO;
import com.codeshare.airline.schedule.ingestion.dto.schedule.ScheduleFileMetaDataDTO;
import com.codeshare.airline.schedule.ingestion.dto.schedule.ScheduleMessageDTO;
import com.codeshare.airline.schedule.ingestion.orchestration.context.AsmContextBuilder;
import com.codeshare.airline.schedule.ingestion.orchestration.parsers.AsmMessageParser;
import com.codeshare.airline.schedule.ingestion.orchestration.parsers.SsmMessageParser;
import com.codeshare.airline.schedule.ingestion.orchestration.parsers.ScheduleParser;
import com.codeshare.airline.schedule.ingestion.orchestration.parsers.shared.EquipmentParser;
import com.codeshare.airline.schedule.ingestion.orchestration.parsers.shared.FlightIdentityParser;
import com.codeshare.airline.schedule.ingestion.orchestration.processor.ScheduleChapterProcessor;
import com.codeshare.airline.schedule.ingestion.source.model.ExchangeConstants;
import com.codeshare.airline.schedule.ingestion.source.model.ScheduleSourceFile;
import com.codeshare.airline.schedule.ingestion.validation.model.ValidationResult;
import com.codeshare.airline.schedule.ingestion.validation.validator.asm.business.AsmAirportValidation;
import com.codeshare.airline.schedule.ingestion.validation.validator.asm.structural.AsmStructuralValidator;
import com.codeshare.airline.schedule.ingestion.validation.validator.ssm.business.SsmAirportValidation;
import com.codeshare.airline.schedule.ingestion.validation.validator.ssm.structural.SsmStructuralValidator;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

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

        new ScheduleIngestionProcessor() {
            @Override
            protected boolean supports(MessageType type) {
                return type == MessageType.SSM;
            }

            @Override
            protected ProcessingStatus processSourceFile(ScheduleSourceFile sourceFile) {
                return chapterProcessor.process(sourceFile);
            }
        }.process(exchange);

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
                .anySatisfy(message -> assertThat(message.getRuleCode()).isEqualTo("ASM_070"));
    }

    @Test
    void asmStructuralValidationDoesNotRequireOptionalTimeModeReferenceOrPeriod() {
        List<String> lines = List.of(
                "ASM",
                "NEW",
                "QR1234/12MAY03",
                "G M80 FCML .FCM",
                "DOH LHR 0800 1400"
        );

        AsmIngestionContext context = AsmIngestionContext.builder()
                .messageType(MessageType.ASM)
                .messageLines(lines)
                .build();

        ValidationResult result = new AsmStructuralValidator().validate(context);

        assertThat(result.getMessages())
                .noneSatisfy(message -> assertThat(message.getRuleCode()).isIn("ASM_002", "ASM_003", "ASM_061"));
    }

    @Test
    void ssmStructuralValidationDoesNotRequireOptionalTimeModeOrReference() {
        List<String> lines = List.of(
                "SSM",
                "NEW",
                "QR1234",
                "12MAY 30MAY 1234567",
                "G M80 FCML .FCM",
                "DOH LHR 0800 1400"
        );

        SsmIngestionContext context = SsmIngestionContext.builder()
                .messageType(MessageType.SSM)
                .messageLines(lines)
                .build();

        ValidationResult result = new SsmStructuralValidator().validate(context);

        assertThat(result.getMessages())
                .noneSatisfy(message -> assertThat(message.getRuleCode()).isIn("SSM_002", "SSM_003"));
    }

    @Test
    void equipmentParserFollowsIataServiceTypeThenAircraftTypeOrder() {
        ScheduleEquipmentDTO equipment = EquipmentParser.parse("G M80 FCML .FCM");

        assertThat(equipment.getServiceType()).isEqualTo("G");
        assertThat(equipment.getAircraftType()).isEqualTo("M80");
        assertThat(equipment.getBookingDesignator()).isEqualTo("FCML");
        assertThat(equipment.getAircraftConfiguration()).isEqualTo("FCM");
    }

    @Test
    void asmFlightIdentityParserSupportsSlashDateIdentifier() {
        var identity = FlightIdentityParser.parseAsm("LX544A/12MAY03");

        assertThat(identity.getAirlineDesignator()).isEqualTo("LX");
        assertThat(identity.getFlightNumber()).isEqualTo("544");
        assertThat(identity.getOperationalSuffix()).isEqualTo("A");
        assertThat(identity.getOperationDate()).isEqualTo("12MAY03");
    }

    @Test
    void ssmParserDefaultsMissingTimeModeToUtcAndPersistsEquipmentFields() {
        SsmMessageParser parser = new SsmMessageParser();
        ScheduleMessageDTO message = parser.parseMessage(parser.groupMessage(List.of(
                "SSM",
                "NEW",
                "QR1234",
                "12MAY 30MAY 1234567",
                "G M80 FCML .FCM",
                "DOH LHR 0800 1400"
        )));

        var subMessage = message.getMessages().getFirst();
        var flight = subMessage.getFlights().getFirst();

        assertThat(subMessage.getTimeMode()).isEqualTo(TimeMode.UTC);
        assertThat(flight.getServiceType()).isEqualTo("G");
        assertThat(flight.getAircraftType()).isEqualTo("M80");
        assertThat(flight.getAircraftConfiguration()).isEqualTo("FCM");
        assertThat(flight.getBookingDesignator()).isEqualTo("FCML");
    }

    @Test
    void ssmQrResourceMessagesAreStructurallyBusinessValidAndParseable() throws IOException, URISyntaxException {
        List<List<String>> blocks = resourceMessageBlocks("100_SSM_QR_Test_Messages.txt");
        SsmStructuralValidator structuralValidator = new SsmStructuralValidator();
        SsmAirportValidation airportValidation = new SsmAirportValidation();
        SsmMessageParser parser = new SsmMessageParser();

        assertThat(blocks).hasSize(100);

        for (int i = 0; i < blocks.size(); i++) {
            List<String> block = blocks.get(i);
            SsmIngestionContext structuralContext = SsmIngestionContext.builder()
                    .messageType(MessageType.SSM)
                    .messageLines(block)
                    .build();

            assertValidationHasNoErrors("SSM structural block " + (i + 1),
                    structuralValidator.validate(structuralContext));

            ScheduleMessageDTO parsed = parser.parseMessage(parser.groupMessage(block));
            SsmIngestionContext businessContext = SsmIngestionContext.builder()
                    .messageType(MessageType.SSM)
                    .messageLines(block)
                    .parsedData(parsed)
                    .build();

            assertValidationHasNoErrors("SSM business block " + (i + 1),
                    airportValidation.validate(businessContext));
        }
    }

    @Test
    void asmQrResourceMessagesAreStructurallyBusinessValidAndParseable() throws IOException, URISyntaxException {
        List<List<String>> blocks = resourceMessageBlocks("100_ASM_QR_Test_Messages.txt");
        AsmStructuralValidator structuralValidator = new AsmStructuralValidator();
        AsmAirportValidation airportValidation = new AsmAirportValidation();
        AsmMessageParser parser = new AsmMessageParser();

        assertThat(blocks).hasSize(100);

        for (int i = 0; i < blocks.size(); i++) {
            List<String> block = blocks.get(i);
            AsmIngestionContext structuralContext = AsmIngestionContext.builder()
                    .messageType(MessageType.ASM)
                    .messageLines(block)
                    .build();

            assertValidationHasNoErrors("ASM structural block " + (i + 1),
                    structuralValidator.validate(structuralContext));

            ScheduleMessageDTO parsed = parser.parseMessage(parser.groupMessage(block));
            AsmIngestionContext businessContext = AsmIngestionContext.builder()
                    .messageType(MessageType.ASM)
                    .messageLines(block)
                    .parsedData(parsed)
                    .build();

            assertValidationHasNoErrors("ASM business block " + (i + 1),
                    airportValidation.validate(businessContext));
        }
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
    void asmStructuralValidationAllowsCancellationWithoutLegOrEquipment() {
        AsmIngestionContext context = AsmIngestionContext.builder()
                .messageType(MessageType.ASM)
                .messageLines(List.of(
                        "ASM",
                        "CNL WEAT",
                        "QR1234/12MAY"
                ))
                .build();

        ValidationResult result = new AsmStructuralValidator().validate(context);

        assertThat(result.getMessages())
                .noneSatisfy(message -> assertThat(message.getRuleCode()).isIn("ASM_070", "ASM_080"));
    }

    @Test
    void asmStructuralValidationRejectsUnsupportedSecondaryActionCombination() {
        AsmIngestionContext context = AsmIngestionContext.builder()
                .messageType(MessageType.ASM)
                .messageLines(List.of(
                        "ASM",
                        "TIM/EQT WEAT",
                        "QR1234/12MAY",
                        "DOH LHR 0800 1400"
                ))
                .build();

        ValidationResult result = new AsmStructuralValidator().validate(context);

        assertThat(result.getMessages())
                .anySatisfy(message -> assertThat(message.getRuleCode()).isEqualTo("ASM_042"));
    }

    @Test
    void asmParserRecognizesPrimaryActionWhenSecondaryActionsArePresent() {
        AsmMessageParser parser = new AsmMessageParser();

        ScheduleMessageDTO parsed = parser.parseMessage(parser.groupMessage(List.of(
                "ASM",
                "RPL/EQT WEAT",
                "QR1234/12MAY",
                "G M80 FCML .FCM",
                "DOH LHR 0800 1400"
        )));

        assertThat(parsed.getMessages().getFirst().getActionType()).isEqualTo(ActionType.REPLACE);
    }

    @Test
    void ssmStructuralValidationAllowsCancellationWithoutLegOrEquipment() {
        SsmIngestionContext context = SsmIngestionContext.builder()
                .messageType(MessageType.SSM)
                .messageLines(List.of(
                        "SSM",
                        "CNL",
                        "QR1234",
                        "12MAY 30MAY 1234567"
                ))
                .build();

        ValidationResult result = new SsmStructuralValidator().validate(context);

        assertThat(result.getMessages())
                .noneSatisfy(message -> assertThat(message.getRuleCode()).isIn("SSM_070", "SSM_080"));
    }

    @Test
    void ssmStructuralValidationRejectsRsdMixedWithOtherActions() {
        SsmIngestionContext context = SsmIngestionContext.builder()
                .messageType(MessageType.SSM)
                .messageLines(List.of(
                        "SSM",
                        "RSD",
                        "QR1234",
                        "//",
                        "NEW",
                        "QR1234",
                        "12MAY 30MAY 1234567",
                        "G M80 FCML .FCM",
                        "DOH LHR 0800 1400"
                ))
                .build();

        ValidationResult result = new SsmStructuralValidator().validate(context);

        assertThat(result.getMessages())
                .anySatisfy(message -> assertThat(message.getRuleCode()).isEqualTo("SSM_043"));
    }

    @Test
    void ssmStructuralValidationRequiresSkdToBeFollowedOnlyByNewForSameFlight() {
        SsmIngestionContext context = SsmIngestionContext.builder()
                .messageType(MessageType.SSM)
                .messageLines(List.of(
                        "SSM",
                        "SKD",
                        "QR1234",
                        "//",
                        "CNL",
                        "QR9999",
                        "12MAY 30MAY 1234567"
                ))
                .build();

        ValidationResult result = new SsmStructuralValidator().validate(context);

        assertThat(result.getMessages())
                .anySatisfy(message -> assertThat(message.getRuleCode()).isEqualTo("SSM_045"));
    }

    @Test
    void ssmParserRecognizesSkdActionIdentifier() {
        SsmMessageParser parser = new SsmMessageParser();

        ScheduleMessageDTO parsed = parser.parseMessage(parser.groupMessage(List.of(
                "SSM",
                "SKD",
                "QR1234"
        )));

        assertThat(parsed.getMessages().getFirst().getActionType()).isEqualTo(ActionType.SCHEDULE_CHANGE);
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

        ValidationResult result = new com.codeshare.airline.schedule.ingestion.validation.validator.ssim.structural.SsimStructuralValidator()
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

        ValidationResult result = new com.codeshare.airline.schedule.ingestion.validation.validator.ssim.structural.SsimStructuralValidator()
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

    private static List<List<String>> resourceMessageBlocks(String fileName) throws IOException, URISyntaxException {
        var resource = IngestionProcessorRegressionTest.class.getClassLoader().getResource(fileName);
        assertThat(resource).as(fileName + " classpath resource").isNotNull();
        Path path = Path.of(resource.toURI());
        List<List<String>> blocks = new ArrayList<>();
        List<String> current = new ArrayList<>();

        for (String line : Files.readAllLines(path)) {
            if (line.isBlank()) {
                continue;
            }
            if ("//".equals(line.trim())) {
                if (!current.isEmpty()) {
                    current.add("//");
                    blocks.add(List.copyOf(current));
                    current.clear();
                }
                continue;
            }
            current.add(line);
        }

        if (!current.isEmpty()) {
            blocks.add(List.copyOf(current));
        }

        return blocks;
    }

    private static void assertValidationHasNoErrors(String label, ValidationResult result) {
        assertThat(result.getMessages())
                .as(label)
                .filteredOn(message -> message.getSeverity().name().equals("ERROR"))
                .map(message -> message.getRuleCode() + ":" + message.getRecordKey() + ":" + message.getMessage())
                .isEmpty();
    }
}
