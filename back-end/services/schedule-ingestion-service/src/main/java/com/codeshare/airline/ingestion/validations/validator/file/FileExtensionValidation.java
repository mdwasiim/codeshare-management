package com.codeshare.airline.ingestion.validations.validator.file;

import com.codeshare.airline.core.enums.MessageType;
import com.codeshare.airline.ingestion.domain.enums.ValidationStage;
import com.codeshare.airline.ingestion.persistence.dto.schedule.ScheduleFileMetaDataDTO;
import com.codeshare.airline.ingestion.config.ScheduleIngestionProperties;
import com.codeshare.airline.ingestion.validations.model.ValidationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FileExtensionValidation implements ScheduleFileExtensionValidation<ScheduleFileMetaDataDTO> {

    private final ScheduleIngestionProperties properties;

    @Override
    public ValidationResult validate(ScheduleFileMetaDataDTO context) {

        ValidationResult result = new ValidationResult();

        String fileName = context.getFileName();
        MessageType type = context.getMessageType();

        if (fileName == null || !fileName.contains(".")) {
            result.addError(
                    "VAL_FILE_EXT_001",
                    "Missing file extension",
                    type.name(),
                    type.name() + " FILE", ValidationStage.FILE_TYPE
            );
            return result;
        }

        String ext = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();

        var allowed = properties.getAllowedExtensions()
                .getOrDefault(type, List.of());

        if (!allowed.contains(ext)) {
            result.addError(
                    "VAL_FILE_EXT_002",
                    "Unsupported extension: " + ext,
                    type.name(),
                    type.name() + " FILE", ValidationStage.FILE_TYPE
            );
        }

        return result;
    }
}