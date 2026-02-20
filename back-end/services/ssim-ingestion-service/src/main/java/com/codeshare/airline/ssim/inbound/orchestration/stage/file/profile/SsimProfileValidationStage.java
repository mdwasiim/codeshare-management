package com.codeshare.airline.ssim.inbound.orchestration.stage.file.profile;

import com.codeshare.airline.ssim.inbound.domain.enums.SsimProfile;

import java.io.InputStream;

public interface SsimProfileValidationStage {

    SsimProfile detect(InputStream is);
}
