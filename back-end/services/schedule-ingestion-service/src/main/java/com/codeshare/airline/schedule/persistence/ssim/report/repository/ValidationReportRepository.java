package com.codeshare.airline.schedule.persistence.ssim.report.repository;

import com.codeshare.airline.persistence.persistence.repository.CSMDataBaseRepository;
import com.codeshare.airline.schedule.persistence.ssim.report.entity.ValidationReport;

import java.util.UUID;

public interface ValidationReportRepository
        extends CSMDataBaseRepository<ValidationReport, UUID> {
}
