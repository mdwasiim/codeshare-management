package com.codeshare.airline.schedule.ingestion.persistence.services.ssim;

import com.codeshare.airline.schedule.ingestion.dto.ssim.SSIMMessageDTO;
import com.codeshare.airline.schedule.ingestion.dto.ssim.SsimMetaDataDTO;

public interface SsimPersistenceService {
    void saveBatch(SSIMMessageDTO message, SsimMetaDataDTO metadata);
}
