package com.codeshare.airline.schedule.ingestion.validation.validator.asm.business;

import com.codeshare.airline.core.enums.schedule.MessageType;
import com.codeshare.airline.schedule.ingestion.domain.context.AsmIngestionContext;
import com.codeshare.airline.schedule.ingestion.domain.enums.ValidationStage;
import com.codeshare.airline.schedule.ingestion.validation.model.ValidationResult;
import com.codeshare.airline.schedule.ingestion.validation.validator.BusinessValidation;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Order(4)
public class AsmFlightDateValidation implements BusinessValidation<AsmIngestionContext> {

    @Override
    public Set<MessageType> supportedTypes() {
        return Set.of(MessageType.ASM);
    }

    @Override
    public ValidationResult validate(AsmIngestionContext context) {
        ValidationResult result = new ValidationResult();

        if (context == null || context.getParsedData() == null) {
            return result;
        }

        context.getParsedData().getMessages().forEach(message ->
                message.getFlights().forEach(flight -> {
                    if (flight.getOperationDate() == null || flight.getOperationDate().isBlank()) {
                        result.addError("ASM_FLT_001",
                                "Missing flight identifier date",
                                "FLIGHT",
                                flight.getFlightNumber(),
                                ValidationStage.BUSINESS);
                    }
                }));

        return result;
    }
}
