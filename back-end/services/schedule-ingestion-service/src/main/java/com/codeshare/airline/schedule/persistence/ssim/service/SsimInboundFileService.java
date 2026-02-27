package com.codeshare.airline.schedule.persistence.ssim.service;

import com.codeshare.airline.schedule.domain.common.ProcessingStatus;
import com.codeshare.airline.schedule.domain.common.ScheduleProfile;
import com.codeshare.airline.schedule.persistence.ssim.entity.SsimInboundFile;
import com.codeshare.airline.schedule.source.ScheduleSourceFile;

public interface SsimInboundFileService {

    SsimInboundFile create(ScheduleSourceFile sourceFile);

    void updateStatus(SsimInboundFile file, ProcessingStatus status);

    void markFailed(SsimInboundFile file, Exception ex);

    void updateProfile(SsimInboundFile file, ScheduleProfile profile);
}
