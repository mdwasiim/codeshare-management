package com.codeshare.airline.ingestion.persistence.services.ssim;

import com.codeshare.airline.ingestion.persistence.dto.ssim.SSIMMessageDTO;
import com.codeshare.airline.ingestion.persistence.dto.ssim.SsimMetaDataDTO;

public interface SsimPersistenceService {
    void saveBatch(SSIMMessageDTO ssimMessageDTO, SsimMetaDataDTO ssimInboundFileDTO);
}
