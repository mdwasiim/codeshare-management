package com.codeshare.airline.schedule.ingestion.validation.service;

import com.codeshare.airline.schedule.ingestion.dto.schedule.ScheduleDataElementDTO;
import com.codeshare.airline.schedule.ingestion.dto.schedule.ScheduleMessageDTO;
import com.codeshare.airline.schedule.ingestion.validation.model.ValidationResult;

public interface DeiRule {

    int getCode();

    void validate(ScheduleDataElementDTO dei, ScheduleMessageDTO block, ValidationResult result);
}