package com.codeshare.airline.processor.pipeline.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SsimR4DateVariationRecordDTO {

    private Long id;
    private Long flightLegId;

    // SSIM Record-5 fields
    private String variationDate;              // Bytes 10–15
    private String actionCode;                 // Byte 16
    private String dayChangeIndicator;          // Byte 17
    private String scheduledDepartureTime;      // Bytes 18–21
    private String scheduledArrivalTime;        // Bytes 22–25
    private String aircraftType;                // Bytes 26–28
    private String trafficRestrictionCode;      // Bytes 29–30
    private String reserved;                    // Byte 31
    private String remarks;                     // Bytes 32–78

    // Record-6 continuations
    @Builder.Default
    private List<SsimR5ContinuationRecordDTO> continuations = new ArrayList<>();

    /**
     * Attach an SSIM Record-6 continuation to this Record-5.
     * SSIM guarantees positional ownership (R5 always follows R4).
     */
    public void addContinuation(SsimR5ContinuationRecordDTO continuation) {
        if (continuation != null) {
            continuations.add(continuation);
        }
    }
}

