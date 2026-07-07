package com.codeshare.airline.schedule.ingestion.ssm;

import com.codeshare.airline.core.enums.schedule.MessageType;
import com.codeshare.airline.schedule.ingestion.domain.context.SsmIngestionContext;
import com.codeshare.airline.schedule.ingestion.domain.enums.ActionType;
import com.codeshare.airline.schedule.ingestion.dto.schedule.ScheduleMessageDTO;
import com.codeshare.airline.schedule.ingestion.orchestration.parsers.SsmMessageParser;
import com.codeshare.airline.schedule.ingestion.validation.model.ValidationResult;
import com.codeshare.airline.schedule.ingestion.validation.validator.ssm.structural.SsmStructuralValidator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SsmValidationRegressionTest {

    private final SsmStructuralValidator structuralValidator = new SsmStructuralValidator();
    private final SsmMessageParser parser = new SsmMessageParser();

    @Test
    void manual_new_requires_period_leg_and_equipment() {
        SsmIngestionContext context = context(
                "SSM",
                "NEW",
                "QR1234",
                "12MAY 30MAY 1234567"
        );

        ValidationResult result = structuralValidator.validate(context);

        assertThat(result.getMessages())
                .anySatisfy(message -> assertThat(message.getRuleCode()).isEqualTo("SSM_070"))
                .anySatisfy(message -> assertThat(message.getRuleCode()).isEqualTo("SSM_080"));
    }

    @Test
    void manual_cnl_does_not_require_leg_or_equipment() {
        SsmIngestionContext context = context(
                "SSM",
                "CNL",
                "QR1234",
                "12MAY 30MAY 1234567"
        );

        ValidationResult result = structuralValidator.validate(context);

        assertThat(result.getMessages())
                .noneSatisfy(message -> assertThat(message.getRuleCode()).isIn("SSM_070", "SSM_080"));
    }

    @Test
    void manual_adm_requires_dei_payload() {
        SsmIngestionContext context = context(
                "SSM",
                "ADM",
                "QR1234",
                "12MAY 30MAY 1234567"
        );

        ValidationResult result = structuralValidator.validate(context);

        assertThat(result.getMessages())
                .anySatisfy(message -> assertThat(message.getRuleCode()).isEqualTo("SSM_090"));
    }

    @Test
    void manual_rsd_may_not_be_mixed_with_other_actions() {
        SsmIngestionContext context = context(
                "SSM",
                "RSD",
                "QR1234",
                "//",
                "NEW",
                "QR1234",
                "12MAY 30MAY 1234567",
                "G M80 FCML .FCM",
                "DOH LHR 0800 1400"
        );

        ValidationResult result = structuralValidator.validate(context);

        assertThat(result.getMessages())
                .anySatisfy(message -> assertThat(message.getRuleCode()).isEqualTo("SSM_043"));
    }

    @Test
    void manual_skd_must_be_followed_only_by_new() {
        SsmIngestionContext context = context(
                "SSM",
                "SKD",
                "QR1234",
                "//",
                "CNL",
                "QR9999",
                "12MAY 30MAY 1234567"
        );

        ValidationResult result = structuralValidator.validate(context);

        assertThat(result.getMessages())
                .anySatisfy(message -> assertThat(message.getRuleCode()).isEqualTo("SSM_045"));
    }

    @Test
    void manual_skd_action_identifier_is_parsed() {
        ScheduleMessageDTO parsed = parser.parseMessage(parser.groupMessage(List.of(
                "SSM",
                "SKD",
                "QR1234"
        )));

        assertThat(parsed.getMessages().getFirst().getActionType()).isEqualTo(ActionType.SCHEDULE_CHANGE);
    }

    @Test
    void extra_final_block_without_separator_is_still_validated() {
        SsmIngestionContext context = context(
                "SSM",
                "NEW",
                "QR1234"
        );

        ValidationResult result = structuralValidator.validate(context);

        assertThat(result.getMessages())
                .anySatisfy(message -> assertThat(message.getRuleCode()).isEqualTo("SSM_060"))
                .anySatisfy(message -> assertThat(message.getRuleCode()).isEqualTo("SSM_070"));
    }

    @Test
    void extra_unknown_line_is_rejected() {
        SsmIngestionContext context = context(
                "SSM",
                "NEW",
                "###INVALID###"
        );

        ValidationResult result = structuralValidator.validate(context);

        assertThat(result.getMessages())
                .anySatisfy(message -> assertThat(message.getRuleCode()).isEqualTo("SSM_120"));
    }

    private SsmIngestionContext context(String... lines) {
        return SsmIngestionContext.builder()
                .messageType(MessageType.SSM)
                .messageLines(List.of(lines))
                .build();
    }
}
