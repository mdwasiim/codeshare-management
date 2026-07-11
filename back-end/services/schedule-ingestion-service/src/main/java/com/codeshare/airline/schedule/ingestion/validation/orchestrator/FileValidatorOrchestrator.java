package com.codeshare.airline.schedule.ingestion.validation.orchestrator;

import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import com.codeshare.airline.schedule.ingestion.dto.schedule.ScheduleFileMetaDataDTO;
import com.codeshare.airline.schedule.ingestion.validation.engine.ValidationEngine;
import com.codeshare.airline.schedule.ingestion.validation.model.ValidationResult;
import com.codeshare.airline.schedule.ingestion.validation.validator.file.ScheduleFileExtensionValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FileValidatorOrchestrator {

    private final List<ScheduleFileExtensionValidation<ScheduleFileMetaDataDTO>> validators;

    private final ValidationEngine validationEngine;

    public ValidationResult validate(MessageType type, ScheduleFileMetaDataDTO scheduleFileMetaDataDTO) {

        return switch (type) {

            case ASM -> validateAsm(scheduleFileMetaDataDTO);

            case SSM -> validateSsm(scheduleFileMetaDataDTO);

            case SSIM -> validateSsim(scheduleFileMetaDataDTO);
        };
    }

    /* ================= ASM ================= */

    private ValidationResult validateAsm(ScheduleFileMetaDataDTO scheduleFileMetaDataDTO) {

        return validateByType(MessageType.ASM, scheduleFileMetaDataDTO);
    }

    /* ================= SSM ================= */

    private ValidationResult validateSsm(ScheduleFileMetaDataDTO scheduleFileMetaDataDTO) {

        return validateByType(MessageType.SSM, scheduleFileMetaDataDTO);
    }

    /* ================= SSIM ================= */

    private ValidationResult validateSsim(ScheduleFileMetaDataDTO scheduleFileMetaDataDTO) {

        return validateByType(MessageType.SSIM, scheduleFileMetaDataDTO);
    }

    private ValidationResult validateByType(MessageType type, ScheduleFileMetaDataDTO scheduleFileMetaDataDTO) {
        List<ScheduleFileExtensionValidation<ScheduleFileMetaDataDTO>> matchingValidators = validators.stream()
                .filter(validator -> validator.supportedTypes().contains(type))
                .toList();

        return validationEngine.validate(
                matchingValidators,
                scheduleFileMetaDataDTO,
                ScheduleFileExtensionValidation::validate,
                true
        );
    }
}
