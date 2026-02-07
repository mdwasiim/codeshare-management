package com.codeshare.airline.processor.domain.event;

import com.codeshare.airline.kafka.model.BaseEvent;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SsimFlightBlockReceivedEvent extends BaseEvent {

    /** File context */
    private String sourceFileName;
    private String season;
    private String airlineCode;

    /** Flight identity */
    private String flightNumber;
    private String departureAirport;
    private String arrivalAirport;

    /** Raw SSIM lines for this flight */
    private String rawSsimBlock;

    /** Structural validation already done */
    private boolean structurallyValid;

    /** Correlation */
    private String fileFingerprint;
    private String flightFingerprint;
}
