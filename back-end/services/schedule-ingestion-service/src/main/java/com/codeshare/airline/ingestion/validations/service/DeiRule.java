package com.codeshare.airline.ingestion.validations.service;

import com.codeshare.airline.ingestion.persistence.dto.schedule.ScheduleDataElementDTO;
import com.codeshare.airline.ingestion.persistence.dto.schedule.ScheduleMessageDTO;
import com.codeshare.airline.ingestion.validations.model.ValidationResult;

public interface DeiRule {

    int getCode();

    void validate(ScheduleDataElementDTO dei, ScheduleMessageDTO block, ValidationResult result);
}