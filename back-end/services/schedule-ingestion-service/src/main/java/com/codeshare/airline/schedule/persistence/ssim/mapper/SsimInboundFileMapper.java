package com.codeshare.airline.schedule.persistence.ssim.mapper;

import com.codeshare.airline.schedule.persistence.ssim.entity.SsimInboundFile;
import com.codeshare.airline.schedule.domain.common.ProcessingStatus;
import com.codeshare.airline.schedule.domain.common.ScheduleProfile;
import com.codeshare.airline.schedule.source.ScheduleSourceFile;

import java.time.Instant;

public final class SsimInboundFileMapper {

    private SsimInboundFileMapper() {
        // utility class
    }

    public static SsimInboundFile fromRawFile(ScheduleSourceFile raw) {

        SsimInboundFile inbound = new SsimInboundFile();

        inbound.setSupersededFileId(raw.getFileId());
        inbound.setLoadId(raw.getLoadId());
        inbound.setFileName(raw.getFileName());

        inbound.setSourceType(raw.getSourceType());
        inbound.setSourceSystem(raw.getSourceSystem());
        inbound.setExternalReference(raw.getExternalReference());
        inbound.setScheduleProfile(ScheduleProfile.MIXED);
        inbound.setReceivedTimestamp(
                raw.getReceivedAt() != null
                        ? raw.getReceivedAt()
                        : Instant.now()
        );

        inbound.setProcessingStatus(ProcessingStatus.RECEIVED);

        // These are derived later
        inbound.setIsSuperseding(Boolean.FALSE);
        inbound.setTotalRecordCount(null);
        inbound.setChecksum(null);
        inbound.setCharacterSet("ASCII");

        return inbound;
    }
}
