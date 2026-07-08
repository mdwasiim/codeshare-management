package com.codeshare.airline.schedule.ingestion.validation.validator.ssm.business;

import com.codeshare.airline.core.enums.schedule.MessageType;
import com.codeshare.airline.schedule.ingestion.domain.context.SsmIngestionContext;
import com.codeshare.airline.schedule.ingestion.domain.enums.ValidationStage;
import com.codeshare.airline.schedule.ingestion.dto.schedule.SchedulePeriodDTO;
import com.codeshare.airline.schedule.ingestion.validation.model.ValidationResult;
import com.codeshare.airline.schedule.ingestion.validation.validator.BusinessValidation;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Order(3)
public class SsmPeriodValidation implements BusinessValidation<SsmIngestionContext> {

    @Override
    public Set<MessageType> supportedTypes() {
        return Set.of(MessageType.SSM);
    }

    @Override
    public ValidationResult validate(SsmIngestionContext context) {
        ValidationResult result = new ValidationResult();

        if (context == null || context.getParsedData() == null) {
            return result;
        }

        context.getParsedData().getMessages().forEach(message ->
                message.getFlights().forEach(flight ->
                        flight.getPeriods().forEach(period -> validatePeriod(result, flight.getFlightNumber(), period))));

        return result;
    }

    private void validatePeriod(ValidationResult result, String flightNumber, SchedulePeriodDTO period) {
        if (!period.isValidPeriod()) {
            result.addError("SSM_PER_001", "Invalid period date range", "PERIOD", flightNumber, ValidationStage.BUSINESS);
        }
        if (period.getDaysOfOperation() != null && !period.isValidDays()) {
            result.addError("SSM_PER_002", "Invalid days of operation", "PERIOD", flightNumber, ValidationStage.BUSINESS);
        }
        if (!period.isValidFrequency()) {
            result.addError("SSM_PER_003", "Invalid frequency rate", "PERIOD", flightNumber, ValidationStage.BUSINESS);
        }
    }
}
