package com.codeshare.airline.ssim.inbound.orchestration.stage.business;

import com.codeshare.airline.ssim.inbound.domain.contex.SsimIngestionContext;

public interface SsimBusinessValidationStage {

    void execute(SsimIngestionContext context);
}
