package com.codeshare.airline.schedule.ingestion.asm;

import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import com.codeshare.airline.schedule.ingestion.domain.context.AsmIngestionContext;
import com.codeshare.airline.schedule.ingestion.domain.enums.ActionType;
import com.codeshare.airline.schedule.ingestion.dto.schedule.ScheduleMessageDTO;
import com.codeshare.airline.schedule.ingestion.orchestration.parsers.AsmMessageParser;
import com.codeshare.airline.schedule.ingestion.validation.model.ValidationResult;
import com.codeshare.airline.schedule.ingestion.validation.validator.asm.business.AsmChapter5BusinessValidator;
import com.codeshare.airline.schedule.ingestion.validation.validator.asm.structural.AsmStructuralValidator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AsmValidationRegressionTest {

    private final AsmStructuralValidator structuralValidator = new AsmStructuralValidator();
    private final AsmChapter5BusinessValidator businessValidator = new AsmChapter5BusinessValidator();
    private final AsmMessageParser parser = new AsmMessageParser();

    @Test
    void manual_new_requires_flight_leg_and_equipment() {
        AsmIngestionContext context = context(
                "ASM",
                "NEW OPER",
                "QR1234/12MAY",
                "G M80 FCML .FCM"
        );

        ValidationResult result = structuralValidator.validate(context);

        assertThat(result.getMessages())
                .anySatisfy(message -> assertThat(message.getRuleCode()).isEqualTo("ASM_070"));
    }

    @Test
    void manual_cnl_does_not_require_leg_or_equipment() {
        AsmIngestionContext context = context(
                "ASM",
                "CNL WEAT",
                "QR1234/12MAY"
        );

        ValidationResult result = structuralValidator.validate(context);

        assertThat(result.getMessages())
                .noneSatisfy(message -> assertThat(message.getRuleCode()).isIn("ASM_070", "ASM_080"));
    }

    @Test
    void manual_adm_requires_dei_payload() {
        AsmIngestionContext context = context(
                "ASM",
                "ADM OPER",
                "QR1234/12MAY"
        );

        ValidationResult result = structuralValidator.validate(context);

        assertThat(result.getMessages())
                .anySatisfy(message -> assertThat(message.getRuleCode()).isEqualTo("ASM_090"));
    }

    @Test
    void manual_secondary_action_combination_must_be_allowed() {
        AsmIngestionContext context = context(
                "ASM",
                "TIM/EQT WEAT",
                "QR1234/12MAY",
                "DOH LHR 0800 1400"
        );

        ValidationResult result = structuralValidator.validate(context);

        assertThat(result.getMessages())
                .anySatisfy(message -> assertThat(message.getRuleCode()).isEqualTo("ASM_042"));
    }

    @Test
    void manual_rpl_with_secondary_action_is_parsed_by_primary_action() {
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
    void extra_final_block_without_separator_is_still_validated() {
        AsmIngestionContext context = context(
                "ASM",
                "NEW OPER",
                "QR1234/12MAY"
        );

        ValidationResult result = structuralValidator.validate(context);

        assertThat(result.getMessages())
                .anySatisfy(message -> assertThat(message.getRuleCode()).isEqualTo("ASM_110"))
                .anySatisfy(message -> assertThat(message.getRuleCode()).isEqualTo("ASM_070"));
    }

    @Test
    void extra_unknown_line_is_rejected() {
        AsmIngestionContext context = context(
                "ASM",
                "NEW OPER",
                "###INVALID###"
        );

        ValidationResult result = structuralValidator.validate(context);

        assertThat(result.getMessages())
                .anySatisfy(message -> assertThat(message.getRuleCode()).isEqualTo("ASM_120"));
    }

    @Test
    void manual_nac_allows_reject_detail_lines() {
        AsmIngestionContext context = context(
                "ASM",
                "LT",
                "17NOV00026E001/LY0005/21NOV",
                "NAC",
                "003 ACTION IDENTIFIER INVALID",
                "ASM",
                "LT",
                "17NOV00026E001/LY0005/21NOV",
                "NEW",
                "//"
        );

        ValidationResult result = structuralValidator.validate(context);

        assertThat(result.getMessages())
                .noneSatisfy(message -> assertThat(message.getRuleCode()).isEqualTo("ASM_120"));
    }

    @Test
    void manual_tim_may_not_carry_equipment_change() {
        AsmIngestionContext context = parsedContext(
                "ASM",
                "TIM COMM",
                "QR1234/12MAY",
                "DOH LHR 0800 1400",
                "G 320 FCMY",
                "//"
        );

        ValidationResult result = businessValidator.validate(context);

        assertThat(result.getMessages())
                .anySatisfy(message -> assertThat(message.getRuleCode()).isEqualTo("ASM_BIZ_105"));
    }

    @Test
    void manual_nac_requires_reject_info_and_repeated_message() {
        AsmIngestionContext context = parsedContext(
                "ASM",
                "LT",
                "17NOV00026E001/LY0005/21NOV",
                "NAC",
                "003 ACTION IDENTIFIER INVALID",
                "ASM",
                "LT",
                "17NOV00026E001/LY0005/21NOV",
                "NEW",
                "//"
        );

        ValidationResult result = businessValidator.validate(context);

        assertThat(result.errorCount()).isZero();
    }

    @Test
    void manual_cnl_may_not_carry_time_or_equipment_lines() {
        AsmIngestionContext context = parsedContext(
                "ASM",
                "CNL WEAT",
                "QR1234/12MAY",
                "DOH LHR 0800 1400",
                "G 320 FCMY",
                "//"
        );

        ValidationResult result = businessValidator.validate(context);

        assertThat(result.getMessages())
                .anySatisfy(message -> assertThat(message.getRuleCode()).isEqualTo("ASM_BIZ_105"));
    }

    private AsmIngestionContext context(String... lines) {
        return AsmIngestionContext.builder()
                .messageType(MessageType.ASM)
                .messageLines(List.of(lines))
                .build();
    }

    private AsmIngestionContext parsedContext(String... lines) {
        ScheduleMessageDTO parsed = parser.parseMessage(parser.groupMessage(List.of(lines)));
        return AsmIngestionContext.builder()
                .messageType(MessageType.ASM)
                .messageLines(List.of(lines))
                .parsedData(parsed)
                .build();
    }
}
