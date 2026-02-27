package com.codeshare.airline.schedule.orchestration.stage.asm.structural;

import com.codeshare.airline.schedule.domain.contex.AsmIngestionContext;
import com.codeshare.airline.schedule.parsing.asm.dto.AsmInboundMessage;
import com.codeshare.airline.schedule.validation.model.ValidationResult;
import org.springframework.stereotype.Component;

@Component
public class AsmStructuralValidation implements AsmStructuralValidationStage {

    public void execute(AsmIngestionContext context) {

        ValidationResult result = new ValidationResult();

        for (AsmInboundMessage msg : context.getParsedResult()) {
            validateMessage(msg, result);
        }

        context.setStructuralResult(result);
    }

    private void validateMessage(AsmInboundMessage msg,
                                 ValidationResult result) {

        if (msg.getActionIdentifier() == null) {
            result.addError("Missing ActionIdentifier");
            return;
        }

        switch (msg.getActionIdentifier()) {

            case NEW, RPL, TIM -> {
                require(msg.getPeriodFrom(), "PeriodFrom required", result);
                require(msg.getPeriodTo(), "PeriodTo required", result);
                require(msg.getDaysOfOperation(), "DaysOfOperation required", result);
                requireRouting(msg, result);
            }

            case CNL -> {
                require(msg.getPeriodFrom(), "PeriodFrom required for CNL", result);
                require(msg.getPeriodTo(), "PeriodTo required for CNL", result);
            }

            default -> {
                // ADM / EQT minimal requirements
                requireRouting(msg, result);
            }
        }

        validateDaysFormat(msg.getDaysOfOperation(), result);
        validateRoutingConsistency(msg, result);
    }

    private void require(Object value,
                         String message,
                         ValidationResult result) {

        if (value == null || (value instanceof String s && s.isBlank())) {
            result.addError(message);
        }
    }

    private void requireRouting(AsmInboundMessage msg,
                                ValidationResult result) {

        if (msg.getLegs() == null || msg.getLegs().isEmpty()) {
            result.addError("Routing required");
        }
    }

    private void validateDaysFormat(String days,
                                    ValidationResult result) {

        if (days == null) return;

        if (!days.matches("^[1-7]{1,7}$")) {
            result.addError("Invalid days-of-operation format: " + days);
        }
    }

    private void validateRoutingConsistency(AsmInboundMessage msg,
                                            ValidationResult result) {

        if (msg.getLegs() == null) return;

        msg.getLegs().forEach(leg -> {
            if (leg.getDepartureTime() == null ||
                    leg.getArrivalTime() == null) {

                result.addError("Missing departure/arrival time in leg "
                        + leg.getOrigin() + "-" + leg.getDestination());
            }
        });
    }

}