package com.codeshare.airline.schedule.ingestion.asm;

import com.codeshare.airline.schedule.ingestion.dto.schedule.ScheduleMessageDTO;
import com.codeshare.airline.schedule.ingestion.orchestration.parsers.AsmMessageParser;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AsmParserRegressionTest {

    private final AsmMessageParser parser = new AsmMessageParser();

    @Test
    void parses_dated_asm_times_and_header_reference() {
        ScheduleMessageDTO parsed = parser.parseMessage(parser.groupMessage(List.of(
                "ASM",
                "LT",
                "13JAN00033E002/REF 910/33",
                "TIM COMM",
                "QR1234/12MAR",
                "DOH LHR 121830 122245"
        )));

        assertThat(parsed.getTimeMode()).isNotNull();
        assertThat(parsed.getMessageReference()).isEqualTo("REF 910/33");
        assertThat(parsed.getMessages().getFirst().getFlights().getFirst().getLegs().getFirst().getDepartureTime())
                .isEqualTo(LocalTime.of(18, 30));
        assertThat(parsed.getMessages().getFirst().getFlights().getFirst().getLegs().getFirst().getArrivalTime())
                .isEqualTo(LocalTime.of(22, 45));
    }
}
