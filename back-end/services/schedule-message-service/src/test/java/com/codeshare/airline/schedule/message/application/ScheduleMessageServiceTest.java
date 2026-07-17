package com.codeshare.airline.schedule.message.application;

import com.codeshare.airline.platform.core.dto.schedule.workflow.ChangeSetDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ScheduleFlightChangeDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ScheduleLegChangeDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ScheduleLegSnapshotDTO;
import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import com.codeshare.airline.platform.core.enums.schedule.ScheduleLegChangeType;
import com.codeshare.airline.platform.core.events.schedule.DistributionRequestedEvent;
import com.codeshare.airline.platform.core.events.schedule.ScheduleUpdatedEvent;
import com.codeshare.airline.schedule.message.domain.entity.OutboundScheduleMessageEntity;
import com.codeshare.airline.schedule.message.domain.enums.OutboundScheduleMessageStatus;
import com.codeshare.airline.schedule.message.domain.repository.OutboundScheduleMessageRepository;
import com.codeshare.airline.schedule.message.feign.CompareChangeSetClient;
import com.codeshare.airline.schedule.message.integration.kafka.DistributionRequestedEventPublisher;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ScheduleMessageServiceTest {

    @Test
    void generateCreatesOutboundMessageAndRequestsDistribution() {
        OutboundScheduleMessageRepository repository = mock(OutboundScheduleMessageRepository.class);
        CompareChangeSetClient compareChangeSetClient = mock(CompareChangeSetClient.class);
        DistributionRequestedEventPublisher publisher = mock(DistributionRequestedEventPublisher.class);
        ScheduleMessageService service = new ScheduleMessageService(
                repository,
                compareChangeSetClient,
                new OutboundScheduleMessageGenerator(),
                publisher
        );

        UUID changeSetId = UUID.randomUUID();
        UUID correlationId = UUID.randomUUID();
        ScheduleUpdatedEvent event = ScheduleUpdatedEvent.builder()
                .correlationId(correlationId)
                .causationId(changeSetId)
                .changeSetId(changeSetId)
                .changeRequestId(10L)
                .importedScheduleId(UUID.randomUUID())
                .importBatchId(UUID.randomUUID())
                .messageType(MessageType.SSM)
                .airlineCode("QR")
                .partnerCode("BA")
                .updatedAt(Instant.now())
                .build();

        ChangeSetDTO changeSet = ChangeSetDTO.builder()
                .changeSetId(changeSetId)
                .messageType(MessageType.SSM)
                .airlineCode("QR")
                .partnerCode("BA")
                .flightChanges(List.of(ScheduleFlightChangeDTO.builder()
                        .airlineCode("QR")
                        .flightNumber("0701")
                        .legChanges(List.of(ScheduleLegChangeDTO.builder()
                                .changeType(ScheduleLegChangeType.NEW)
                                .newValue(ScheduleLegSnapshotDTO.builder()
                                        .departureStation("DOH")
                                        .arrivalStation("LHR")
                                        .build())
                                .build()))
                        .build()))
                .build();

        when(repository.findByChangeSetId(changeSetId)).thenReturn(Optional.empty());
        when(repository.save(any(OutboundScheduleMessageEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(compareChangeSetClient.getChangeSet(changeSetId)).thenReturn(changeSet);

        UUID outboundMessageId = service.generate(event);

        assertThat(outboundMessageId).isNotNull();
        ArgumentCaptor<OutboundScheduleMessageEntity> messageCaptor = ArgumentCaptor.forClass(OutboundScheduleMessageEntity.class);
        verify(repository, org.mockito.Mockito.atLeastOnce()).save(messageCaptor.capture());
        OutboundScheduleMessageEntity finalMessage = messageCaptor.getAllValues().getLast();
        assertThat(finalMessage.getStatus()).isEqualTo(OutboundScheduleMessageStatus.DISTRIBUTION_REQUESTED);
        assertThat(finalMessage.getPayload()).contains("SSM");

        ArgumentCaptor<DistributionRequestedEvent> eventCaptor = ArgumentCaptor.forClass(DistributionRequestedEvent.class);
        verify(publisher).publish(eventCaptor.capture());
        assertThat(eventCaptor.getValue().getOutboundMessageId()).isEqualTo(outboundMessageId);
        assertThat(eventCaptor.getValue().getCorrelationId()).isEqualTo(correlationId);
        assertThat(eventCaptor.getValue().getCausationId()).isEqualTo(changeSetId);
    }
}
