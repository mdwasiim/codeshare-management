package com.codeshare.airline.ssim.inbound.validation.business.rule;

import com.codeshare.airline.ssim.inbound.validation.business.BusinessValidationContext;
import com.codeshare.airline.ssim.inbound.validation.business.BusinessValidationRule;
import org.springframework.stereotype.Component;

@Component
public class AirlineCodeBlankWarnRule implements BusinessValidationRule {

    @Override
    public void validate(BusinessValidationContext context) {

       /* if (line.charAt(0) == '3') {

            String airline = line.substring(2, 5).trim();

            if (airline.isEmpty()) {

                ctx.addMessage(
                        ValidationMessage.of(
                                "AIRLINE_CODE_BLANK",
                                "Blank airline code in T3 record at line "
                                        + ctx.getLineNumber(),
                                ValidationSeverity.WARNING,
                                false   // not blocking
                        )
                );
            }
        }*/
    }

}
