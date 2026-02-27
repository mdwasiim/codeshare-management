package com.codeshare.airline.schedule.validation.structural.ssim;

import com.codeshare.airline.schedule.orchestration.stage.ssim.structural.context.SsimStructuralValidationContext;
import com.codeshare.airline.schedule.validation.model.ValidationResult;
import com.codeshare.airline.schedule.validation.structural.ssim.rule.StructuralValidationRule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StructuralValidationEngine {

    private final List<StructuralValidationRule> rules;

    public ValidationResult validate(InputStream is) {

        SsimStructuralValidationContext context = new SsimStructuralValidationContext();

        try (BufferedReader reader =  new BufferedReader(new InputStreamReader(is))) {

            String line;

            while ((line = reader.readLine()) != null) {
                context.nextLine();
                for (StructuralValidationRule rule : rules) {
                    rule.validate(line, context);
                }

            }

            // Post-file validations
            for (StructuralValidationRule rule : rules) {
                rule.afterFileComplete(context);
            }

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        return new ValidationResult(context.getMessages());
    }
}
