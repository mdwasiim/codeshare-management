package com.codeshare.airline.schedule.ingestion.persistence.services.schedule;

import com.codeshare.airline.schedule.ingestion.persistence.entities.schedule.ScheduleFileMetaDataEntity;
import com.codeshare.airline.schedule.ingestion.persistence.mappers.schedule.ScheduleFileMetaDataMapper;
import com.codeshare.airline.schedule.ingestion.persistence.mappers.schedule.ScheduleFlightMapper;
import com.codeshare.airline.schedule.ingestion.persistence.mappers.schedule.ScheduleMessageMapper;
import com.codeshare.airline.schedule.ingestion.persistence.repositories.schedule.ScheduleFileMetaDataRepository;
import com.codeshare.airline.schedule.ingestion.persistence.repositories.schedule.ScheduleFlightRepository;
import com.codeshare.airline.schedule.ingestion.persistence.repositories.schedule.ScheduleMessageRepository;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ScheduleQueryServiceTest {

    @Test
    void findFileByImportBatchIdRejectsAmbiguousLoadId() {
        ScheduleFileMetaDataRepository fileRepository = mock(ScheduleFileMetaDataRepository.class);
        ScheduleFlightRepository flightRepository = mock(ScheduleFlightRepository.class);
        ScheduleMessageRepository messageRepository = mock(ScheduleMessageRepository.class);
        ScheduleFileMetaDataMapper fileMapper = mock(ScheduleFileMetaDataMapper.class);
        ScheduleMessageMapper messageMapper = mock(ScheduleMessageMapper.class);
        ScheduleFlightMapper flightMapper = mock(ScheduleFlightMapper.class);

        ScheduleQueryService service = new ScheduleQueryService(
                fileRepository,
                flightRepository,
                messageRepository,
                fileMapper,
                messageMapper,
                flightMapper
        );

        UUID loadId = UUID.randomUUID();
        when(fileRepository.findAllByLoadId(loadId))
                .thenReturn(List.of(new ScheduleFileMetaDataEntity(), new ScheduleFileMetaDataEntity()));

        assertThatThrownBy(() -> service.findFileByImportBatchId(loadId))
                .isInstanceOf(ResponseStatusException.class)
                .satisfies(ex -> {
                    ResponseStatusException response = (ResponseStatusException) ex;
                    assertThat(response.getStatusCode().value()).isEqualTo(409);
                });
    }
}
