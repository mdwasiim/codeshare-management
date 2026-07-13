package com.codeshare.airline.schedule.ingestion.persistence.entities.schedule;

import com.codeshare.airline.schedule.ingestion.domain.enums.DeiScope;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ScheduleDataElementEntityTest {

    @Test
    void flightOwnershipClearsLegParent() {
        ScheduleFlightEntity flight = new ScheduleFlightEntity();
        ScheduleLegEntity leg = new ScheduleLegEntity();
        ScheduleDataElementEntity dei = new ScheduleDataElementEntity();
        dei.setScope(DeiScope.FLIGHT);
        dei.attachToLeg(leg);

        flight.addDei(dei);

        assertThat(dei.getFlight()).isSameAs(flight);
        assertThat(dei.getLeg()).isNull();
    }

    @Test
    void legOwnershipClearsFlightParent() {
        ScheduleFlightEntity flight = new ScheduleFlightEntity();
        ScheduleLegEntity leg = new ScheduleLegEntity();
        ScheduleDataElementEntity dei = new ScheduleDataElementEntity();
        dei.setScope(DeiScope.LEG);
        dei.attachToFlight(flight);

        leg.addDei(dei);

        assertThat(dei.getLeg()).isSameAs(leg);
        assertThat(dei.getFlight()).isNull();
    }

    @Test
    void segmentDeiRequiresBoardAndOffPoints() throws Exception {
        ScheduleLegEntity leg = new ScheduleLegEntity();
        ScheduleDataElementEntity dei = new ScheduleDataElementEntity();
        dei.setScope(DeiScope.SEGMENT);
        dei.attachToLeg(leg);

        Method validationMethod = ScheduleDataElementEntity.class.getDeclaredMethod("validateOwnership");
        validationMethod.setAccessible(true);

        assertThatThrownBy(() -> validationMethod.invoke(dei))
                .isInstanceOf(InvocationTargetException.class)
                .hasCauseInstanceOf(IllegalStateException.class)
                .hasRootCauseMessage("Segment-level DEI requires board and off points");
    }
}
