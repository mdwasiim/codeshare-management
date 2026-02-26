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
public class AsmIngestionContext extends AbstractIngestionContext {

    private final ScheduleProfile profile;

    public AsmIngestionContext withStatus(ProcessingStatus status) {
        return this.toBuilder()
                .currentStatus(status)
                .build();
    }

    public AsmIngestionContext withStructuralResult(ValidationResult result) {
        return this.toBuilder()
                .structuralResult(result)
                .build();
    }

    public AsmIngestionContext withBusinessResult(ValidationResult result) {
        return this.toBuilder()
                .businessResult(result)
                .build();
    }
}
