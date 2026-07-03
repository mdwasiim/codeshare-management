package com.codeshare.airline.schedule.ingestion.persistence.mappers.source;


import com.codeshare.airline.schedule.ingestion.dto.source.AirlineIngestionChannelDTO;
import com.codeshare.airline.schedule.ingestion.dto.source.AirlineIngestionProfileDTO;
import com.codeshare.airline.schedule.ingestion.persistence.entities.source.ScheduleIngestionChannelEntity;
import com.codeshare.airline.schedule.ingestion.persistence.entities.source.ScheduleIngestionProfileEntity;

public class AirlineIngestionMapper {

    private AirlineIngestionMapper() {}

    /* ======================================================
       PROFILE
       ====================================================== */

    public static ScheduleIngestionProfileEntity toEntity(AirlineIngestionProfileDTO dto) {

        if (dto == null) return null;

        ScheduleIngestionProfileEntity profile = new ScheduleIngestionProfileEntity();

        profile.setAirlineCode(dto.getAirlineCode());
        profile.setSourceSystem(dto.getSourceSystem());
        profile.setEnabled(dto.getEnabled());
        profile.setPollIntervalMs(dto.getPollIntervalMs());

        if (dto.getChannels() != null) {
            dto.getChannels().forEach(channelDTO -> {
                ScheduleIngestionChannelEntity channel = toChannelEntity(channelDTO);
                profile.addChannel(channel);
            });
        }

        return profile;
    }

    /* ======================================================
       CHANNEL
       ====================================================== */

    public static ScheduleIngestionChannelEntity toChannelEntity(AirlineIngestionChannelDTO dto) {

        if (dto == null) return null;

        ScheduleIngestionChannelEntity ch = new ScheduleIngestionChannelEntity();

        /* =========================
           CORE
           ========================= */
        ch.setMessageType(dto.getMessageType());
        ch.setSourceType(dto.getSourceType());

        /* =========================
           CONNECTION
           ========================= */
        ch.setHost(dto.getHost());
        ch.setPort(dto.getPort());
        ch.setUsername(dto.getUsername());
        ch.setPasswordEncrypted(dto.getPasswordEncrypted());

        /* =========================
           PATH / DIRECTORY
           ========================= */
        ch.setRemoteDirectory(dto.getRemoteDirectory());

        /* =========================
           EMAIL
           ========================= */
        ch.setProtocol(dto.getProtocol());
        ch.setMailbox(dto.getMailbox());
        ch.setMailDelayMs(dto.getMailDelayMs());
        ch.setMailUnseenOnly(dto.getMailUnseenOnly());
        ch.setMailDelete(dto.getMailDelete());
        ch.setMailPeek(dto.getMailPeek());
        ch.setMailMoveTo(dto.getMailMoveTo());

        /* =========================
           MQ
           ========================= */
        ch.setBrokerUrl(dto.getBrokerUrl());
        ch.setQueueName(dto.getQueueName());
        ch.setTopicName(dto.getTopicName());
        ch.setConcurrentConsumers(dto.getConcurrentConsumers());
        ch.setMaxConcurrentConsumers(dto.getMaxConcurrentConsumers());
        ch.setAsyncConsumer(dto.getAsyncConsumer());
        ch.setReceiveTimeoutMs(dto.getReceiveTimeoutMs());
        ch.setMaxMessagesPerTask(dto.getMaxMessagesPerTask());

        /* =========================
           FILE / SFTP COMMON
           ========================= */
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

        /* =========================
           SFTP ADVANCED
           ========================= */
        ch.setDisconnect(dto.getDisconnect());
        ch.setBinary(dto.getBinary());
        ch.setPassiveMode(dto.getPassiveMode());
        ch.setReconnectDelayMs(dto.getReconnectDelayMs());
        ch.setMaximumReconnectAttempts(dto.getMaximumReconnectAttempts());
        ch.setSoTimeoutMs(dto.getSoTimeoutMs());

        /* =========================
           RETRY / CONTROL
           ========================= */
        ch.setRetryAttempts(dto.getRetryAttempts());
        ch.setRetryDelayMs(dto.getRetryDelayMs());
        ch.setEnabled(dto.getEnabled());
        ch.setPriority(dto.getPriority());

        return ch;
    }
}