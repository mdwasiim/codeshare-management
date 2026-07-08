package com.codeshare.airline.schedule.ingestion.validation.validator.ssim.business;

import com.codeshare.airline.core.enums.schedule.MessageType;
import com.codeshare.airline.schedule.ingestion.domain.context.SsimIngestionContext;
import com.codeshare.airline.schedule.ingestion.domain.enums.ValidationStage;
import com.codeshare.airline.schedule.ingestion.dto.ssim.record.SsimDataElementDTO;
import com.codeshare.airline.schedule.ingestion.dto.ssim.record.SsimFlightDTO;
import com.codeshare.airline.schedule.ingestion.dto.ssim.SSIMMessageDTO;
import com.codeshare.airline.schedule.ingestion.validation.model.ValidationResult;
import com.codeshare.airline.schedule.ingestion.validation.validator.BusinessValidation;
import lombok.AllArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.regex.Pattern;

@Component
@Order(4)
@AllArgsConstructor
public class SsimAirportValidation implements BusinessValidation<SsimIngestionContext> {

    private static final Pattern IATA_AIRPORT = Pattern.compile("[A-Z]{3}");

    @Override
    public Set<MessageType> supportedTypes() {
        return Set.of(MessageType.SSIM);
    }

    @Override
    public ValidationResult validate(SsimIngestionContext context) {

        ValidationResult result = new ValidationResult();

        if (context == null || context.getParsedData() == null) {
            return result;
        }

        SSIMMessageDTO message = context.getParsedData();
        if (message.getFlights() == null) {
            return result;
        }

        for (SsimFlightDTO flight : message.getFlights()) {

            if (flight == null) {
                continue;
            }

            String recordKey = flightKey(flight);
            String departure = normalize(flight.getDepartureStation());
            String arrival = normalize(flight.getArrivalStation());

            if (!isAirport(departure)) {
                result.addError("SSIM_APT_001", "Invalid SSIM departure airport", "TYPE_3", recordKey, ValidationStage.BUSINESS);
            }

            if (!isAirport(arrival)) {
                result.addError("SSIM_APT_002", "Invalid SSIM arrival airport", "TYPE_3", recordKey, ValidationStage.BUSINESS);
            }

            if (isAirport(departure) && departure.equals(arrival)) {
                result.addError("SSIM_APT_003", "SSIM departure and arrival airports must be different", "TYPE_3", recordKey, ValidationStage.BUSINESS);
            }

            if (flight.getDeis() == null) {
                continue;
            }

            for (SsimDataElementDTO dei : flight.getDeis()) {
                if (dei == null) {
                    continue;
                }

                String deiKey = recordKey + "/" + normalize(dei.getDataElementIdentifier());
                String boardPoint = normalize(dei.getBoardPoint());
                String offPoint = normalize(dei.getOffPoint());

                if (!isAirport(boardPoint)) {
                    result.addError("SSIM_DEI_APT_001", "Invalid SSIM DEI board point", "TYPE_4", deiKey, ValidationStage.BUSINESS);
                }

                if (!isAirport(offPoint)) {
                    result.addError("SSIM_DEI_APT_002", "Invalid SSIM DEI off point", "TYPE_4", deiKey, ValidationStage.BUSINESS);
                }

                if (isAirport(boardPoint) && boardPoint.equals(offPoint)) {
                    result.addError("SSIM_DEI_APT_003", "SSIM DEI board and off points must be different", "TYPE_4", deiKey, ValidationStage.BUSINESS);
                }
            }
        }

        return result;
    }

    private boolean isAirport(String value) {
        return value != null && IATA_AIRPORT.matcher(value).matches();
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }

    private String flightKey(SsimFlightDTO flight) {
        return normalize(flight.getAirlineCode())
                + normalize(flight.getFlightNumber())
                + "/" + normalize(flight.getItineraryVariationIdentifier())
                + "/" + flight.getLegSequenceNumber();
    }
}

