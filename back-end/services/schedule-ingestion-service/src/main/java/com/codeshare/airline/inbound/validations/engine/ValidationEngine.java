package com.codeshare.airline.inbound.validations.engine;

import com.codeshare.airline.inbound.domain.enums.ValidationStage;
import com.codeshare.airline.inbound.validations.model.ValidationResult;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ValidationEngine {

    public <T, V> ValidationResult validate( List<V> validators,T target, ValidatorExecutor<T, V> executor, boolean failFast) {

        ValidationResult finalResult = new ValidationResult();

        if (validators == null || validators.isEmpty()) {
            return finalResult;
        }

        for (V validator : validators) {

            if (validator == null) continue;

            try {
                ValidationResult result = executor.execute(validator, target);

                if (result != null && !result.getMessages().isEmpty()) {

                    finalResult.merge(result);

                    if (failFast && result.hasErrors()) break;
                }

            } catch (Exception ex) {

                ValidationResult error = new ValidationResult();

                error.addError(
                        "VALIDATION_EXCEPTION",
                        ex.getMessage(),
                        null,
                        validator.getClass().getSimpleName(),
                        ValidationStage.EXCEPTION
                );

                finalResult.merge(error);

                if (failFast) break;
            }
        }

        return finalResult;
    }
}
