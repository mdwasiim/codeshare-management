package com.codeshare.airline.inbound.api.response;

import com.codeshare.airline.inbound.dto.ssim.SSIMMessageDTO;
import com.codeshare.airline.inbound.dto.ssim.SsimMetaDataDTO;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SsimLoadedScheduleDetailResponse {

    private SsimMetaDataDTO file;
    private SSIMMessageDTO schedule;
    private long flightCount;
}
