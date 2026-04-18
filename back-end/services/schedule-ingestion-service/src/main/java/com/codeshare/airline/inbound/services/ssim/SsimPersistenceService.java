package com.codeshare.airline.inbound.services.ssim;

import com.codeshare.airline.inbound.dto.ssim.SSIMMessageDTO;
import com.codeshare.airline.inbound.dto.ssim.SsimMetaDataDTO;

public interface SsimPersistenceService {
    void saveBatch(SSIMMessageDTO ssimMessageDTO, SsimMetaDataDTO ssimInboundFileDTO);
}
