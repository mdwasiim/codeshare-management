package com.codeshare.airline.inbound.api.response;

import com.codeshare.airline.inbound.dto.ssim.SsimMetaDataDTO;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SsimLoadedScheduleSummaryResponse {

    private SsimMetaDataDTO file;
    private long flightCount;
}
