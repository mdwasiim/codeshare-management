package com.codeshare.airline.schedule.orchestration.stage.asm.file.profile;

import com.codeshare.airline.schedule.domain.common.ScheduleProfile;

import java.io.InputStream;

public interface AsmProfileValidationStage {

    ScheduleProfile detect(InputStream is);
}
