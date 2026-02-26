package com.codeshare.airline.schedule.validation.ssim.business.rule;


import com.codeshare.airline.schedule.validation.ssim.business.BusinessValidationContext;
import com.codeshare.airline.schedule.validation.ssim.business.BusinessValidationRule;
import com.codeshare.airline.schedule.validation.ssim.model.ValidationMessage;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class AirlineExistsRule implements BusinessValidationRule {

    private static final Set<String> VALID_AIRLINES =
            Set.of("QR", "EK", "BA", "LH", "AS");

    @Override
    public void validate(BusinessValidationContext context) {

        String airlineCode =
                context.getInboundFile().getAirlineCode();
        if(airlineCode==null && context.getSsimInboundCarrier()!=null){

            airlineCode =  context.getSsimInboundCarrier().getAirlineDesignator();
        }

        if (!VALID_AIRLINES.contains(airlineCode)) {

            context.addMessage(
                    ValidationMessage.error(
                            "AIRLINE_NOT_FOUND",
                            "Airline code not found: " + airlineCode
                    )
            );
        }
    }
}
