package com.codeshare.airline.master.validation.util;

import com.codeshare.airline.master.geography.repository.DstRuleRepository;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

class ScheduleTimeValidationUtilTest {

    @Test
    void parseOffsetRejectsValuesOutsideUtcRange() {
        ScheduleTimeValidationUtil util = new ScheduleTimeValidationUtil(mock(DstRuleRepository.class));

        assertThatThrownBy(() -> util.parseOffsetMinutes("+2460"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("valid range");
    }
}
