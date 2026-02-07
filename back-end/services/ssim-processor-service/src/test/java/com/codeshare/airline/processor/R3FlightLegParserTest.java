/*
package com.codeshare.airline.processor;

import com.codeshare.airline.processor.processing.parsing.R3FlightLegParser;
import com.codeshare.airline.processor.pipeline.dto.R3FlightLegDto;

class R3FlightLegParserTest {

    private final R3FlightLegParser parser = new R3FlightLegParser();

    @Test
    void shouldParseValidRecord3() {

        String line =
                "3QR1234 A001JEDDOH08001000011111101JAN2628MAR26320  QR1234 YN0000";

        R3FlightLegDto dto = parser.parse(line);

        assertEquals("QR", dto.getAirlineDesignator());
        assertEquals("1234", dto.getFlightNumber());
        assertEquals("JED", dto.getDepartureStation());
        assertEquals("DOH", dto.getArrivalStation());
        assertEquals("1111110", dto.getDaysOfOperation());
    }
}
*/
