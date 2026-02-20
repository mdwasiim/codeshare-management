package com.codeshare.airline.ssim.inbound.persistence.control.repository;

import com.codeshare.airline.persistence.persistence.repository.CSMDataBaseRepository;
import com.codeshare.airline.ssim.inbound.persistence.control.entity.ValidationReport;

import java.util.UUID;

public interface ValidationReportRepository
        extends CSMDataBaseRepository<ValidationReport, UUID> {
}
