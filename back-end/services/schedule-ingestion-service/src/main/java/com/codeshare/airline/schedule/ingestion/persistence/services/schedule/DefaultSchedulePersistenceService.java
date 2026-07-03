package com.codeshare.airline.schedule.ingestion.persistence.services.schedule;

import com.codeshare.airline.schedule.ingestion.dto.schedule.ScheduleFileMetaDataDTO;
import com.codeshare.airline.schedule.ingestion.dto.schedule.ScheduleMessageDTO;
import com.codeshare.airline.schedule.ingestion.persistence.entities.schedule.ScheduleFileMetaDataEntity;
import com.codeshare.airline.schedule.ingestion.persistence.mappers.schedule.ScheduleAggregateMapper;
import com.codeshare.airline.schedule.ingestion.persistence.repositories.schedule.ScheduleFileMetaDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class DefaultSchedulePersistenceService implements SchedulePersistenceService {

    private final ScheduleFileMetaDataRepository scheduleFileRepository;
    private final ScheduleAggregateMapper aggregateMapper;

    @Override
    public void save(ScheduleMessageDTO scheduleMessage,
                     ScheduleFileMetaDataDTO scheduleFileMetaDataDTO) {

        if (scheduleMessage == null) {
            throw new IllegalArgumentException("Message cannot be null");
        }

        if (scheduleFileMetaDataDTO == null) {
            throw new IllegalArgumentException("File metadata cannot be null");
        }

        log.info("Persisting message type={} for fileId={}",
                scheduleMessage.getMessageType(),
                scheduleFileMetaDataDTO.getFileId());

        /* ================= FETCH FILE ================= */

        ScheduleFileMetaDataEntity scheduleFileMetaData =
                scheduleFileRepository.findByFileId(scheduleFileMetaDataDTO.getFileId())
                        .orElseThrow(() -> new IllegalStateException(
                                "File not found for fileId=" + scheduleFileMetaDataDTO.getFileId()
                        ));

        /* ================= MAP ================= */

        aggregateMapper.toEntity(scheduleFileMetaData, scheduleMessage); // 🔥 attaches to file

        /* ================= SAVE ROOT ================= */

        scheduleFileRepository.save(scheduleFileMetaData); // 🔥 correct root save
    }
}