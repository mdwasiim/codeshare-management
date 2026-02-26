package com.codeshare.airline.schedule.orchestration.stage.ssim.file.profile;

import com.codeshare.airline.schedule.domain.common.ScheduleProfile;

import java.io.InputStream;

public interface SsimProfileValidationStage {

    ScheduleProfile detect(InputStream is);
}
