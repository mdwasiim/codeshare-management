package com.codeshare.airline.schedule.message.application;

import com.codeshare.airline.platform.core.dto.master.validation.ScheduleCodeListValidationErrorDTO;
import com.codeshare.airline.platform.core.dto.master.validation.ScheduleCodeListValidationRequestDTO;
import com.codeshare.airline.platform.core.dto.master.validation.ScheduleCodeListValidationResponseDTO;
import com.codeshare.airline.platform.core.dto.master.validation.ScheduleTimeValidationErrorDTO;
import com.codeshare.airline.platform.core.dto.master.validation.ScheduleTimeValidationRequestDTO;
import com.codeshare.airline.platform.core.dto.master.validation.ScheduleTimeValidationResponseDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ChangeSetDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ScheduleCodeshareSnapshotDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ScheduleDataElementSnapshotDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ScheduleFlightChangeDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ScheduleLegChangeDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ScheduleLegSnapshotDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ScheduleSegmentSnapshotDTO;
import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import com.codeshare.airline.platform.core.enums.schedule.ScheduleLegChangeType;
import com.codeshare.airline.platform.core.events.schedule.DistributionRequestedEvent;
import com.codeshare.airline.platform.core.events.schedule.ScheduleUpdatedEvent;
import com.codeshare.airline.schedule.message.domain.entity.OutboundScheduleMessageEntity;
import com.codeshare.airline.schedule.message.domain.enums.OutboundScheduleMessageAuditEventType;
import com.codeshare.airline.schedule.message.domain.enums.OutboundScheduleMessageStatus;
import com.codeshare.airline.schedule.message.domain.repository.OutboundScheduleMessageRepository;
import com.codeshare.airline.schedule.message.feign.CompareChangeSetClient;
import com.codeshare.airline.schedule.message.feign.MasterDataScheduleCodeListClient;
import com.codeshare.airline.schedule.message.feign.MasterDataScheduleTimeClient;
import com.codeshare.airline.schedule.message.integration.kafka.DistributionRequestedEventPublisher;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ScheduleMessageServiceTest {

    @Test
    void generateCreatesOutboundMessageAndRequestsDistribution() {
        OutboundScheduleMessageRepository repository = mock(OutboundScheduleMessageRepository.class);
        CompareChangeSetClient compareChangeSetClient = mock(CompareChangeSetClient.class);
        ScheduleCodeListValidator codeListValidator = mock(ScheduleCodeListValidator.class);
        ScheduleTimeValidator timeValidator = mock(ScheduleTimeValidator.class);
        OutboundScheduleMessageAuditService auditService = mock(OutboundScheduleMessageAuditService.class);
        DistributionRequestedEventPublisher publisher = mock(DistributionRequestedEventPublisher.class);
        ScheduleMessageService service = new ScheduleMessageService(
                repository,
                compareChangeSetClient,
                codeListValidator,
                timeValidator,
                new OutboundScheduleMessageGenerator(),
                auditService,
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
                                        .legSequenceNumber(1)
                                        .periodStart(LocalDate.of(2026, 3, 1))
                                        .periodEnd(LocalDate.of(2026, 10, 24))
                                        .daysOfOperation("1234567")
                                        .departureStation("DOH")
                                        .arrivalStation("LHR")
                                        .scheduledDepartureTime(LocalTime.of(8, 30))
                                        .aircraftDepartureTime(LocalTime.of(8, 30))
                                        .scheduledArrivalTime(LocalTime.of(13, 20))
                                        .aircraftArrivalTime(LocalTime.of(13, 20))
                                        .departureUtcOffset("+0300")
                                        .arrivalUtcOffset("+0000")
                                        .aircraftType("77W")
                                        .aircraftConfiguration("CY")
                                        .serviceType("J")
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
        verify(codeListValidator).validate(changeSet);
        verify(timeValidator).validate(changeSet);
        verify(auditService).record(any(), any(), org.mockito.Mockito.eq(OutboundScheduleMessageAuditEventType.PAYLOAD_GENERATED), any());
        verify(auditService).record(any(), any(), org.mockito.Mockito.eq(OutboundScheduleMessageAuditEventType.DISTRIBUTION_REQUESTED), any());
    }

    @Test
    void generateMarksFailedAndDoesNotRequestDistributionWhenMandatoryFieldMissing() {
        OutboundScheduleMessageRepository repository = mock(OutboundScheduleMessageRepository.class);
        CompareChangeSetClient compareChangeSetClient = mock(CompareChangeSetClient.class);
        ScheduleCodeListValidator codeListValidator = mock(ScheduleCodeListValidator.class);
        ScheduleTimeValidator timeValidator = mock(ScheduleTimeValidator.class);
        OutboundScheduleMessageAuditService auditService = mock(OutboundScheduleMessageAuditService.class);
        DistributionRequestedEventPublisher publisher = mock(DistributionRequestedEventPublisher.class);
        ScheduleMessageService service = new ScheduleMessageService(
                repository,
                compareChangeSetClient,
                codeListValidator,
                timeValidator,
                new OutboundScheduleMessageGenerator(),
                auditService,
                publisher
        );

        UUID changeSetId = UUID.randomUUID();
        ScheduleUpdatedEvent event = ScheduleUpdatedEvent.builder()
                .changeSetId(changeSetId)
                .changeRequestId(10L)
                .messageType(MessageType.SSM)
                .airlineCode("QR")
                .partnerCode("BA")
                .updatedAt(Instant.now())
                .build();

        ChangeSetDTO invalid = ChangeSetDTO.builder()
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
        when(compareChangeSetClient.getChangeSet(changeSetId)).thenReturn(invalid);

        UUID outboundMessageId = service.generate(event);

        assertThat(outboundMessageId).isNotNull();
        ArgumentCaptor<OutboundScheduleMessageEntity> messageCaptor = ArgumentCaptor.forClass(OutboundScheduleMessageEntity.class);
        verify(repository).save(messageCaptor.capture());
        assertThat(messageCaptor.getValue().getStatus()).isEqualTo(OutboundScheduleMessageStatus.FAILED);
        assertThat(messageCaptor.getValue().getErrorMessage()).contains("Outbound IATA compliance failure");
        verify(auditService).record(any(), any(), org.mockito.Mockito.eq(OutboundScheduleMessageAuditEventType.GENERATION_FAILED), any());
        verify(publisher, never()).publish(any());
    }

    @Test
    void generateMarksFailedAndDoesNotRequestDistributionWhenMasterDataValidationFails() {
        OutboundScheduleMessageRepository repository = mock(OutboundScheduleMessageRepository.class);
        CompareChangeSetClient compareChangeSetClient = mock(CompareChangeSetClient.class);
        ScheduleCodeListValidator codeListValidator = mock(ScheduleCodeListValidator.class);
        ScheduleTimeValidator timeValidator = mock(ScheduleTimeValidator.class);
        OutboundScheduleMessageAuditService auditService = mock(OutboundScheduleMessageAuditService.class);
        DistributionRequestedEventPublisher publisher = mock(DistributionRequestedEventPublisher.class);
        ScheduleMessageService service = new ScheduleMessageService(
                repository,
                compareChangeSetClient,
                codeListValidator,
                timeValidator,
                new OutboundScheduleMessageGenerator(),
                auditService,
                publisher
        );

        UUID changeSetId = UUID.randomUUID();
        ScheduleUpdatedEvent event = ScheduleUpdatedEvent.builder()
                .changeSetId(changeSetId)
                .changeRequestId(10L)
                .messageType(MessageType.SSM)
                .airlineCode("QR")
                .partnerCode("BA")
                .updatedAt(Instant.now())
                .build();
        ChangeSetDTO changeSet = changeSet(MessageType.SSM, ScheduleLegChangeType.NEW);

        when(repository.findByChangeSetId(changeSetId)).thenReturn(Optional.empty());
        when(repository.save(any(OutboundScheduleMessageEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(compareChangeSetClient.getChangeSet(changeSetId)).thenReturn(changeSet);
        org.mockito.Mockito.doThrow(new OutboundScheduleMessageComplianceException(
                        "Outbound master-data validation failure: aircraftTypeCodes=ZZZ"))
                .when(codeListValidator).validate(changeSet);

        UUID outboundMessageId = service.generate(event);

        assertThat(outboundMessageId).isNotNull();
        ArgumentCaptor<OutboundScheduleMessageEntity> messageCaptor = ArgumentCaptor.forClass(OutboundScheduleMessageEntity.class);
        verify(repository).save(messageCaptor.capture());
        assertThat(messageCaptor.getValue().getStatus()).isEqualTo(OutboundScheduleMessageStatus.FAILED);
        assertThat(messageCaptor.getValue().getErrorMessage()).contains("Outbound master-data validation failure");
        verify(auditService).record(any(), any(), org.mockito.Mockito.eq(OutboundScheduleMessageAuditEventType.VALIDATION_FAILED), any());
        verify(publisher, never()).publish(any());
    }

    @Test
    void generateMarksFailedAndDoesNotRequestDistributionWhenScheduleTimeValidationFails() {
        OutboundScheduleMessageRepository repository = mock(OutboundScheduleMessageRepository.class);
        CompareChangeSetClient compareChangeSetClient = mock(CompareChangeSetClient.class);
        ScheduleCodeListValidator codeListValidator = mock(ScheduleCodeListValidator.class);
        ScheduleTimeValidator timeValidator = mock(ScheduleTimeValidator.class);
        OutboundScheduleMessageAuditService auditService = mock(OutboundScheduleMessageAuditService.class);
        DistributionRequestedEventPublisher publisher = mock(DistributionRequestedEventPublisher.class);
        ScheduleMessageService service = new ScheduleMessageService(
                repository,
                compareChangeSetClient,
                codeListValidator,
                timeValidator,
                new OutboundScheduleMessageGenerator(),
                auditService,
                publisher
        );

        UUID changeSetId = UUID.randomUUID();
        ScheduleUpdatedEvent event = ScheduleUpdatedEvent.builder()
                .changeSetId(changeSetId)
                .changeRequestId(10L)
                .messageType(MessageType.SSIM)
                .airlineCode("QR")
                .partnerCode("BA")
                .updatedAt(Instant.now())
                .build();
        ChangeSetDTO changeSet = changeSet(MessageType.SSIM, ScheduleLegChangeType.NEW);

        when(repository.findByChangeSetId(changeSetId)).thenReturn(Optional.empty());
        when(repository.save(any(OutboundScheduleMessageEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(compareChangeSetClient.getChangeSet(changeSetId)).thenReturn(changeSet);
        org.mockito.Mockito.doThrow(new OutboundScheduleMessageComplianceException(
                        "Outbound schedule time validation failure: legs[0].arrivalUtcOffset"))
                .when(timeValidator).validate(changeSet);

        UUID outboundMessageId = service.generate(event);

        assertThat(outboundMessageId).isNotNull();
        ArgumentCaptor<OutboundScheduleMessageEntity> messageCaptor = ArgumentCaptor.forClass(OutboundScheduleMessageEntity.class);
        verify(repository).save(messageCaptor.capture());
        assertThat(messageCaptor.getValue().getStatus()).isEqualTo(OutboundScheduleMessageStatus.FAILED);
        assertThat(messageCaptor.getValue().getErrorMessage()).contains("Outbound schedule time validation failure");
        verify(auditService).record(any(), any(), org.mockito.Mockito.eq(OutboundScheduleMessageAuditEventType.VALIDATION_FAILED), any());
        verify(publisher, never()).publish(any());
    }

    @Test
    void codeListValidatorRaisesComplianceFailureForInvalidMasterDataCodes() {
        MasterDataScheduleCodeListClient client = mock(MasterDataScheduleCodeListClient.class);
        ScheduleCodeListValidator validator = new ScheduleCodeListValidator(client);
        ScheduleCodeListValidationResponseDTO response = new ScheduleCodeListValidationResponseDTO(
                false,
                List.of(new ScheduleCodeListValidationErrorDTO("aircraftTypeCodes", "ZZZ", "Unknown IATA aircraft type"))
        );
        when(client.validate(any())).thenReturn(response);

        org.assertj.core.api.Assertions.assertThatThrownBy(() -> validator.validate(changeSet(MessageType.SSM, ScheduleLegChangeType.NEW)))
                .isInstanceOf(OutboundScheduleMessageComplianceException.class)
                .hasMessageContaining("aircraftTypeCodes=ZZZ");
    }

    @Test
    void codeListValidatorSendsExpandedMasterDataCodes() {
        MasterDataScheduleCodeListClient client = mock(MasterDataScheduleCodeListClient.class);
        ScheduleCodeListValidator validator = new ScheduleCodeListValidator(client);
        when(client.validate(any())).thenReturn(new ScheduleCodeListValidationResponseDTO(true, List.of()));

        ChangeSetDTO changeSet = changeSet(MessageType.SSIM, ScheduleLegChangeType.COD);
        ScheduleLegSnapshotDTO snapshot = changeSet.getFlightChanges().getFirst()
                .getLegChanges().getFirst()
                .getNewValue();
        snapshot.setDepartureTerminal("T1");
        snapshot.setArrivalTerminal("T5");
        snapshot.setMealServiceNote("BL");
        snapshot.setSecureFlightIndicator("Y");
        snapshot.setTrafficRestrictionCode("TNT");
        changeSet.getFlightChanges().getFirst().setOperationalSuffix("A");

        validator.validate(changeSet);

        ArgumentCaptor<ScheduleCodeListValidationRequestDTO> requestCaptor =
                ArgumentCaptor.forClass(ScheduleCodeListValidationRequestDTO.class);
        verify(client).validate(requestCaptor.capture());
        ScheduleCodeListValidationRequestDTO request = requestCaptor.getValue();
        assertThat(request.getTerminalCodes()).anySatisfy(terminal -> {
            assertThat(terminal.getAirportCode()).isEqualTo("DOH");
            assertThat(terminal.getTerminalCode()).isEqualTo("T1");
        });
        assertThat(request.getTerminalCodes()).anySatisfy(terminal -> {
            assertThat(terminal.getAirportCode()).isEqualTo("LHR");
            assertThat(terminal.getTerminalCode()).isEqualTo("T5");
        });
        assertThat(request.getMealServiceCodes()).contains("B", "L");
        assertThat(request.getSecureFlightIndicatorCodes()).contains("Y");
        assertThat(request.getActionCodes()).contains("COD");
        assertThat(request.getOperationalSuffixCodes()).contains("A");
        assertThat(request.getFlightSuffixCodes()).contains("A");
        assertThat(request.getTrafficRestrictionCodes()).contains("T");
        assertThat(request.getTrafficRestrictionQualifiers()).anySatisfy(qualifier -> {
            assertThat(qualifier.getRestrictionCode()).isEqualTo("T");
            assertThat(qualifier.getQualifierCode()).isEqualTo("NT");
        });
    }

    @Test
    void timeValidatorSendsLegTimesAndRaisesComplianceFailure() {
        MasterDataScheduleTimeClient client = mock(MasterDataScheduleTimeClient.class);
        ScheduleTimeValidator validator = new ScheduleTimeValidator(client);
        when(client.validate(any())).thenReturn(new ScheduleTimeValidationResponseDTO(
                false,
                List.of(new ScheduleTimeValidationErrorDTO("legs[0].arrivalUtcOffset", "Expected +0100 for LHR"))
        ));

        org.assertj.core.api.Assertions.assertThatThrownBy(() -> validator.validate(changeSet(MessageType.SSIM, ScheduleLegChangeType.NEW)))
                .isInstanceOf(OutboundScheduleMessageComplianceException.class)
                .hasMessageContaining("legs[0].arrivalUtcOffset");

        ArgumentCaptor<ScheduleTimeValidationRequestDTO> requestCaptor =
                ArgumentCaptor.forClass(ScheduleTimeValidationRequestDTO.class);
        verify(client).validate(requestCaptor.capture());
        assertThat(requestCaptor.getValue().getLegs()).hasSize(1);
        assertThat(requestCaptor.getValue().getLegs().getFirst().getDepartureAirport()).isEqualTo("DOH");
        assertThat(requestCaptor.getValue().getLegs().getFirst().getArrivalAirport()).isEqualTo("LHR");
    }

    @Test
    void generatorCreatesChapter4SsmPayload() {
        OutboundScheduleMessageGenerator generator = new OutboundScheduleMessageGenerator();

        String payload = generator.generate(changeSet(MessageType.SSM, ScheduleLegChangeType.TIM));

        assertThat(payload).startsWith("SSM");
        assertThat(payload).contains(System.lineSeparator() + "UTC" + System.lineSeparator());
        assertThat(payload).contains(System.lineSeparator() + "TIM" + System.lineSeparator());
        assertThat(payload).contains("QR0701");
        assertThat(payload).contains("DOH0830 LHR1320");
        assertThat(payload).contains("2/BA");
        assertThat(payload).contains("DOHLHR 105/10000K");
        assertThat(payload).contains("DOHLHR 10/BA1701");
        assertThat(payload.lines()).allMatch(line -> line.length() <= 69);
    }

    @Test
    void generatorCreatesChapter5AsmPayload() {
        OutboundScheduleMessageGenerator generator = new OutboundScheduleMessageGenerator();

        String payload = generator.generate(changeSet(MessageType.ASM, ScheduleLegChangeType.NEW));

        assertThat(payload).startsWith("ASM");
        assertThat(payload).contains(System.lineSeparator() + "NEW SCHG" + System.lineSeparator());
        assertThat(payload).contains("QR0701/01MAR26");
        assertThat(payload).contains("J 77W JY.CY");
        assertThat(payload.lines()).allMatch(line -> line.length() <= 69);
    }

    @Test
    void generatorPreservesCodeshareChangeAction() {
        OutboundScheduleMessageGenerator generator = new OutboundScheduleMessageGenerator();

        String payload = generator.generate(changeSet(MessageType.SSM, ScheduleLegChangeType.COD));

        assertThat(payload).contains(System.lineSeparator() + "COD" + System.lineSeparator());
        assertThat(payload).contains("DOHLHR 10/BA1701");
    }

    @Test
    void generatorCreatesChapter7SsimFixedRecords() {
        OutboundScheduleMessageGenerator generator = new OutboundScheduleMessageGenerator();

        ChangeSetDTO changeSet = changeSet(MessageType.SSIM, ScheduleLegChangeType.NEW);
        changeSet.getFlightChanges().getFirst()
                .getLegChanges().getFirst()
                .getNewValue()
                .setSecureFlightIndicator("Y");

        String payload = generator.generate(changeSet);

        List<String> records = payload.lines().toList();
        assertThat(records).hasSizeGreaterThanOrEqualTo(4);
        assertThat(records).allMatch(record -> record.length() == 200);
        assertThat(records.get(0)).startsWith("1AIRLINE STANDARD SCHEDULE DATA SET");
        assertThat(records.subList(1, 5)).allMatch(record -> record.matches("0{200}"));
        assertThat(records.get(5)).startsWith("2UQR ");
        assertThat(records.subList(6, 10)).allMatch(record -> record.matches("0{200}"));
        assertThat(records.stream().anyMatch(record -> record.startsWith("3 QR 0701"))).isTrue();
        assertThat(records.stream().filter(record -> record.startsWith("3 QR 0701")).findFirst().orElseThrow()
                .charAt(121)).isEqualTo('S');
        assertThat(records.stream().anyMatch(record -> record.startsWith("5 QR "))).isTrue();
        assertThat(records.subList(records.size() - 10, records.size())).allMatch(record -> record.matches("0{200}"));
    }

    private ChangeSetDTO changeSet(MessageType messageType, ScheduleLegChangeType changeType) {
        UUID changeSetId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        return ChangeSetDTO.builder()
                .changeSetId(changeSetId)
                .messageType(messageType)
                .airlineCode("QR")
                .partnerCode("BA")
                .messageReference("TEST")
                .createdAt(Instant.parse("2026-02-01T10:15:00Z"))
                .flightChanges(List.of(ScheduleFlightChangeDTO.builder()
                        .airlineCode("QR")
                        .flightNumber("0701")
                        .legChanges(List.of(ScheduleLegChangeDTO.builder()
                                .changeType(changeType)
                                .newValue(ScheduleLegSnapshotDTO.builder()
                                        .legSequenceNumber(1)
                                        .periodStart(LocalDate.of(2026, 3, 1))
                                        .periodEnd(LocalDate.of(2026, 10, 24))
                                        .daysOfOperation("1234567")
                                        .departureStation("DOH")
                                        .arrivalStation("LHR")
                                        .scheduledDepartureTime(LocalTime.of(8, 30))
                                        .aircraftDepartureTime(LocalTime.of(8, 30))
                                        .scheduledArrivalTime(LocalTime.of(13, 20))
                                        .aircraftArrivalTime(LocalTime.of(13, 20))
                                        .departureUtcOffset("+0300")
                                        .arrivalUtcOffset("+0000")
                                        .aircraftType("77W")
                                        .aircraftConfiguration("CY")
                                        .serviceType("J")
                                        .bookingDesignator("JY")
                                        .legDataElements(List.of(ScheduleDataElementSnapshotDTO.builder()
                                                .code("2")
                                                .value("BA")
                                                .build()))
                                        .segments(List.of(ScheduleSegmentSnapshotDTO.builder()
                                                .boardPoint("DOH")
                                                .offPoint("LHR")
                                                .dataElements(List.of(ScheduleDataElementSnapshotDTO.builder()
                                                        .code("105")
                                                        .value("10000K")
                                                        .build()))
                                                .build()))
                                        .codeshares(List.of(ScheduleCodeshareSnapshotDTO.builder()
                                                .boardPoint("DOH")
                                                .offPoint("LHR")
                                                .marketingAirlineCode("BA")
                                                .marketingFlightNumber("1701")
                                                .codeshare(true)
                                                .build()))
                                        .build())
                                .build()))
                        .build()))
                .build();
    }
}
