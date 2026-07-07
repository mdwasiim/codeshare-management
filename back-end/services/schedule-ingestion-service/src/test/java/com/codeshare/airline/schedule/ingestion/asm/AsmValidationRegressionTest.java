package com.codeshare.airline.schedule.ingestion.asm;

import com.codeshare.airline.core.enums.schedule.MessageType;
import com.codeshare.airline.schedule.ingestion.domain.context.AsmIngestionContext;
import com.codeshare.airline.schedule.ingestion.domain.enums.ActionType;
import com.codeshare.airline.schedule.ingestion.dto.schedule.ScheduleMessageDTO;
import com.codeshare.airline.schedule.ingestion.orchestration.parsers.AsmMessageParser;
import com.codeshare.airline.schedule.ingestion.validation.model.ValidationResult;
import com.codeshare.airline.schedule.ingestion.validation.validator.asm.structural.AsmStructuralValidator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AsmValidationRegressionTest {

    private final AsmStructuralValidator structuralValidator = new AsmStructuralValidator();
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

    private AsmIngestionContext context(String... lines) {
        return AsmIngestionContext.builder()
                .messageType(MessageType.ASM)
                .messageLines(List.of(lines))
                .build();
    }
}
