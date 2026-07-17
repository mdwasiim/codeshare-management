package com.codeshare.airline.schedule.ingestion.validation.validator.asm.business;

import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import com.codeshare.airline.schedule.ingestion.domain.context.AsmIngestionContext;
import com.codeshare.airline.schedule.ingestion.validation.model.ValidationResult;
import com.codeshare.airline.schedule.ingestion.validation.support.ScheduleMessageReferenceValidationSupport;
import com.codeshare.airline.schedule.ingestion.validation.validator.BusinessValidation;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Order(4)
public class AsmReferenceValidation implements BusinessValidation<AsmIngestionContext> {

    private final ScheduleMessageReferenceValidationSupport validationSupport;

    public AsmReferenceValidation(ScheduleMessageReferenceValidationSupport validationSupport) {
        this.validationSupport = validationSupport;
    }

    @Override
    public Set<MessageType> supportedTypes() {
        return Set.of(MessageType.ASM);
    }

    @Override
    public ValidationResult validate(AsmIngestionContext context) {
        if (context == null) {
            return new ValidationResult();
        }
        return validationSupport.validateScheduleMessage(context.getParsedData(), "ASM");
    }
}
