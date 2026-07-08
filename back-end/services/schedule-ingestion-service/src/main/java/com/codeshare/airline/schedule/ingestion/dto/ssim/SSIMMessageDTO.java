package com.codeshare.airline.schedule.ingestion.dto.ssim;

import com.codeshare.airline.schedule.ingestion.dto.ssim.record.SsimCarrierDTO;
import com.codeshare.airline.schedule.ingestion.dto.ssim.record.SsimFlightDTO;
import com.codeshare.airline.schedule.ingestion.dto.ssim.record.SsimHeaderDTO;
import com.codeshare.airline.schedule.ingestion.dto.ssim.record.SsimTrailerDTO;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SSIMMessageDTO {

    private String airlineCode;
    private String flightNumber;

    private SsimHeaderDTO header;
    private SsimCarrierDTO carrier;

    @Builder.Default
    private List<SsimFlightDTO> flights = new ArrayList<>();

    private SsimTrailerDTO trailer;

    /* =========================
       HELPERS
       ========================= */

    public void addFlight(SsimFlightDTO flight) {
        if (flight != null) {
            flights.add(flight);
        }
    }

    public boolean hasFlights() {
        return flights != null && !flights.isEmpty();
    }
}
