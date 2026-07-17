package com.codeshare.airline.schedule.live.application;

import com.codeshare.airline.platform.core.dto.schedule.workflow.ChangeSetDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ScheduleDataElementSnapshotDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ScheduleFlightChangeDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ScheduleLegChangeDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ScheduleLegSnapshotDTO;
import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import com.codeshare.airline.platform.core.enums.schedule.ScheduleLegChangeType;
import com.codeshare.airline.schedule.live.domain.entity.ActiveScheduleEntity;
import com.codeshare.airline.schedule.live.domain.entity.LiveFlightEntity;
import com.codeshare.airline.schedule.live.domain.entity.LiveScheduleVersionEntity;
import com.codeshare.airline.schedule.live.domain.repository.ActiveScheduleRepository;
import com.codeshare.airline.schedule.live.domain.repository.LiveFlightRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ScheduleChangeApplicationServiceTest {

    @Test
    void applyCreatesLiveFlightLegAndVersionForNewScheduleChange() {
        LiveFlightRepository repository = mock(LiveFlightRepository.class);
        ActiveScheduleRepository activeScheduleRepository = mock(ActiveScheduleRepository.class);
        when(repository.findByAirlineCodeAndFlightNumberAndOperationalSuffixAndItineraryVariationId(
                "QR", "0701", "", "00"
        )).thenReturn(Optional.empty());
        when(repository.save(any(LiveFlightEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(activeScheduleRepository.findByAirlineCode("QR")).thenReturn(Optional.empty());
        when(activeScheduleRepository.save(any(ActiveScheduleEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ScheduleChangeApplicationService service = new ScheduleChangeApplicationService(
                repository,
                activeScheduleRepository,
                new ActiveScheduleMapper(),
                new ObjectMapper().findAndRegisterModules()
        );

        ScheduleLegSnapshotDTO legSnapshot = ScheduleLegSnapshotDTO.builder()
                .legSequenceNumber(1)
                .periodStart(LocalDate.of(2026, 10, 1))
                .periodEnd(LocalDate.of(2026, 10, 31))
                .daysOfOperation("1234567")
                .departureStation("DOH")
                .arrivalStation("LHR")
                .scheduledDepartureTime(LocalTime.of(8, 0))
                .scheduledArrivalTime(LocalTime.of(13, 0))
                .aircraftType("359")
                .serviceType("J")
                .legDataElements(List.of(
                        ScheduleDataElementSnapshotDTO.builder()
                                .code("050")
                                .value("TRF")
                                .scope("LEG")
                                .sequenceOrder(1)
                                .build()
                ))
                .build();

        ChangeSetDTO changeSet = ChangeSetDTO.builder()
                .changeSetId(UUID.randomUUID())
                .importedScheduleId(UUID.randomUUID())
                .importBatchId(UUID.randomUUID())
                .messageType(MessageType.SSIM)
                .airlineCode("QR")
                .flightChanges(List.of(
                        ScheduleFlightChangeDTO.builder()
                                .airlineCode("QR")
                                .flightNumber("0701")
                                .operationalSuffix("")
                                .itineraryVariationId("00")
                                .legChanges(List.of(
                                        ScheduleLegChangeDTO.builder()
                                                .changeType(ScheduleLegChangeType.NEW)
                                                .newValue(legSnapshot)
                                                .build()
                                ))
                                .build()
                ))
                .build();

        service.apply(changeSet);

        ArgumentCaptor<LiveFlightEntity> flightCaptor = ArgumentCaptor.forClass(LiveFlightEntity.class);
        verify(repository).save(flightCaptor.capture());
        LiveFlightEntity savedFlight = flightCaptor.getValue();

        assertThat(savedFlight.getLegs()).hasSize(1);
        assertThat(savedFlight.getLegs().getFirst().getDepartureStation()).isEqualTo("DOH");
        assertThat(savedFlight.getLegs().getFirst().getLegDataElements()).hasSize(1);
        assertThat(savedFlight.getLegs().getFirst().getLegDataElements().getFirst().getDataElementIdentifier()).isEqualTo("050");
        assertThat(savedFlight.getLegs().getFirst().getVersions()).hasSize(1);
        LiveScheduleVersionEntity version = savedFlight.getLegs().getFirst().getVersions().getFirst();
        assertThat(version.getMessageType()).isEqualTo(MessageType.SSIM);
    }
}

