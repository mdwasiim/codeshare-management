package com.codeshare.airline.schedule.parsing.ssm.parser;

import com.codeshare.airline.schedule.parsing.ssm.dto.SsmInboundLeg;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class SsmRoutingParser {

    public List<SsmInboundLeg> parse(String routingLine) {

        String[] parts = routingLine.split("\\s+");

        String routing = parts[0];
        String times = parts[1];

        List<SsmInboundLeg> legs = new ArrayList<>();

        int sectorCount = routing.length() / 3 - 1;

        for (int i = 0; i < sectorCount; i++) {

            String origin = routing.substring(i * 3, i * 3 + 3);
            String destination = routing.substring(i * 3 + 3, i * 3 + 6);

            String dep = times.substring(i * 4, i * 4 + 4);
            String arr = times.substring(i * 4 + 4, i * 4 + 8);

            SsmInboundLeg leg = new SsmInboundLeg();
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