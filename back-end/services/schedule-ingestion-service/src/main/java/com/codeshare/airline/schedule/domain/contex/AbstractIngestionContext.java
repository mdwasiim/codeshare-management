package com.codeshare.airline.schedule.domain.contex;

import com.codeshare.airline.schedule.domain.common.ProcessingStatus;
import com.codeshare.airline.schedule.source.ScheduleSourceFile;
import com.codeshare.airline.schedule.validation.ssim.model.ValidationResult;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
public abstract class AbstractIngestionContext {

    protected final ScheduleSourceFile sourceFile;

    protected final String inboundFileId;  

    protected final ValidationResult structuralResult;

    protected final ValidationResult businessResult;

    protected final ProcessingStatus currentStatus;
}