package com.codeshare.airline.schedule.ingestion.source.initializer;

import com.codeshare.airline.core.enums.schedule.MessageType;
import com.codeshare.airline.schedule.ingestion.domain.enums.SourceType;
import com.codeshare.airline.schedule.ingestion.persistence.entities.source.ScheduleIngestionChannelEntity;
import com.codeshare.airline.schedule.ingestion.persistence.entities.source.ScheduleIngestionProfileEntity;
import com.codeshare.airline.schedule.ingestion.persistence.repositories.source.ScheduleIngestionProfileRepository;
import com.codeshare.airline.schedule.ingestion.source.security.DefaultScheduleCredentialResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;

@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultIngestionDataInitializer {

    private final ScheduleIngestionProfileRepository profileRepository;
    private final DefaultScheduleCredentialResolver credentialResolver;

    @Value("${ingestion.local-root}")
    private String localRoot;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    @Order(1)
    public void loadDefaults() {

        log.info(" Initializing default ingestion profiles...");

        initializeAirline("QR");
        initializeAirline("6E");

        log.info(" Default ingestion profiles initialized");
    }

    /* ====================================================== */

    private void initializeAirline(String airlineCode) {

        if (profileRepository.findByAirlineCode(airlineCode).isPresent()) {
            log.info("ℹ️ Airline {} already exists. Skipping.", airlineCode);
            return;
        }

        ScheduleIngestionProfileEntity profile = createBaseProfile(airlineCode);

        addMqChannels(profile, airlineCode);
        addSftpChannel(profile, airlineCode);
        addEmailChannel(profile, airlineCode);
        addLocalChannels(profile, airlineCode);

        profileRepository.save(profile);

        log.info(" Created ingestion profile for {}", airlineCode);
    }

    private ScheduleIngestionProfileEntity createBaseProfile(String airlineCode) {

        ScheduleIngestionProfileEntity profile = new ScheduleIngestionProfileEntity();
        profile.setAirlineCode(airlineCode);
        profile.setSourceSystem("DEFAULT_SYSTEM");
        profile.setEnabled(true);
        profile.setPollIntervalMs(60000L);

        return profile;
    }

    /* ======================================================
       MQ
       ====================================================== */

    private void addMqChannels(ScheduleIngestionProfileEntity profile, String airline) {

        profile.addChannel(createMqChannel(airline, MessageType.ASM));
        profile.addChannel(createMqChannel(airline, MessageType.SSM));
    }

    private ScheduleIngestionChannelEntity createMqChannel(String airline, MessageType messageType) {

        ScheduleIngestionChannelEntity ch = new ScheduleIngestionChannelEntity();

        ch.setSourceType(SourceType.MQ);
        ch.setMessageType(messageType);

        ch.setBrokerUrl("tcp://localhost:61616");
        ch.setQueueName(airline + "." + messageType + ".IN");

        // 🔥 Remove username/password from URI usage (use Spring config)
        ch.setConcurrentConsumers(2);
        ch.setMaxConcurrentConsumers(5);
        ch.setAsyncConsumer(true);
        ch.setReceiveTimeoutMs(1000);
        ch.setMaxMessagesPerTask(10);

        ch.setEnabled(false);

        return ch;
    }

    /* ======================================================
       SFTP
       ====================================================== */

    private void addSftpChannel(ScheduleIngestionProfileEntity profile, String airline) {
        profile.addChannel(createSftpChannel(airline));
    }

    private ScheduleIngestionChannelEntity createSftpChannel(String airline) {

        ScheduleIngestionChannelEntity ch = new ScheduleIngestionChannelEntity();

        ch.setSourceType(SourceType.SFTP);
        ch.setMessageType(MessageType.SSIM);

        ch.setHost("localhost");
        ch.setPort(22);
        ch.setUsername("admin");
        ch.setPasswordEncrypted(encrypt("admin"));

        ch.setRemoteDirectory("/inbound/" + airline);

        // 🔥 FILE behavior
        ch.setFilePollDelayMs(60000);
        ch.setFileReadLock("changed");
        ch.setFileReadLockMinAge("1s");
        ch.setFileReadLockTimeout(60000);
        ch.setFileReadLockCheckInterval(10000);

        ch.setFileIdempotent(true);
        ch.setFileIdempotentKey("${file:absolute.path}-${file:modified}");

        ch.setFilePreMove(".inprogress/${file:name}");
        ch.setFileMove(".processed/${file:name}");
        ch.setFileMoveFailed(".error/${file:name}");

        ch.setMaxMessagesPerPoll(5);

        // 🔥 SFTP behavior
        ch.setDisconnect(false);
        ch.setBinary(true);
        ch.setPassiveMode(true);
        ch.setReconnectDelayMs(5000);
        ch.setMaximumReconnectAttempts(3);

        ch.setEnabled(false);

        return ch;
    }

    /* ======================================================
       EMAIL
       ====================================================== */

    private void addEmailChannel(ScheduleIngestionProfileEntity profile, String airline) {
        profile.addChannel(createEmailChannel(airline));
    }

    private ScheduleIngestionChannelEntity createEmailChannel(String airline) {

        ScheduleIngestionChannelEntity ch = new ScheduleIngestionChannelEntity();

        ch.setSourceType(SourceType.EMAIL);
        ch.setMessageType(MessageType.SSM);

        ch.setProtocol("imaps");
        ch.setHost("imap.mailtrap.io");
        ch.setMailbox("INBOX/" + airline);

        ch.setUsername("mailtrap_username");
        ch.setPasswordEncrypted(encrypt("mailtrap_password"));

        ch.setMailDelayMs(60000);
        ch.setMailUnseenOnly(true);
        ch.setMailDelete(false);
        ch.setMailPeek(false);
        ch.setMailMoveTo("Processed");

        ch.setEnabled(false);

        return ch;
    }

    /* ======================================================
       LOCAL FILE
       ====================================================== */

    private void addLocalChannels(ScheduleIngestionProfileEntity profile, String airline) {

        profile.addChannel(createLocalChannel(MessageType.SSM, buildPath(airline, "SSM")));
        profile.addChannel(createLocalChannel(MessageType.ASM, buildPath(airline, "ASM")));
        profile.addChannel(createLocalChannel(MessageType.SSIM, buildPath(airline, "SSIM")));
    }

    private ScheduleIngestionChannelEntity createLocalChannel(MessageType scheduleType, String directory) {

        createDirectory(directory);

        ScheduleIngestionChannelEntity ch = new ScheduleIngestionChannelEntity();

        ch.setSourceType(SourceType.LOCAL);
        ch.setMessageType(scheduleType);

        ch.setRemoteDirectory(directory);

        ch.setFileNoop(false);
        ch.setFileIncludePattern("(?i).*\\.(ssm|asm|ssim|txt|text)");
        ch.setFileExcludePattern(null);

        ch.setFileReadLock("changed");
        ch.setFileReadLockMinAge("5s");
        ch.setFileReadLockTimeout(60000);
        ch.setFileReadLockCheckInterval(10000);

        ch.setFilePollDelayMs(60000);
        ch.setFileInitialDelayMs(30000);

        ch.setFileMove(".processed/${date:now:yyyyMMdd}/${file:name}");
        ch.setFileMoveFailed(".error/${date:now:yyyyMMdd}/${file:name}");

        ch.setFileIdempotent(true);
        ch.setFileIdempotentKey("${file:absolute.path}-${file:modified}");

        ch.setFileCharset("UTF-8");

        ch.setMaxMessagesPerPoll(10);
        ch.setRecursive(false);
        ch.setBridgeErrorHandler(true);

        ch.setEnabled(true);

        return ch;
    }

    /* ====================================================== */

    private String buildPath(String airline, String type) {
        return localRoot + "/" + airline + "/" + type;
    }


    private void createDirectory(String path) {
        File dir = new File(path);
        if (!dir.exists() && !dir.mkdirs()) {
            log.error(" Failed to create directory {}", path);
        }
    }

    private String encrypt(String value) {
        return credentialResolver.encrypt(value);
    }
}