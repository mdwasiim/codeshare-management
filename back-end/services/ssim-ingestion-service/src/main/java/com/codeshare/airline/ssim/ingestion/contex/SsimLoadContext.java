package com.codeshare.airline.ssim.ingestion.contex;

import com.codeshare.airline.ssim.persistence.inbound.entity.SsimInboundFile;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * File-scoped context for SSIM ingestion.
 *
 * Lives ONLY for the duration of ingesting one SSIM file.
 * Does NOT hold SSIM records or entities.
 */
@Getter
@Setter
@ToString
public class SsimLoadContext {

    /**
     * Inbound file DB record
     */
    private SsimInboundFile inboundFile;

    /**
     * Structural state
     */
    private boolean t1Seen;
    private boolean t2Seen;
    private boolean t5Seen;
    private boolean hasInvalidOrder;

    /**
     * Resume / progress tracking
     */
    private String lastProcessedRecordSerial;
    private String lastFlushedFlightKey;

    /**
     * Optional counters (metrics only)
     */
    private long totalRecordsRead;
    private long totalFlightsPersisted;
    private long totalDeisPersisted;

    /**
     * Failure tracking
     */
    private boolean failed;
    private String failureReason;
}
