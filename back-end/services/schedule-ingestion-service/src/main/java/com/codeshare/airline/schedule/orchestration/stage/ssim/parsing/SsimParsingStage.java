package com.codeshare.airline.schedule.orchestration.stage.ssim.parsing;

import com.codeshare.airline.schedule.domain.contex.SsimIngestionContext;

public interface SsimParsingStage {

    void execute(SsimIngestionContext context);
}
