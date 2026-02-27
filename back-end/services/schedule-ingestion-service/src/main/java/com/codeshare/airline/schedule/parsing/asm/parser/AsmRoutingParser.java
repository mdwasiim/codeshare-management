package com.codeshare.airline.schedule.parsing.asm.parser;

import com.codeshare.airline.schedule.parsing.asm.dto.AsmInboundLeg;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class AsmRoutingParser {

    public List<AsmInboundLeg> parse(String routingLine) {

        String[] parts = routingLine.split("\\s+");

        String routing = parts[0];
        String times = parts[1];

        List<AsmInboundLeg> legs = new ArrayList<>();

        int sectors = routing.length() / 3 - 1;

        for (int i = 0; i < sectors; i++) {

            String origin = routing.substring(i * 3, i * 3 + 3);
            String destination = routing.substring(i * 3 + 3, i * 3 + 6);

            String dep = times.substring(i * 4, i * 4 + 4);
            String arr = times.substring(i * 4 + 4, i * 4 + 8);

            AsmInboundLeg leg = new AsmInboundLeg();
            leg.setOrigin(origin);
            leg.setDestination(destination);
            leg.setDepartureTime(parseTime(dep));
            leg.setArrivalTime(parseTime(arr));

            legs.add(leg);
        }

        return legs;
    }

    private LocalTime parseTime(String token) {
        return LocalTime.of(
                Integer.parseInt(token.substring(0,2)),
                Integer.parseInt(token.substring(2))
        );
    }
}