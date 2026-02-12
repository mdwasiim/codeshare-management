package com.codeshare.airline.ssim.ingestion.parser;

import com.codeshare.airline.ssim.persistence.inbound.entity.SsimInboundFile;
import com.codeshare.airline.ssim.persistence.inbound.entity.SsimInboundFlightLeg;
import org.springframework.stereotype.Component;

@Component
public class T3FlightLegParser {

    public SsimInboundFlightLeg parse(
            String line,
            SsimInboundFile inboundFile
    ) {

        if (line == null || line.length() != 200) {
            throw new IllegalStateException("Invalid SSIM record length");
        }

        if (line.charAt(0) != '3') {
            throw new IllegalStateException("Not a T3 record");
        }

        SsimInboundFlightLeg leg = new SsimInboundFlightLeg();
        leg.setFile(inboundFile);
        leg.setRecordType("3");

        // ---- Core identifiers ----
        leg.setOperationalVariation(line.substring(1, 2));   // byte 2
        leg.setAirlineCode(line.substring(2, 5).trim());;            // bytes 3–5
        leg.setFlightNumber(line.substring(5, 9).trim());           // bytes 6–9
        leg.setOperationalSuffix(line.substring(9, 11).trim());     // bytes 10–11
        leg.setLegSequence(line.substring(11, 13));          // bytes 12–13
        leg.setServiceType(line.substring(13, 14));          // byte 14

        // ---- Period & days ----
        leg.setPeriodStartRaw(line.substring(14, 21));          // bytes 15–21
        leg.setPeriodEndRaw(line.substring(21, 28));            // bytes 22–28
        leg.setOperatingDays(line.substring(28, 35));        // bytes 29–35

        // ---- Departure ----
        leg.setDepartureAirport(line.substring(35, 38));     // bytes 36–38
        leg.setDepartureTimeRaw(line.substring(38, 42));        // bytes 39–42
        leg.setDepartureUtcOffsetRaw(line.substring(42, 47));   // bytes 43–47
        leg.setDepartureDayRelative(line.substring(47, 48)); // byte 48
        leg.setDepartureTerminal(line.substring(48, 49));    // byte 49

        // ---- Arrival ----
        leg.setArrivalAirport(line.substring(49, 52));       // bytes 50–52
        leg.setArrivalTimeRaw(line.substring(52, 56));          // bytes 53–56
        leg.setArrivalUtcOffsetRaw(line.substring(56, 61));     // bytes 57–61
        leg.setArrivalDayChange(line.substring(61, 62));     // byte 62
        leg.setArrivalTerminal(line.substring(62, 63));      // byte 63

        // ---- Equipment & service ----
        leg.setAircraftType(line.substring(63, 66));         // bytes 64–66
        leg.setBookingClasses(line.substring(66, 86));       // bytes 67–86
        leg.setOnwardFlight(line.substring(86, 91));         // bytes 87–91
        leg.setTransitIndicator(line.substring(91, 92));     // byte 92
        leg.setMealCodes(line.substring(92, 127));           // bytes 93–127

        // ---- DEI + spare ----
        leg.setDeiRaw(line.substring(127, 184));          // bytes 128–184
        leg.setSpare185To191(line.substring(184, 191));              // bytes 185–191

        // ---- Footer ----
        leg.setIvrIndicator(line.substring(191, 192));       // byte 192
        leg.setDataSourceCode(line.substring(192, 194));     // bytes 193–194
        leg.setRecordSerialNumber(line.substring(194, 200)); // bytes 195–200

        leg.setRawRecord(line);

        return leg;
    }
}
