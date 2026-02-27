package com.codeshare.airline.schedule.orchestration.stage.asm.file.extension;

import com.codeshare.airline.schedule.source.ScheduleSourceFile;
import com.codeshare.airline.schedule.validation.core.exceptiom.InvalidFileExtensionException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AsmFileExtensionValidation {

    private final List<String> allowedExtensions;

    public AsmFileExtensionValidation(@Value("${ssim.allowed-extensions:ssim,txt}")  String extensions) {
        this.allowedExtensions = List.of(extensions.split(","));
    }

    public void validate(ScheduleSourceFile sourceFile) {

        String fileName = sourceFile.getFileName();

        if (fileName == null || !fileName.contains(".")) {
            throw new InvalidFileExtensionException( "Missing file extension");
        }

        String extension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();

        if (!allowedExtensions.contains(extension)) {
            throw new InvalidFileExtensionException("Unsupported file extension: " + extension);
        }
    }
}
