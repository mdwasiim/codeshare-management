package com.codeshare.airline.ssim.inbound.orchestration.stage.file;

import com.codeshare.airline.ssim.inbound.domain.contex.SsimIngestionContext;

public interface SsimFileValidationStage {

    void execute(SsimIngestionContext context);
}
