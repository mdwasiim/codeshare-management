package com.codeshare.airline.schedule.orchestration.stage.ssim.file;

import com.codeshare.airline.schedule.domain.contex.SsimIngestionContext;

public interface SsimFileValidationStage {

    void execute(SsimIngestionContext context);
}
