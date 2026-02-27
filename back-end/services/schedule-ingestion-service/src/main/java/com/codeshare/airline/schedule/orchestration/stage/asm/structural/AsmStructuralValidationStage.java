package com.codeshare.airline.schedule.orchestration.stage.asm.structural;

import com.codeshare.airline.schedule.domain.contex.AsmIngestionContext;

public interface AsmStructuralValidationStage {

    void execute(AsmIngestionContext context);
}
