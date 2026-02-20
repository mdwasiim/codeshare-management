package com.codeshare.airline.ssim.inbound.domain.contex;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
public class FlightBlockContext {

    private final String flightNumber;
    private final String carrier;

    private final List<String> lines = new ArrayList<>();

    public FlightBlockContext(String flightNumber, String carrier) {
        this.flightNumber = flightNumber;
        this.carrier = carrier;
    }

    public void addLine(String line) {
        lines.add(line);
    }
}
