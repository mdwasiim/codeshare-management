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
import com.codeshare.airline.platform.core.enums.master.codesharepartner.CommunicationProtocol;
import com.codeshare.airline.platform.core.dto.schedule.workflow.OutboundScheduleMessageDTO;
import com.codeshare.airline.platform.core.events.schedule.DistributionRequestedEvent;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class DistributionEngineService {

    private final DistributionJobRepository repository;
    private final TenantDistributionProfileClient tenantDistributionProfileClient;
    private final TenantCommunicationProfileClient tenantCommunicationProfileClient;
    private final OutboundScheduleMessageClient outboundScheduleMessageClient;
    private final Map<com.codeshare.airline.platform.core.enums.master.codesharepartner.CommunicationProtocol, DistributionAdapter> adapters;

    public DistributionEngineService(
            DistributionJobRepository repository,
            TenantDistributionProfileClient tenantDistributionProfileClient,
            TenantCommunicationProfileClient tenantCommunicationProfileClient,
            OutboundScheduleMessageClient outboundScheduleMessageClient,
            List<DistributionAdapter> adapters
    ) {
        this.repository = repository;
        this.tenantDistributionProfileClient = tenantDistributionProfileClient;
        this.tenantCommunicationProfileClient = tenantCommunicationProfileClient;
        this.outboundScheduleMessageClient = outboundScheduleMessageClient;
        this.adapters = adapters.stream().collect(Collectors.toMap(DistributionAdapter::supports, Function.identity()));
    }

    public void process(DistributionRequestedEvent event) {
        DistributionJobEntity job = repository.findByDistributionRequestId(event.getDistributionRequestId())
                .orElseGet(() -> createJob(event));

        if (job.getStatus() == DistributionJobStatus.DISPATCHED || job.getStatus() == DistributionJobStatus.NO_RULES) {
            return;
        }

        try {
            job.setStatus(DistributionJobStatus.DISPATCHING);
            job.setStartedAt(job.getStartedAt() != null ? job.getStartedAt() : Instant.now());
            repository.save(job);

            OutboundScheduleMessageDTO outboundMessage = outboundScheduleMessageClient.getOutboundMessage(event.getOutboundMessageId());
            List<CodesharePartnerDistributionProfileDTO> profiles = tenantDistributionProfileClient.resolve(
                    event.getAirlineCode(),
                    event.getPartnerCode(),
                    event.getMessageType()
            );

            if (profiles == null || profiles.isEmpty()) {
                job.setStatus(DistributionJobStatus.NO_RULES);
                job.setCompletedAt(Instant.now());
                job.setDispatchCount(0);
                repository.save(job);
                return;
            }

            int dispatchCount = 0;
            for (CodesharePartnerDistributionProfileDTO profile : profiles) {
                DistributionAdapter adapter = resolveAdapter(profile.getDistributionChannel());
                if (adapter == null) {
                    throw new IllegalStateException("No distribution adapter for channel=" + profile.getDistributionChannel());
                }
                CodesharePartnerCommunicationProfileDTO communicationProfile = resolveCommunicationProfile(event, profile);
                adapter.dispatch(event, profile, communicationProfile, outboundMessage);
                dispatchCount++;
            }

            job.setStatus(DistributionJobStatus.DISPATCHED);
            job.setDispatchCount(dispatchCount);
            job.setCompletedAt(Instant.now());
            repository.save(job);
        } catch (Exception ex) {
            job.setStatus(DistributionJobStatus.FAILED);
            job.setErrorMessage(ex.getMessage());
            job.setCompletedAt(Instant.now());
            repository.save(job);
            throw ex;
        }
    }

    private DistributionJobEntity createJob(DistributionRequestedEvent event) {
        DistributionJobEntity job = DistributionJobEntity.builder()
                .distributionRequestId(event.getDistributionRequestId())
                .outboundMessageId(event.getOutboundMessageId())
                .changeSetId(event.getChangeSetId())
                .changeRequestId(event.getChangeRequestId())
                .importedScheduleId(event.getImportedScheduleId())
                .importBatchId(event.getImportBatchId())
                .messageType(event.getMessageType())
                .airlineCode(event.getAirlineCode())
                .partnerCode(event.getPartnerCode())
                .requestedAt(event.getRequestedAt() != null ? event.getRequestedAt() : Instant.now())
                .dispatchCount(0)
                .status(DistributionJobStatus.REQUESTED)
                .build();
        return repository.save(job);
    }

    private DistributionAdapter resolveAdapter(CommunicationProtocol channel) {
        DistributionAdapter adapter = adapters.get(channel);
        if (adapter == null && channel == CommunicationProtocol.REST) {
            return adapters.get(CommunicationProtocol.API);
        }
        return adapter;
    }

    private CodesharePartnerCommunicationProfileDTO resolveCommunicationProfile(
            DistributionRequestedEvent event,
            CodesharePartnerDistributionProfileDTO profile
    ) {
        CommunicationProtocol communicationProtocol = profile.getDistributionChannel() == CommunicationProtocol.REST
                ? CommunicationProtocol.API
                : profile.getDistributionChannel();
        List<CodesharePartnerCommunicationProfileDTO> communicationProfiles = tenantCommunicationProfileClient.resolve(
                event.getAirlineCode(),
                event.getPartnerCode(),
                communicationProtocol
        );
        if (communicationProfiles == null || communicationProfiles.isEmpty()) {
            throw new IllegalStateException("No communication profile for channel=" + profile.getDistributionChannel());
        }
        return communicationProfiles.getFirst();
    }
}
