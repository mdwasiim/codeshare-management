/*
package com.codeshare.airline.processor;

import com.codeshare.airline.processor.processing.parsing.R4DateVariationParser;
import com.codeshare.airline.processor.pipeline.dto.R4DateVariationDto;

import java.time.LocalDate;

class R4DateVariationParserTest {

    private final R4DateVariationParser parser = new R4DateVariationParser();

    @Test
    void shouldParseValidRecord4() {

        String line =
                "4QR1234 A001A01JAN26 08000900320  QR1234 Y This is a test variation    ";

        R4DateVariationDto dto = parser.parse(line);

        assertEquals("QR", dto.getAirlineDesignator());
        assertEquals("1234", dto.getFlightNumber());
        assertEquals("A", dto.getActionCode());
        assertEquals(LocalDate.of(2026, 1, 1), dto.getVariationDate());
    }
}
*/
