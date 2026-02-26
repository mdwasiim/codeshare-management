package com.codeshare.airline.schedule.orchestration.stage.ssim.business;

import com.codeshare.airline.schedule.domain.contex.SsimIngestionContext;

public interface SsimBusinessValidationStage {

    void execute(SsimIngestionContext context);
}
