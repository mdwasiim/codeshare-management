package com.codeshare.airline.schedule.processing.application;

import com.codeshare.airline.platform.core.dto.schedule.workflow.ImportBatchDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ImportedScheduleDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ActiveScheduleDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ScheduleFlightSnapshotDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ScheduleLegSnapshotDTO;
import com.codeshare.airline.platform.core.events.schedule.ChangeSetCreatedEvent;
import com.codeshare.airline.platform.core.events.schedule.ImportCompletedEvent;
import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import com.codeshare.airline.schedule.processing.domain.entity.ChangeSetEntity;
import com.codeshare.airline.schedule.processing.domain.enums.LegChangeType;
import com.codeshare.airline.schedule.processing.domain.repository.ChangeSetRepository;
import com.codeshare.airline.schedule.processing.feign.ImportBatchClient;
import com.codeshare.airline.schedule.processing.feign.ActiveScheduleClient;
import com.codeshare.airline.schedule.processing.integration.kafka.ChangeSetCreatedEventPublisher;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ScheduleComparisonServiceTest {

    @Test
    void compareCreatesNewLegChangeSetForFirstOperationalLoad() {
        ImportBatchClient importedClient = mock(ImportBatchClient.class);
        ActiveScheduleClient operationalClient = mock(ActiveScheduleClient.class);
        ChangeSetRepository repository = mock(ChangeSetRepository.class);
        ChangeSetCreatedEventPublisher publisher = mock(ChangeSetCreatedEventPublisher.class);

        ScheduleComparisonService service = new ScheduleComparisonService(
                importedClient,
                operationalClient,
                repository,
                publisher,
                new ObjectMapper().findAndRegisterModules()
        );

        UUID fileId = UUID.randomUUID();
        UUID loadId = UUID.randomUUID();
        ImportCompletedEvent event = ImportCompletedEvent.builder()
                .importedScheduleId(fileId)
                .importBatchId(loadId)
                .messageType(MessageType.SSIM)
                .airlineCode("QR")
                .sourceName("winter-2026.ssim")
                .completedAt(Instant.now())
                .build();

        ImportedScheduleDTO importedSchedule = ImportedScheduleDTO.builder()
                .importedScheduleId(fileId)
                .importBatchId(loadId)
                .messageType(MessageType.SSIM)
                .airlineCode("QR")
                .flights(java.util.List.of(
                        ScheduleFlightSnapshotDTO.builder()
                                .flightId(UUID.randomUUID())
                                .airlineCode("QR")
                                .flightNumber("0701")
                                .operationalSuffix("")
                                .itineraryVariationId("00")
                                .legs(java.util.List.of(
                                        ScheduleLegSnapshotDTO.builder()
                                                .legId(UUID.randomUUID())
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
                                                .build()
                                ))
                                .build()
                ))
                .build();

        when(repository.findByImportBatchId(loadId)).thenReturn(Optional.empty());
        when(importedClient.getImportBatch(loadId)).thenReturn(ImportBatchDTO.builder()
                .importBatchId(loadId)
                .importedScheduleId(fileId)
                .messageType(MessageType.SSIM)
                .airlineCode("QR")
                .sourceName("winter-2026.ssim")
                .importedSchedule(importedSchedule)
                .build());
        when(operationalClient.getActiveSchedule("QR"))
                .thenReturn(ActiveScheduleDTO.builder().airlineCode("QR").build());
        when(repository.save(any(ChangeSetEntity.class))).thenAnswer(invocation -> {
            ChangeSetEntity run = invocation.getArgument(0);
            if (run.getId() == null) {
                run.setId(UUID.randomUUID());
            }
            return run;
        });

        UUID changeSetId = service.compare(event);

        assertThat(changeSetId).isNotNull();

        ArgumentCaptor<ChangeSetEntity> runCaptor = ArgumentCaptor.forClass(ChangeSetEntity.class);
        verify(repository, org.mockito.Mockito.atLeastOnce()).save(runCaptor.capture());
        ChangeSetEntity finalRun = runCaptor.getAllValues().getLast();

        assertThat(finalRun.getFlightChanges()).hasSize(1);
        assertThat(finalRun.getFlightChanges().getFirst().getLegChanges()).hasSize(1);
        assertThat(finalRun.getFlightChanges().getFirst().getLegChanges().getFirst().getChangeType()).isEqualTo(LegChangeType.NEW);

        ArgumentCaptor<ChangeSetCreatedEvent> eventCaptor = ArgumentCaptor.forClass(ChangeSetCreatedEvent.class);
        verify(publisher).publish(eventCaptor.capture());
        assertThat(eventCaptor.getValue().getChangeSetId()).isEqualTo(changeSetId);
        assertThat(eventCaptor.getValue().getImportBatchId()).isEqualTo(loadId);
    }
}


