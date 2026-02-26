package com.codeshare.airline.schedule.orchestration.stage.ssim.structural;

import com.codeshare.airline.schedule.domain.contex.SsimIngestionContext;

public interface SsimStructuralValidationStage {

    void execute(SsimIngestionContext context);
}
