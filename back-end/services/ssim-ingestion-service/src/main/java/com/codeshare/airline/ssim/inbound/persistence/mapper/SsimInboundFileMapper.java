package com.codeshare.airline.ssim.inbound.persistence.mapper;

import com.codeshare.airline.ssim.inbound.persistence.inbound.entity.SsimInboundFile;
import com.codeshare.airline.ssim.inbound.domain.enums.SsimProcessingStatus;
import com.codeshare.airline.ssim.inbound.domain.enums.SsimProfile;
import com.codeshare.airline.ssim.inbound.source.SsimSourceFile;

import java.time.Instant;

public final class SsimInboundFileMapper {

    private SsimInboundFileMapper() {
        // utility class
    }

    public static SsimInboundFile fromRawFile(SsimSourceFile raw) {

        SsimInboundFile inbound = new SsimInboundFile();

        inbound.setSupersededFileId(raw.getFileId());
        inbound.setLoadId(raw.getLoadId());
        inbound.setFileName(raw.getFileName());

        inbound.setSourceType(raw.getSourceType());
        inbound.setSourceSystem(raw.getSourceSystem());
        inbound.setExternalReference(raw.getExternalReference());
        inbound.setSsimProfile(SsimProfile.MIXED);
        inbound.setReceivedTimestamp(
                raw.getReceivedAt() != null
                        ? raw.getReceivedAt()
                        : Instant.now()
        );

        inbound.setProcessingStatus(SsimProcessingStatus.RECEIVED);

        // These are derived later
        inbound.setIsSuperseding(Boolean.FALSE);
        inbound.setTotalRecordCount(null);
        inbound.setChecksum(null);
        inbound.setCharacterSet("ASCII");

        return inbound;
    }
}
