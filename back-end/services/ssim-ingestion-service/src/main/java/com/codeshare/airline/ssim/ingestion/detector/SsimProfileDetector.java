package com.codeshare.airline.ssim.ingestion.detector;

import com.codeshare.airline.ssim.source.SsimProfile;

import java.io.InputStream;

public interface SsimProfileDetector {

    SsimProfile detect(InputStream is);
}
