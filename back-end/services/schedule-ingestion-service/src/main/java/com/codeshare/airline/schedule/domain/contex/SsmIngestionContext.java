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
public class SsmIngestionContext extends AbstractIngestionContext {
    private final ScheduleProfile profile;

    public SsmIngestionContext withStatus(ProcessingStatus status) {
        return this.toBuilder()
                .currentStatus(status)
                .build();
    }

    public SsmIngestionContext withStructuralResult(ValidationResult result) {
        return this.toBuilder()
                .structuralResult(result)
                .build();
    }

    public SsmIngestionContext withBusinessResult(ValidationResult result) {
        return this.toBuilder()
                .businessResult(result)
                .build();
    }
}
