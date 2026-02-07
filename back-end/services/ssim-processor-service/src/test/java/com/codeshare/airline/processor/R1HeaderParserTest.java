/*
package com.codeshare.airline.processor;

import com.codeshare.airline.processor.processing.parsing.R1HeaderParser;
import com.codeshare.airline.processor.pipeline.dto.R1HeaderDto;

class R1HeaderParserTest {

    private final R1HeaderParser parser = new R1HeaderParser();

    @Test
    void shouldParseValidHeader() {
        String line =
                "1QR  00000101JAN26 S01JAN2628MAR26 01 CThis is a test header line      ";

        R1HeaderDto dto = parser.parse(line);

        assertEquals("QR", dto.getAirlineDesignator());
        assertEquals("000001", dto.getDatasetSerialNo());
        assertEquals(LocalDate.of(2026, 1, 1), dto.getCreationDate());
        assertEquals("S", dto.getScheduleType());
    }
}
*/
