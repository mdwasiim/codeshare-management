package com.codeshare.airline.ssim.inbound.validation.structural;

import com.codeshare.airline.ssim.inbound.domain.contex.StructuralValidationContext;
import com.codeshare.airline.ssim.inbound.validation.model.ValidationResult;
import com.codeshare.airline.ssim.inbound.validation.structural.rule.StructuralValidationRule;
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

        StructuralValidationContext context = new StructuralValidationContext();

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
