package com.codeshare.airline.schedule.persistence.ssim.service;

import com.codeshare.airline.schedule.domain.common.ProcessingStatus;
import com.codeshare.airline.schedule.domain.common.ScheduleProfile;
import com.codeshare.airline.schedule.parsing.ssim.dto.SsimInboundFileDTO;
import com.codeshare.airline.schedule.persistence.ssim.entity.SsimInboundCarrier;
import com.codeshare.airline.schedule.persistence.ssim.entity.SsimInboundHeader;
import com.codeshare.airline.schedule.persistence.ssim.entity.SsimInboundTrailer;
import com.codeshare.airline.schedule.source.ScheduleSourceFile;

public interface SsimInboundFileService {

    SsimInboundFileDTO create(ScheduleSourceFile sourceFile);

    void updateStatus(String fileId, ProcessingStatus status);

    void markFailed(String fileId, Exception ex);

    void saveHeader(SsimInboundHeader header);

    void saveCarrier(SsimInboundCarrier carrier);

    void saveTrailer(SsimInboundTrailer trailer);

    void updateProfile(String fileId, ScheduleProfile profile);
}
