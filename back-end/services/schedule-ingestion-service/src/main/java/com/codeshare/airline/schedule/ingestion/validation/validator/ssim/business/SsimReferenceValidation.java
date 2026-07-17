package com.codeshare.airline.schedule.ingestion.validation.validator.ssim.business;

import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import com.codeshare.airline.schedule.ingestion.domain.context.SsimIngestionContext;
import com.codeshare.airline.schedule.ingestion.dto.ssim.SSIMMessageDTO;
import com.codeshare.airline.schedule.ingestion.dto.ssim.record.SsimDataElementDTO;
import com.codeshare.airline.schedule.ingestion.dto.ssim.record.SsimFlightDTO;
import com.codeshare.airline.schedule.ingestion.validation.model.ValidationResult;
import com.codeshare.airline.schedule.ingestion.validation.support.MasterDataReferenceValidationSupport;
import com.codeshare.airline.schedule.ingestion.validation.validator.BusinessValidation;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Order(5)
public class SsimReferenceValidation implements BusinessValidation<SsimIngestionContext> {

    private final MasterDataReferenceValidationSupport validationSupport;

    public SsimReferenceValidation(MasterDataReferenceValidationSupport validationSupport) {
        this.validationSupport = validationSupport;
    }

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
            validationSupport.validateAirline(result, flight.getAirlineCode(), "SSIM_REF_001", "TYPE_3", recordKey);
            validationSupport.validateAirport(result, flight.getDepartureStation(), "SSIM_REF_010", "TYPE_3", recordKey);
            validationSupport.validateAirport(result, flight.getArrivalStation(), "SSIM_REF_011", "TYPE_3", recordKey);
            validationSupport.validateAircraft(result, flight.getAircraftType(), "SSIM_REF_020", "TYPE_3", recordKey);
            validationSupport.validateServiceType(result, flight.getServiceType(), "SSIM_REF_021", "TYPE_3", recordKey);
            validationSupport.validateTrafficRestriction(result, flight.getTrafficRestrictionCode(), "SSIM_REF_022", "TYPE_3", recordKey);

            if (flight.getDeis() == null) {
                continue;
            }

            for (SsimDataElementDTO dei : flight.getDeis()) {
                if (dei == null) {
                    continue;
                }

                String deiKey = recordKey + "/DEI-" + normalize(dei.getDataElementIdentifier());
                validationSupport.validateAirport(result, dei.getBoardPoint(), "SSIM_REF_030", "TYPE_4", deiKey);
                validationSupport.validateAirport(result, dei.getOffPoint(), "SSIM_REF_031", "TYPE_4", deiKey);
            }
        }

        return result;
    }

    private String flightKey(SsimFlightDTO flight) {
        return normalize(flight.getAirlineCode())
                + normalize(flight.getFlightNumber())
                + "/" + normalize(flight.getItineraryVariationIdentifier())
                + "/" + flight.getLegSequenceNumber();
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }
}
