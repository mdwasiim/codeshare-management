package com.codeshare.airline.ssim.inbound.orchestration.stage.file.extension;

import com.codeshare.airline.ssim.inbound.source.SsimSourceFile;
import com.codeshare.airline.ssim.inbound.validation.exception.InvalidFileExtensionException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultSsimFileExtensionStage {

    private final List<String> allowedExtensions;

    public DefaultSsimFileExtensionStage(@Value("${ssim.allowed-extensions:ssim,txt}")  String extensions) {
        this.allowedExtensions = List.of(extensions.split(","));
    }

    public void validate(SsimSourceFile sourceFile) {

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
