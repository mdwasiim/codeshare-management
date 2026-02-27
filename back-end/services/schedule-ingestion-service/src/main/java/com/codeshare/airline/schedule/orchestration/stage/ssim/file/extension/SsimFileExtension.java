package com.codeshare.airline.schedule.orchestration.stage.ssim.file.extension;

import com.codeshare.airline.schedule.persistence.ssim.entity.SsimInboundFile;
import com.codeshare.airline.schedule.persistence.ssim.report.entity.ValidationReport;
import com.codeshare.airline.schedule.persistence.ssim.report.repository.ValidationReportRepository;
import com.codeshare.airline.schedule.source.ScheduleSourceFile;
import com.codeshare.airline.schedule.validation.core.exceptiom.InvalidFileExtensionException;
import com.codeshare.airline.schedule.validation.model.ValidationMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SsimFileExtension implements SsimFileExtensionStage {

    private  ValidationReportRepository repository;

    @Value("${ssim.allowed-extensions:ssim,txt}")
    private  List<String> allowedExtensions;

    public SsimFileExtension(List<String> allowedExtensions) {
        this.allowedExtensions = allowedExtensions;
    }

    @Override
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
    
    @Transactional
    public void saveAll(SsimInboundFile ssimInboundFile,  List<ValidationMessage> messages) {

        List<ValidationReport> reports = messages.stream()
                .map(msg -> map(ssimInboundFile.getId().toString(), msg))
                .toList();

        repository.saveAll(reports);
    }

    private ValidationReport map(String fileId,
                                 ValidationMessage msg) {

        ValidationReport report = new ValidationReport();
        report.setFileId(fileId);
        report.setLoadId(UUID.fromString(fileId));
        report.setRecordType(msg.getRecordType());
        report.setRecordKey(msg.getRecordKey());
        report.setSeverity(msg.getSeverity());
        report.setRuleCode(msg.getCode());
        report.setMessage(msg.getMessage());
        report.setBlocking(msg.isBlocking());
        report.setCreatedAt(LocalDateTime.now());

        return report;
    }
}
