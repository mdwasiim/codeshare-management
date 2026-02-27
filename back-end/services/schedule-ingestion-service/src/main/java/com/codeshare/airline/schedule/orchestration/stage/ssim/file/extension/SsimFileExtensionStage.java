package com.codeshare.airline.schedule.orchestration.stage.ssim.file.extension;

import com.codeshare.airline.schedule.persistence.ssim.entity.SsimInboundFile;
import com.codeshare.airline.schedule.source.ScheduleSourceFile;
import com.codeshare.airline.schedule.validation.model.ValidationMessage;

import java.util.List;

public interface SsimFileExtensionStage {

    void validate(ScheduleSourceFile sourceFile);
    void saveAll(SsimInboundFile ssimInboundFile, List<ValidationMessage> messages);

}
