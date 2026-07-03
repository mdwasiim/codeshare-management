package com.codeshare.airline.schedule.ingestion.validation.validator.file;

import com.codeshare.airline.core.enums.schedule.MessageType;
import com.codeshare.airline.schedule.ingestion.domain.enums.ValidationStage;
import com.codeshare.airline.schedule.ingestion.dto.schedule.ScheduleFileMetaDataDTO;
import com.codeshare.airline.schedule.ingestion.config.ScheduleIngestionProperties;
import com.codeshare.airline.schedule.ingestion.validation.model.ValidationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class FileExtensionValidation implements ScheduleFileExtensionValidation<ScheduleFileMetaDataDTO> {

    private final ScheduleIngestionProperties properties;

    @Override
    public Set<MessageType> supportedTypes() {
        return Set.of(MessageType.ASM, MessageType.SSM, MessageType.SSIM);
    }

    @Override
    public ValidationResult validate(ScheduleFileMetaDataDTO context) {

        ValidationResult result = new ValidationResult();

        if (context == null) {
            result.addError(
                    "VAL_FILE_META_001",
                    "Missing file metadata",
                    null,
                    "FILE",
                    ValidationStage.FILE_TYPE
            );
            return result;
        }

        String fileName = context.getFileName();
        MessageType type = context.getMessageType();

        if (type == null) {
            result.addError(
                    "VAL_FILE_TYPE_001",
                    "Missing message type",
                    null,
                    fileName,
                    ValidationStage.FILE_TYPE
            );
            return result;
        }

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
