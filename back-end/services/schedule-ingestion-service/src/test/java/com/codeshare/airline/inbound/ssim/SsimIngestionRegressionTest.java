package com.codeshare.airline.inbound.ssim;

import com.codeshare.airline.core.enums.MessageType;
import com.codeshare.airline.inbound.config.ScheduleIngestionProperties;
import com.codeshare.airline.inbound.domain.context.ScheduleGroupedMessage;
import com.codeshare.airline.inbound.domain.context.SsimIngestionContext;
import com.codeshare.airline.inbound.domain.enums.SsimValidationMode;
import com.codeshare.airline.inbound.dto.ssim.SSIMMessageDTO;
import com.codeshare.airline.inbound.orchestration.parsers.SsimMessageParser;
import com.codeshare.airline.inbound.stream.extractor.SsimMessageExtractor;
import com.codeshare.airline.inbound.validations.model.ValidationResult;
import com.codeshare.airline.inbound.validations.validator.ssim.structural.SsimStructuralValidator;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SsimIngestionRegressionTest {

    @Test
    void extractsValidatesAndParsesSampleSsimAsCarrierSectionBatchWithFileMetadata() throws Exception {
        SsimMessageExtractor extractor = new SsimMessageExtractor(MessageType.SSIM);
        List<List<String>> blocks = new ArrayList<>();

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("sample_SSIM.ssim")) {
            assertThat(inputStream).isNotNull();
            extractor.extract(inputStream, blocks::add);
        }

        assertThat(blocks).hasSize(1);

        for (List<String> lines : blocks) {
            assertThat(lines).hasSize(15);
            assertThat(lines).allSatisfy(line -> assertThat(line).hasSize(200));
            assertThat(lines).filteredOn(line -> line.charAt(0) == '1').hasSize(1);
            assertThat(lines).filteredOn(line -> line.charAt(0) == '2').hasSize(1);
            assertThat(lines).filteredOn(line -> line.charAt(0) == '3').hasSize(2);
            assertThat(lines).filteredOn(line -> line.charAt(0) == '4').hasSize(10);
            assertThat(lines).filteredOn(line -> line.charAt(0) == '5').hasSize(1);

            SsimIngestionContext context = SsimIngestionContext.builder()
                    .messageType(MessageType.SSIM)
                    .messageLines(lines)
                    .build();

            ValidationResult result = new SsimStructuralValidator().validate(context);
            assertThat(result.hasErrors()).isFalse();

            SSIMMessageDTO dto = new SsimMessageParser().parseMessage(new ScheduleGroupedMessage(null, null, lines));

            assertThat(dto.getHeader()).isNotNull();
            assertThat(dto.getCarrier()).isNotNull();
            assertThat(dto.getTrailer()).isNotNull();
            assertThat(dto.getFlights()).hasSize(2);
            assertThat(dto.getFlights().getFirst().getDeis()).hasSize(5);
        }
    }

    @Test
    void extractsMultiCarrierSsimFileAsIndependentValidBatches() throws Exception {
        SsimMessageExtractor extractor = new SsimMessageExtractor(MessageType.SSIM);
        List<List<String>> blocks = new ArrayList<>();

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("multipleSSIM.txt")) {
            assertThat(inputStream).isNotNull();
            extractor.extract(inputStream, blocks::add);
        }

        assertThat(blocks).hasSize(3);

        for (List<String> lines : blocks) {
            assertThat(lines).allSatisfy(line -> assertThat(line).hasSize(200));
            assertThat(lines).filteredOn(line -> line.charAt(0) == '1').hasSize(1);
            assertThat(lines).filteredOn(line -> line.charAt(0) == '2').hasSize(1);
            assertThat(lines).filteredOn(line -> line.charAt(0) == '3').hasSizeGreaterThan(1);
            assertThat(lines).filteredOn(line -> line.charAt(0) == '5').hasSize(1);

            SsimIngestionContext context = SsimIngestionContext.builder()
                    .messageType(MessageType.SSIM)
                    .messageLines(lines)
                    .build();

            ValidationResult result = new SsimStructuralValidator().validate(context);
            assertThat(result.hasErrors()).isFalse();

            SSIMMessageDTO dto = new SsimMessageParser().parseMessage(new ScheduleGroupedMessage(null, null, lines));
            assertThat(dto.getHeader()).isNotNull();
            assertThat(dto.getCarrier()).isNotNull();
            assertThat(dto.getTrailer()).isNotNull();
            assertThat(dto.getFlights()).isNotEmpty();
        }
    }

    @Test
    void strictParserRejectsInvalidCarrierTimeMode() throws Exception {
        SsimMessageExtractor extractor = new SsimMessageExtractor(MessageType.SSIM);
        List<List<String>> blocks = new ArrayList<>();

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("sample_SSIM.ssim")) {
            assertThat(inputStream).isNotNull();
            extractor.extract(inputStream, blocks::add);
        }

        List<String> lines = new ArrayList<>(blocks.getFirst());
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.charAt(0) == '2') {
                lines.set(i, line.substring(0, 1) + "X" + line.substring(2));
                break;
            }
        }

        ScheduleIngestionProperties properties = new ScheduleIngestionProperties();
        properties.getSsim().setValidationMode(SsimValidationMode.STRICT);
        SsimMessageParser parser = new SsimMessageParser(properties);

        assertThatThrownBy(() -> parser.parseMessage(new ScheduleGroupedMessage(null, null, lines)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid SSIM Type 2 time mode");
    }
}
