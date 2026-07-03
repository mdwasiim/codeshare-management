package com.codeshare.airline.schedule.ingestion.api.response;

import com.codeshare.airline.schedule.ingestion.dto.ssim.SsimMetaDataDTO;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SsimLoadedScheduleSummaryResponse {

    private SsimMetaDataDTO file;
    private long flightCount;
}
