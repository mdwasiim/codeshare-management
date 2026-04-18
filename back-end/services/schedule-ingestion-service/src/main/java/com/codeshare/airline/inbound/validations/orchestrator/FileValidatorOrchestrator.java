package com.codeshare.airline.inbound.validations.orchestrator;

import com.codeshare.airline.enums.MessageType;
import com.codeshare.airline.inbound.dto.schedule.ScheduleFileMetaDataDTO;
import com.codeshare.airline.inbound.validations.engine.ValidationEngine;
import com.codeshare.airline.inbound.validations.model.ValidationResult;
import com.codeshare.airline.inbound.validations.validator.file.ScheduleFileExtensionValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FileValidatorOrchestrator {

    private final List<ScheduleFileExtensionValidation<ScheduleFileMetaDataDTO>> asmValidators;
    private final List<ScheduleFileExtensionValidation<ScheduleFileMetaDataDTO>> ssmValidators;
    private final List<ScheduleFileExtensionValidation<ScheduleFileMetaDataDTO>> ssimValidators;

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

        return validationEngine.validate(
                asmValidators,
                scheduleFileMetaDataDTO,
                ScheduleFileExtensionValidation::validate,
                true   // fail fast
        );
    }

    /* ================= SSM ================= */

    private ValidationResult validateSsm(ScheduleFileMetaDataDTO scheduleFileMetaDataDTO) {

        return validationEngine.validate(
                ssmValidators,
                scheduleFileMetaDataDTO,
                ScheduleFileExtensionValidation::validate,
                true
        );
    }

    /* ================= SSIM ================= */

    private ValidationResult validateSsim(ScheduleFileMetaDataDTO scheduleFileMetaDataDTO) {

        return validationEngine.validate(
                ssimValidators,
                scheduleFileMetaDataDTO,
                ScheduleFileExtensionValidation::validate,
                true
        );
    }
}