package com.codeshare.airline.distribution.engine.application;

import com.codeshare.airline.distribution.engine.domain.entity.DistributionJobEntity;
import com.codeshare.airline.distribution.engine.domain.enums.DistributionJobStatus;
import com.codeshare.airline.distribution.engine.domain.repository.DistributionJobRepository;
import com.codeshare.airline.distribution.engine.feign.OutboundScheduleMessageClient;
import com.codeshare.airline.distribution.engine.feign.TenantCommunicationProfileClient;
import com.codeshare.airline.distribution.engine.feign.TenantDistributionProfileClient;
import com.codeshare.airline.distribution.engine.integration.adapter.DistributionAdapter;
import com.codeshare.airline.platform.core.dto.master.codesharepartner.CodesharePartnerCommunicationProfileDTO;
import com.codeshare.airline.platform.core.dto.master.codesharepartner.CodesharePartnerDistributionProfileDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.OutboundScheduleMessageDTO;
import com.codeshare.airline.platform.core.enums.master.codesharepartner.CommunicationProtocol;
import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import com.codeshare.airline.platform.core.events.schedule.DistributionRequestedEvent;
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

class DistributionEngineServiceTest {

    @Test
    void processCreatesJobAndDispatchesMatchingProfile() {
        DistributionJobRepository repository = mock(DistributionJobRepository.class);
        TenantDistributionProfileClient profileClient = mock(TenantDistributionProfileClient.class);
        TenantCommunicationProfileClient communicationProfileClient = mock(TenantCommunicationProfileClient.class);
        OutboundScheduleMessageClient outboundMessageClient = mock(OutboundScheduleMessageClient.class);
        DistributionAdapter adapter = mock(DistributionAdapter.class);
        when(adapter.supports()).thenReturn(CommunicationProtocol.MQ);

        DistributionEngineService service = new DistributionEngineService(
                repository,
                profileClient,
                communicationProfileClient,
                outboundMessageClient,
                List.of(adapter)
        );

        UUID requestId = UUID.randomUUID();
        UUID outboundMessageId = UUID.randomUUID();
        UUID correlationId = UUID.randomUUID();
        DistributionRequestedEvent event = DistributionRequestedEvent.builder()
                .correlationId(correlationId)
                .causationId(outboundMessageId)
                .distributionRequestId(requestId)
                .outboundMessageId(outboundMessageId)
                .changeSetId(UUID.randomUUID())
                .changeRequestId(101L)
                .importedScheduleId(UUID.randomUUID())
                .importBatchId(UUID.randomUUID())
                .messageType(MessageType.SSM)
                .airlineCode("QR")
                .partnerCode("BA")
                .requestedAt(Instant.now())
                .build();

        CodesharePartnerDistributionProfileDTO profile = new CodesharePartnerDistributionProfileDTO();
        profile.setProfileCode("BA-SSM-MQ");
        profile.setDistributionChannel(CommunicationProtocol.MQ);
        profile.setMessageType(MessageType.SSM);
        CodesharePartnerCommunicationProfileDTO communicationProfile = new CodesharePartnerCommunicationProfileDTO();
        communicationProfile.setProtocol(CommunicationProtocol.MQ);
        communicationProfile.setEndpointUrl("partner.ssm.queue");

        when(repository.findByDistributionRequestId(requestId)).thenReturn(Optional.empty());
        when(repository.save(any(DistributionJobEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        OutboundScheduleMessageDTO outboundMessage = OutboundScheduleMessageDTO.builder()
                .outboundMessageId(outboundMessageId)
                .messageType(MessageType.SSM)
                .airlineCode("QR")
                .partnerCode("BA")
                .payload("SSM\nQR0701")
                .build();
        when(outboundMessageClient.getOutboundMessage(outboundMessageId)).thenReturn(outboundMessage);
        when(profileClient.resolve("QR", "BA", MessageType.SSM)).thenReturn(List.of(profile));
        when(communicationProfileClient.resolve("QR", "BA", CommunicationProtocol.MQ)).thenReturn(List.of(communicationProfile));

        service.process(event);

        verify(adapter).dispatch(event, profile, communicationProfile, outboundMessage);

        ArgumentCaptor<DistributionJobEntity> jobCaptor = ArgumentCaptor.forClass(DistributionJobEntity.class);
        verify(repository, org.mockito.Mockito.atLeastOnce()).save(jobCaptor.capture());
        DistributionJobEntity finalJob = jobCaptor.getAllValues().getLast();
        assertThat(finalJob.getStatus()).isEqualTo(DistributionJobStatus.DISPATCHED);
        assertThat(finalJob.getDispatchCount()).isEqualTo(1);
    }
}
