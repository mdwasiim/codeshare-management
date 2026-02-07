package com.codeshare.airline.processor.processing.parsing;


import com.codeshare.airline.processor.pipeline.dto.SsimR3FlightLegRecordDTO;
import com.codeshare.airline.processor.pipeline.model.SsimRawLine;
import org.springframework.stereotype.Component;


@Component
public class R3FlightLegParser implements RecordParser<SsimR3FlightLegRecordDTO> {

    @Override
    public SsimR3FlightLegRecordDTO parse(SsimRawLine ssimRawLine) {

        String line = ssimRawLine.getContent();

        // ---- structural sanity only ----
        if (line == null || line.length() < 78) {
            throw new IllegalArgumentException("Invalid SSIM Record-3 line length");
        }

        if (line.charAt(0) != '3') {
            throw new IllegalArgumentException("Not an SSIM Record-3 line");
        }

        return SsimR3FlightLegRecordDTO.builder()
                .airlineDesignator(substring(line, 2, 4))          // Bytes 2–4
                .flightNumber(substring(line, 5, 8))               // Bytes 5–8
                .operationalSuffix(substring(line, 9, 9))          // Byte 9

                .originAirport(substring(line, 10, 12))            // Bytes 10–12
                .destinationAirport(substring(line, 13, 15))       // Bytes 13–15

                .scheduledDepartureTime(substring(line, 16, 19))   // Bytes 16–19
                .scheduledArrivalTime(substring(line, 20, 23))     // Bytes 20–23
                .overnightIndicator(substring(line, 24, 24))       // Byte 24

                .periodStartDate(substring(line, 25, 30))          // Bytes 25–30
                .periodEndDate(substring(line, 31, 36))            // Bytes 31–36
                .daysOfOperation(substring(line, 37, 43))          // Bytes 37–43

                .aircraftType(substring(line, 44, 46))             // Bytes 44–46
                .aircraftConfiguration(substring(line, 47, 66))    // Bytes 47–66

                .serviceType(substring(line, 67, 68))              // Bytes 67–68
                .trafficRestrictionCode(substring(line, 69, 70))   // Bytes 69–70

                .remarks(substring(line, 72, 78))                  // Bytes 72–78
                .build();
    }

    /**
     * SSIM positions are 1-based and inclusive.
     * DO NOT trim or normalize.
     */
    private String substring(String line, int start, int end) {
        return line.substring(start - 1, end);
    }
}

