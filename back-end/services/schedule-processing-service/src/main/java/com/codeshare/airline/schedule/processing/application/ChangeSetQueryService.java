package com.codeshare.airline.schedule.processing.application;

import com.codeshare.airline.platform.core.dto.schedule.workflow.ChangeSetDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ScheduleCodeshareChangeDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ScheduleCodeshareSnapshotDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ScheduleDataElementChangeDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ScheduleDataElementSnapshotDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ScheduleFlightChangeDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ScheduleLegChangeDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ScheduleLegSnapshotDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ScheduleSegmentChangeDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ScheduleSegmentSnapshotDTO;
import com.codeshare.airline.platform.core.enums.schedule.ScheduleCodeshareChangeType;
import com.codeshare.airline.platform.core.enums.schedule.ScheduleDataElementChangeType;
import com.codeshare.airline.platform.core.enums.schedule.ScheduleLegChangeType;
import com.codeshare.airline.platform.core.enums.schedule.ScheduleSegmentChangeType;
import com.codeshare.airline.schedule.processing.domain.entity.ScheduleCodeshareChangeEntity;
import com.codeshare.airline.schedule.processing.domain.entity.ChangeSetEntity;
import com.codeshare.airline.schedule.processing.domain.entity.ScheduleDeiChangeEntity;
import com.codeshare.airline.schedule.processing.domain.entity.ScheduleFlightChangeEntity;
import com.codeshare.airline.schedule.processing.domain.entity.ScheduleLegChangeEntity;
import com.codeshare.airline.schedule.processing.domain.entity.ScheduleSegmentChangeEntity;
import com.codeshare.airline.schedule.processing.domain.repository.ChangeSetRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@Transactional
public class ChangeSetQueryService {

    private final ChangeSetRepository changeSetRepository;
    private final ObjectMapper objectMapper;

    public ChangeSetQueryService(
            ChangeSetRepository changeSetRepository,
            ObjectMapper objectMapper
    ) {
        this.changeSetRepository = changeSetRepository;
        this.objectMapper = objectMapper;
    }

