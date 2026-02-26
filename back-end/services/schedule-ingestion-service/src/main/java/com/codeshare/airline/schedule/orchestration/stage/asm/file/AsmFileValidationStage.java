package com.codeshare.airline.schedule.orchestration.stage.asm.file;

import com.codeshare.airline.schedule.domain.contex.AsmIngestionContext;

public interface AsmFileValidationStage {

    void execute(AsmIngestionContext context);
}
