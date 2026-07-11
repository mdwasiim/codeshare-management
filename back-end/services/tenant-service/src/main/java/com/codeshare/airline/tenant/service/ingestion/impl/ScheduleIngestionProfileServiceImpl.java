package com.codeshare.airline.tenant.service.ingestion.impl;

import com.codeshare.airline.platform.core.dto.tenant.TenantIngestionChannelDTO;
import com.codeshare.airline.platform.core.dto.tenant.TenantIngestionProfileDTO;
import com.codeshare.airline.tenant.entities.Tenant;
import com.codeshare.airline.tenant.entities.ingestion.ScheduleIngestionChannelEntity;
import com.codeshare.airline.tenant.entities.ingestion.ScheduleIngestionProfileEntity;
import com.codeshare.airline.tenant.repository.TenantRepository;
import com.codeshare.airline.tenant.repository.ingestion.ScheduleIngestionProfileRepository;
import com.codeshare.airline.tenant.service.ingestion.ScheduleIngestionProfileService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ScheduleIngestionProfileServiceImpl implements ScheduleIngestionProfileService {

    private final ScheduleIngestionProfileRepository repository;
    private final TenantRepository tenantRepository;

    @Override
    public TenantIngestionProfileDTO create(TenantIngestionProfileDTO dto) {
        validateBusinessRules(dto);
        ScheduleIngestionProfileEntity entity = toEntity(dto, new ScheduleIngestionProfileEntity());
        return toDto(repository.save(entity));
    }

    @Override
    public TenantIngestionProfileDTO update(UUID id, TenantIngestionProfileDTO dto) {
        validateBusinessRules(dto);
        ScheduleIngestionProfileEntity existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ingestion profile not found"));
        existing.getChannels().clear();
        toEntity(dto, existing);
        return toDto(repository.save(existing));
    }

    @Override
    @Transactional(readOnly = true)
    public TenantIngestionProfileDTO getByTenantCode(String tenantCode) {
        return repository.findWithChannelsByTenantCode(tenantCode)
                .map(this::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Ingestion profile not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TenantIngestionProfileDTO> getAll() {
        return repository.findAllWithChannels().stream().map(this::toDto).toList();
    }

    @Override
    public void delete(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public void enable(UUID id, boolean enabled) {
        ScheduleIngestionProfileEntity existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ingestion profile not found"));
        existing.setEnabled(enabled);
    }

    private ScheduleIngestionProfileEntity toEntity(TenantIngestionProfileDTO dto, ScheduleIngestionProfileEntity entity) {
        Tenant tenant = tenantRepository.findById(dto.getTenantId())
                .orElseThrow(() -> new EntityNotFoundException("Tenant not found"));
        entity.setTenant(tenant);
        entity.setSourceSystem(dto.getSourceSystem());
        entity.setEnabled(dto.getEnabled());
        entity.setPollIntervalMs(dto.getPollIntervalMs());
        if (dto.getChannels() != null) {
            dto.getChannels().forEach(channelDto -> entity.addChannel(toChannelEntity(channelDto)));
        }
        return entity;
    }

    private ScheduleIngestionChannelEntity toChannelEntity(TenantIngestionChannelDTO dto) {
        ScheduleIngestionChannelEntity ch = new ScheduleIngestionChannelEntity();
        ch.setMessageType(dto.getMessageType());
        ch.setSourceType(dto.getSourceType());
        ch.setEnabled(dto.getEnabled());
        ch.setPriority(dto.getPriority());
        ch.setHost(dto.getHost());
        ch.setPort(dto.getPort());
        ch.setUsername(dto.getUsername());
        ch.setPasswordEncrypted(dto.getPasswordEncrypted());
        ch.setRemoteDirectory(dto.getRemoteDirectory());
        ch.setProtocol(dto.getProtocol());
        ch.setMailbox(dto.getMailbox());
        ch.setMailDelayMs(dto.getMailDelayMs());
        ch.setMailUnseenOnly(dto.getMailUnseenOnly());
        ch.setMailDelete(dto.getMailDelete());
        ch.setMailPeek(dto.getMailPeek());
        ch.setMailMoveTo(dto.getMailMoveTo());
        ch.setBrokerUrl(dto.getBrokerUrl());
        ch.setQueueName(dto.getQueueName());
        ch.setTopicName(dto.getTopicName());
        ch.setConcurrentConsumers(dto.getConcurrentConsumers());
        ch.setMaxConcurrentConsumers(dto.getMaxConcurrentConsumers());
        ch.setAsyncConsumer(dto.getAsyncConsumer());
        ch.setReceiveTimeoutMs(dto.getReceiveTimeoutMs());
        ch.setMaxMessagesPerTask(dto.getMaxMessagesPerTask());
        ch.setFileNoop(dto.getFileNoop());
        ch.setFileDelete(dto.getFileDelete());
        ch.setFileIncludePattern(dto.getFileIncludePattern());
        ch.setFileExcludePattern(dto.getFileExcludePattern());
        ch.setFileReadLock(dto.getFileReadLock());
        ch.setFileReadLockMinAge(dto.getFileReadLockMinAge());
        ch.setFileReadLockTimeout(dto.getFileReadLockTimeout());
        ch.setFileReadLockCheckInterval(dto.getFileReadLockCheckInterval());
        ch.setFilePollDelayMs(dto.getFilePollDelayMs());
        ch.setFileInitialDelayMs(dto.getFileInitialDelayMs());
        ch.setFileMove(dto.getFileMove());
        ch.setFileMoveFailed(dto.getFileMoveFailed());
        ch.setFilePreMove(dto.getFilePreMove());
        ch.setFileIdempotent(dto.getFileIdempotent());
        ch.setFileIdempotentKey(dto.getFileIdempotentKey());
        ch.setFileCharset(dto.getFileCharset());
        ch.setMaxMessagesPerPoll(dto.getMaxMessagesPerPoll());
        ch.setRecursive(dto.getRecursive());
        ch.setBridgeErrorHandler(dto.getBridgeErrorHandler());
        ch.setDisconnect(dto.getDisconnect());
        ch.setBinary(dto.getBinary());
        ch.setPassiveMode(dto.getPassiveMode());
        ch.setReconnectDelayMs(dto.getReconnectDelayMs());
        ch.setMaximumReconnectAttempts(dto.getMaximumReconnectAttempts());
        ch.setSoTimeoutMs(dto.getSoTimeoutMs());
        ch.setRetryAttempts(dto.getRetryAttempts());
        ch.setRetryDelayMs(dto.getRetryDelayMs());
        return ch;
    }

    private TenantIngestionProfileDTO toDto(ScheduleIngestionProfileEntity entity) {
        TenantIngestionProfileDTO dto = new TenantIngestionProfileDTO();
        dto.setId(entity.getId());
        dto.setTenantId(entity.getTenant().getId());
        dto.setTenantCode(entity.getTenant().getTenantCode());
        dto.setAirlineCode(entity.getTenant().getTenantCode());
        dto.setSourceSystem(entity.getSourceSystem());
        dto.setEnabled(entity.getEnabled());
        dto.setPollIntervalMs(entity.getPollIntervalMs());
        dto.setChannels(entity.getChannels().stream().map(this::toDto).toList());
        return dto;
    }

    private TenantIngestionChannelDTO toDto(ScheduleIngestionChannelEntity channel) {
        TenantIngestionChannelDTO dto = new TenantIngestionChannelDTO();
        dto.setId(channel.getId());
        dto.setMessageType(channel.getMessageType());
        dto.setSourceType(channel.getSourceType());
        dto.setEnabled(channel.getEnabled());
        dto.setPriority(channel.getPriority());
        dto.setHost(channel.getHost());
        dto.setPort(channel.getPort());
        dto.setUsername(channel.getUsername());
        dto.setPasswordEncrypted(channel.getPasswordEncrypted());
        dto.setRemoteDirectory(channel.getRemoteDirectory());
        dto.setProtocol(channel.getProtocol());
        dto.setMailbox(channel.getMailbox());
        dto.setMailDelayMs(channel.getMailDelayMs());
        dto.setMailUnseenOnly(channel.getMailUnseenOnly());
        dto.setMailDelete(channel.getMailDelete());
        dto.setMailPeek(channel.getMailPeek());
        dto.setMailMoveTo(channel.getMailMoveTo());
        dto.setBrokerUrl(channel.getBrokerUrl());
        dto.setQueueName(channel.getQueueName());
        dto.setTopicName(channel.getTopicName());
        dto.setConcurrentConsumers(channel.getConcurrentConsumers());
        dto.setMaxConcurrentConsumers(channel.getMaxConcurrentConsumers());
        dto.setAsyncConsumer(channel.getAsyncConsumer());
        dto.setReceiveTimeoutMs(channel.getReceiveTimeoutMs());
        dto.setMaxMessagesPerTask(channel.getMaxMessagesPerTask());
        dto.setFileNoop(channel.getFileNoop());
        dto.setFileDelete(channel.getFileDelete());
        dto.setFileIncludePattern(channel.getFileIncludePattern());
        dto.setFileExcludePattern(channel.getFileExcludePattern());
        dto.setFileReadLock(channel.getFileReadLock());
        dto.setFileReadLockMinAge(channel.getFileReadLockMinAge());
        dto.setFileReadLockTimeout(channel.getFileReadLockTimeout());
        dto.setFileReadLockCheckInterval(channel.getFileReadLockCheckInterval());
        dto.setFilePollDelayMs(channel.getFilePollDelayMs());
        dto.setFileInitialDelayMs(channel.getFileInitialDelayMs());
        dto.setFileMove(channel.getFileMove());
        dto.setFileMoveFailed(channel.getFileMoveFailed());
        dto.setFilePreMove(channel.getFilePreMove());
        dto.setFileIdempotent(channel.getFileIdempotent());
        dto.setFileIdempotentKey(channel.getFileIdempotentKey());
        dto.setFileCharset(channel.getFileCharset());
        dto.setMaxMessagesPerPoll(channel.getMaxMessagesPerPoll());
        dto.setRecursive(channel.getRecursive());
        dto.setBridgeErrorHandler(channel.getBridgeErrorHandler());
        dto.setDisconnect(channel.getDisconnect());
        dto.setBinary(channel.getBinary());
        dto.setPassiveMode(channel.getPassiveMode());
        dto.setReconnectDelayMs(channel.getReconnectDelayMs());
        dto.setMaximumReconnectAttempts(channel.getMaximumReconnectAttempts());
        dto.setSoTimeoutMs(channel.getSoTimeoutMs());
        dto.setRetryAttempts(channel.getRetryAttempts());
        dto.setRetryDelayMs(channel.getRetryDelayMs());
        return dto;
    }

    private void validateBusinessRules(TenantIngestionProfileDTO dto) {
        if (dto.getTenantId() == null) {
            throw new IllegalArgumentException("Tenant is required");
        }
        if (dto.getChannels() == null || dto.getChannels().isEmpty()) {
            throw new IllegalArgumentException("At least one channel required");
        }
        dto.getChannels().forEach(channel -> {
            switch (channel.getSourceType()) {
                case SFTP -> {
                    require(channel.getHost(), "SFTP host required");
                    require(channel.getPort(), "SFTP port required");
                }
                case MQ -> require(channel.getBrokerUrl(), "MQ brokerUrl required");
                case EMAIL -> require(channel.getProtocol(), "Email protocol required");
                default -> {
                }
            }
        });
    }

    private void require(Object value, String message) {
        if (value == null || (value instanceof String s && s.isBlank())) {
            throw new IllegalArgumentException(message);
        }
    }
}
