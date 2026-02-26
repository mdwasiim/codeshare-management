package com.codeshare.airline.schedule.domain.contex;

import com.codeshare.airline.schedule.domain.common.ProcessingStatus;
import com.codeshare.airline.schedule.domain.common.ScheduleProfile;
import com.codeshare.airline.schedule.validation.ssim.model.ValidationResult;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class SsimIngestionContext extends AbstractIngestionContext {

    private final ScheduleProfile profile;

    public SsimIngestionContext withStatus(ProcessingStatus status) {
        return this.toBuilder()
                .currentStatus(status)
                .build();
    }

    public SsimIngestionContext withStructuralResult(ValidationResult result) {
        return this.toBuilder()
                .structuralResult(result)
                .build();
    }

    public SsimIngestionContext withBusinessResult(ValidationResult result) {
        return this.toBuilder()
                .businessResult(result)
                .build();
    }
}