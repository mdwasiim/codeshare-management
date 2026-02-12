package com.codeshare.airline.ssim.ingestion.mapper;

import com.codeshare.airline.ssim.persistence.inbound.entity.SsimInboundFile;
import com.codeshare.airline.ssim.source.SsimProcessingStatus;
import com.codeshare.airline.ssim.source.SsimProfile;
import com.codeshare.airline.ssim.source.SsimSourceFile;

import java.time.Instant;

public final class SsimInboundFileMapper {

    private SsimInboundFileMapper() {
        // utility class
    }

    public static SsimInboundFile fromRawFile(SsimSourceFile raw) {

        SsimInboundFile inbound = new SsimInboundFile();

        inbound.setFileId(raw.getFileId());
        inbound.setLoadId(raw.getLoadId());
        inbound.setFileName(raw.getFileName());

        inbound.setSourceType(raw.getSourceType());
        inbound.setSourceSystem(raw.getSourceSystem());
        inbound.setExternalReference(raw.getExternalReference());
        inbound.setSsimProfile(SsimProfile.MIXED);
        inbound.setReceivedAt(
                raw.getReceivedAt() != null
                        ? raw.getReceivedAt()
                        : Instant.now()
        );

        inbound.setProcessingStatus(SsimProcessingStatus.RECEIVED);

        // These are derived later
        inbound.setSuperseding(Boolean.FALSE);
        inbound.setTotalRecords(null);
        inbound.setChecksum(null);
        inbound.setCharacterSet("ASCII");

        return inbound;
    }
}
