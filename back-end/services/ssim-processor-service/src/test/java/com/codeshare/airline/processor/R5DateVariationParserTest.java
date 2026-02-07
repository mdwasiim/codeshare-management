/*
package com.codeshare.airline.processor;

import com.codeshare.airline.processor.processing.parsing.R5ContinuationParser;
import com.codeshare.airline.processor.pipeline.dto.R5DateVariationDto;

import java.time.LocalDate;
import java.util.List;

class R5DateVariationParserTest {

    private final R5ContinuationParser parser = new R5ContinuationParser();

    @Test
    void shouldParseMultipleDates() {

        String line = "501JAN2602JAN2603JAN26";

        List<R5DateVariationDto> dates = parser.parse(line);

        assertEquals(3, dates.size());
        assertEquals(LocalDate.of(2026, 1, 1), dates.get(0).getVariationDate());
        assertEquals(LocalDate.of(2026, 1, 2), dates.get(1).getVariationDate());
        assertEquals(LocalDate.of(2026, 1, 3), dates.get(2).getVariationDate());
    }
}
*/
