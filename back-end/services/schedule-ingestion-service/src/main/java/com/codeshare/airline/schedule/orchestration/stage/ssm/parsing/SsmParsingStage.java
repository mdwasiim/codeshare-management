package com.codeshare.airline.schedule.orchestration.stage.ssm.parsing;

import com.codeshare.airline.schedule.domain.contex.SsmIngestionContext;
import com.codeshare.airline.schedule.parsing.ssm.dto.SsmInboundMessage;
import org.springframework.stereotype.Component;

@Component
public class SsmParsingStage {

    public void execute(SsmIngestionContext context) {

        String raw = context.getRawContent();

        SsmInboundMessage message = new SsmInboundMessage();

        // Step 1: Extract action identifier
        message.setActionIdentifier(extractAction(raw));

        // Step 2: Extract flight designator
        message.setCarrier(extractCarrier(raw));
        message.setFlightNumber(extractFlight(raw));

        // Step 3: Extract operation date
        message.setOperationDate(extractOperationDate(raw));

        // Step 4: Extract legs
        message.setLegs(extractLegs(raw));

        // Step 5: Extract DEI
        message.setDeis(extractDei(raw));

        context.setParsedMessage(message);
    }
}