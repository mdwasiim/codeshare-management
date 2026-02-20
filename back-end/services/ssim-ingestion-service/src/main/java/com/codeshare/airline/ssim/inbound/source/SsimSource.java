package com.codeshare.airline.ssim.inbound.source;

import com.codeshare.airline.ssim.inbound.domain.enums.SsimSourceType;

public interface SsimSource {

    /**
     * Source type identifier
     */
    SsimSourceType getType();

    /**
     * Discover available SSIM files.
     * Metadata-only. No file content is read here.
     */
    Iterable<SsimSourceFile> fetch();
}
