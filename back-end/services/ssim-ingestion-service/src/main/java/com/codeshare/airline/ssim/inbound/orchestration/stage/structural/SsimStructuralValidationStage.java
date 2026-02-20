package com.codeshare.airline.ssim.inbound.orchestration.stage.structural;

import com.codeshare.airline.ssim.inbound.domain.contex.SsimIngestionContext;

public interface SsimStructuralValidationStage {

    void execute(SsimIngestionContext context);
}
