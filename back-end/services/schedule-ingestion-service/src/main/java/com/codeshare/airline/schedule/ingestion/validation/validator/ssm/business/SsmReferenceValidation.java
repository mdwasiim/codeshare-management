package com.codeshare.airline.schedule.ingestion.validation.validator.ssm.business;

import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import com.codeshare.airline.schedule.ingestion.domain.context.SsmIngestionContext;
import com.codeshare.airline.schedule.ingestion.validation.model.ValidationResult;
import com.codeshare.airline.schedule.ingestion.validation.support.ScheduleMessageReferenceValidationSupport;
import com.codeshare.airline.schedule.ingestion.validation.validator.BusinessValidation;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Order(4)
public class SsmReferenceValidation implements BusinessValidation<SsmIngestionContext> {

    private final ScheduleMessageReferenceValidationSupport validationSupport;

    public SsmReferenceValidation(ScheduleMessageReferenceValidationSupport validationSupport) {
        this.validationSupport = validationSupport;
    }

    @Override
    public Set<MessageType> supportedTypes() {
        return Set.of(MessageType.SSM);
    }

    @Override
    public ValidationResult validate(SsmIngestionContext context) {
        if (context == null) {
            return new ValidationResult();
        }
        return validationSupport.validateScheduleMessage(context.getParsedData(), "SSM");
    }
}
