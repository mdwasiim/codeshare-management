package com.codeshare.airline.schedule.ingestion.api.response;

import com.codeshare.airline.schedule.ingestion.dto.ssim.SSIMMessageDTO;
import com.codeshare.airline.schedule.ingestion.dto.ssim.SsimMetaDataDTO;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SsimLoadedScheduleDetailResponse {

    private SsimMetaDataDTO file;
    private SSIMMessageDTO schedule;
    private long flightCount;
}
