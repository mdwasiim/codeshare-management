package com.codeshare.airline.ssim.inbound.orchestration.stage.parsing;

import com.codeshare.airline.ssim.inbound.domain.contex.SsimIngestionContext;

public interface SsimParsingStage {

    void execute(SsimIngestionContext context);
}
