package com.codeshare.airline.schedule.orchestration.stage.ssm.business;

import com.codeshare.airline.schedule.domain.contex.SsmIngestionContext;
import com.codeshare.airline.schedule.parsing.ssm.dto.SsmInboundMessage;
import com.codeshare.airline.schedule.validation.model.ValidationResult;
import org.springframework.stereotype.Component;

@Component
public class SsmBusinessValidationStage {

    public void execute(SsmIngestionContext context) {

        SsmInboundMessage msg = context.getParsedMessage();

        ValidationResult result = new ValidationResult();

        switch (msg.getActionIdentifier()) {

            case "CNL":
                validateCancel(msg, result);
                break;

            case "RIN":
                validateReinstate(msg, result);
                break;

            case "TIM":
                validateTimeChange(msg, result);
                break;

            case "EQT":
                validateEquipmentChange(msg, result);
                break;

            case "RRT":
                validateRoutingChange(msg, result);
                break;

            default:
                result.addBlocking("Unsupported ActionIdentifier");
        }

        context.setBusinessResult(result);
    }
}