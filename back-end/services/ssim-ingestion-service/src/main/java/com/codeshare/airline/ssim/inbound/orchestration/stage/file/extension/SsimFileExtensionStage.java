package com.codeshare.airline.ssim.inbound.orchestration.stage.file.extension;

import com.codeshare.airline.ssim.inbound.persistence.control.entity.ValidationReport;
import com.codeshare.airline.ssim.inbound.persistence.control.repository.ValidationReportRepository;
import com.codeshare.airline.ssim.inbound.validation.model.ValidationMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SsimFileExtensionStage {

    private final ValidationReportRepository repository;

    @Transactional
    public void saveAll(String fileId,
                        List<ValidationMessage> messages) {

        List<ValidationReport> reports = messages.stream()
                .map(msg -> map(fileId, msg))
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