    public ChangeSetDTO getChangeSet(UUID changeSetId) {
        ChangeSetEntity run = changeSetRepository.findById(changeSetId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Change set not found: " + changeSetId));

        List<ScheduleFlightChangeDTO> flightChanges = run.getFlightChanges().stream()
                .map(this::toFlightChange)
                .toList();

        return ChangeSetDTO.builder()
                .changeSetId(run.getId())
                .importedScheduleId(run.getImportedScheduleId())
                .importBatchId(run.getImportBatchId())
                .messageType(run.getSourceType())
                .airlineCode(run.getAirlineCode())
                .messageReference(run.getMessageReference())
                .sourceName(run.getSourceFileName())
                .createdAt(run.getCompletedAt())
                .status(run.getStatus().name())
                .flightChanges(flightChanges)
                .build();
    }

    private ScheduleFlightChangeDTO toFlightChange(ScheduleFlightChangeEntity entity) {
        return ScheduleFlightChangeDTO.builder()
                .flightChangeId(entity.getId())
                .airlineCode(entity.getAirlineCode())
                .flightNumber(entity.getFlightNumber())
                .operationalSuffix(entity.getOperationalSuffix())
                .itineraryVariationId(entity.getItineraryVariationId())
                .legChanges(entity.getLegChanges().stream().map(this::toLegChange).toList())
                .build();
    }

    private ScheduleLegChangeDTO toLegChange(ScheduleLegChangeEntity entity) {
        return ScheduleLegChangeDTO.builder()
                .legChangeId(entity.getId())
                .liveLegId(entity.getLiveLegId())
                .changeType(ScheduleLegChangeType.valueOf(entity.getChangeType().name()))
                .oldValue(readJson(entity.getLiveSnapshot(), ScheduleLegSnapshotDTO.class))
                .newValue(readJson(entity.getIngestedSnapshot(), ScheduleLegSnapshotDTO.class))
                .segmentChanges(entity.getSegmentChanges().stream().map(this::toSegmentChange).toList())
                .legDataElementChanges(entity.getLegDeiChanges().stream().map(this::toDataElementChange).toList())
                .codeshareChanges(entity.getCodeshareChanges().stream().map(this::toCodeshareChange).toList())
                .build();
    }

    private ScheduleSegmentChangeDTO toSegmentChange(ScheduleSegmentChangeEntity entity) {
        ScheduleSegmentSnapshotDTO oldValue = null;
        ScheduleSegmentSnapshotDTO newValue = null;

        if (entity.getSegmentChangeType() != com.codeshare.airline.schedule.processing.domain.enums.SegmentChangeType.ADDED) {
            oldValue = ScheduleSegmentSnapshotDTO.builder()
                    .segmentId(entity.getLiveSegmentId())
                    .boardPoint(entity.getBoardPoint())
                    .offPoint(entity.getOffPoint())
                    .dataElements(projectDataElements(entity.getDeiChanges(), true))
                    .build();
        }
        if (entity.getSegmentChangeType() != com.codeshare.airline.schedule.processing.domain.enums.SegmentChangeType.REMOVED) {
            newValue = ScheduleSegmentSnapshotDTO.builder()
                    .segmentId(entity.getImportedSegmentId())
                    .boardPoint(entity.getBoardPoint())
                    .offPoint(entity.getOffPoint())
                    .dataElements(projectDataElements(entity.getDeiChanges(), false))
                    .build();
        }

        return ScheduleSegmentChangeDTO.builder()
                .segmentChangeId(entity.getId())
                .liveSegmentId(entity.getLiveSegmentId())
                .changeType(ScheduleSegmentChangeType.valueOf(entity.getSegmentChangeType().name()))
                .oldValue(oldValue)
                .newValue(newValue)
                .dataElementChanges(entity.getDeiChanges().stream().map(this::toDataElementChange).toList())
                .build();
    }

    private ScheduleDataElementChangeDTO toDataElementChange(ScheduleDeiChangeEntity entity) {
        return ScheduleDataElementChangeDTO.builder()
                .changeId(entity.getId())
                .liveDataElementId(entity.getLiveDeiId())
                .changeType(ScheduleDataElementChangeType.valueOf(entity.getDeiChangeType().name()))
                .scope(entity.getDeiScope().name())
                .code(entity.getDeiCode())
                .sequenceOrder(entity.getSequenceOrder())
                .oldValue(entity.getLiveValue())
                .newValue(entity.getIngestedValue())
                .build();
    }

    private ScheduleCodeshareChangeDTO toCodeshareChange(ScheduleCodeshareChangeEntity entity) {
        return ScheduleCodeshareChangeDTO.builder()
                .changeId(entity.getId())
                .liveCodeshareId(entity.getLiveCodeshareId())
                .changeType(ScheduleCodeshareChangeType.valueOf(entity.getChangeType().name()))
                .oldValue(readJson(entity.getLiveSnapshot(), ScheduleCodeshareSnapshotDTO.class))
                .newValue(readJson(entity.getIngestedSnapshot(), ScheduleCodeshareSnapshotDTO.class))
                .build();
    }

    private List<ScheduleDataElementSnapshotDTO> projectDataElements(List<ScheduleDeiChangeEntity> changes, boolean oldValue) {
        List<ScheduleDataElementSnapshotDTO> snapshots = new ArrayList<>();
        for (ScheduleDeiChangeEntity change : changes) {
            String value = oldValue ? change.getLiveValue() : change.getIngestedValue();
            UUID id = oldValue ? change.getLiveDeiId() : change.getImportedDataElementId();
            if (value == null && id == null) {
                continue;
            }
            snapshots.add(ScheduleDataElementSnapshotDTO.builder()
                    .dataElementId(id)
                    .code(change.getDeiCode())
                    .value(value)
                    .scope(change.getDeiScope().name())
                    .sequenceOrder(change.getSequenceOrder())
                    .build());
        }
        return snapshots;
    }

    private <T> T readJson(String json, Class<T> type) {
        if (json == null || json.isBlank()) {
            return null;
        }

        try {
            return objectMapper.readValue(json, type);
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Unable to deserialize schedule snapshot", ex);
        }
    }
}


