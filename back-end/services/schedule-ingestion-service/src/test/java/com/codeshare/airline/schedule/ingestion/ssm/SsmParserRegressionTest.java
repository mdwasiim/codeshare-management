package com.codeshare.airline.schedule.ingestion.ssm;

import com.codeshare.airline.schedule.ingestion.dto.schedule.ScheduleMessageDTO;
import com.codeshare.airline.schedule.ingestion.orchestration.parsers.SsmMessageParser;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SsmParserRegressionTest {

    private final SsmMessageParser parser = new SsmMessageParser();

    @Test
    void parses_period_with_year_and_frequency() {
        ScheduleMessageDTO parsed = parser.parseMessage(parser.groupMessage(List.of(
                "SSM",
                "UTC",
                "25MAY00144E003/REF 123/449",
                "NEW XASM",
                "QR1234",
                "12AUG09 30SEP09 1234567/W2",
                "DOH LHR 0800 1400",
                "G 320 FCMY"
        )));

        assertThat(parsed.getMessageReference()).isEqualTo("REF 123/449");
        assertThat(parsed.getMessages().getFirst().getFlights().getFirst().getPeriods().getFirst().getFrequencyRate())
                .isEqualTo(2);
        assertThat(parsed.getMessages().getFirst().getFlights().getFirst().getPeriods().getFirst().getDaysOfOperation())
                .isEqualTo("1234567");
    }
}
