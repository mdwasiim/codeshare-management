package com.codeshare.airline.schedule.processing.application;

import com.codeshare.airline.platform.core.dto.schedule.workflow.ImportBatchDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ImportedScheduleDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ActiveScheduleDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ScheduleCodeshareSnapshotDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ScheduleDataElementSnapshotDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ScheduleFlightSnapshotDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ScheduleLegSnapshotDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ScheduleSegmentSnapshotDTO;
import com.codeshare.airline.platform.core.events.schedule.ChangeSetCreatedEvent;
import com.codeshare.airline.platform.core.events.schedule.ImportCompletedEvent;
import com.codeshare.airline.schedule.processing.domain.entity.ScheduleCodeshareChangeEntity;
import com.codeshare.airline.schedule.processing.domain.entity.ChangeSetEntity;
import com.codeshare.airline.schedule.processing.domain.entity.ScheduleDeiChangeEntity;
import com.codeshare.airline.schedule.processing.domain.entity.ScheduleFlightChangeEntity;
import com.codeshare.airline.schedule.processing.domain.entity.ScheduleLegChangeEntity;
import com.codeshare.airline.schedule.processing.domain.entity.ScheduleSegmentChangeEntity;
import com.codeshare.airline.schedule.processing.domain.enums.CodeshareChangeType;
import com.codeshare.airline.schedule.processing.domain.enums.ComparisonStatus;
import com.codeshare.airline.schedule.processing.domain.enums.DeiChangeType;
import com.codeshare.airline.schedule.processing.domain.enums.LegChangeType;
import com.codeshare.airline.schedule.processing.domain.enums.ChangeSetStatus;
import com.codeshare.airline.schedule.processing.domain.enums.SegmentChangeType;
import com.codeshare.airline.schedule.processing.domain.repository.ChangeSetRepository;
import com.codeshare.airline.schedule.processing.feign.ImportBatchClient;
import com.codeshare.airline.schedule.processing.feign.ActiveScheduleClient;
import com.codeshare.airline.schedule.processing.integration.kafka.ChangeSetCreatedEventPublisher;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional
public class ScheduleComparisonService {

    private final ImportBatchClient importBatchClient;
    private final ActiveScheduleClient activeScheduleClient;
    private final ChangeSetRepository changeSetRepository;
    private final ChangeSetCreatedEventPublisher changeSetCreatedEventPublisher;
    private final ObjectMapper objectMapper;

    public ScheduleComparisonService(
            ImportBatchClient importBatchClient,
            ActiveScheduleClient activeScheduleClient,
            ChangeSetRepository changeSetRepository,
            ChangeSetCreatedEventPublisher changeSetCreatedEventPublisher,
            ObjectMapper objectMapper
    ) {
        this.importBatchClient = importBatchClient;
        this.activeScheduleClient = activeScheduleClient;
        this.changeSetRepository = changeSetRepository;
        this.changeSetCreatedEventPublisher = changeSetCreatedEventPublisher;
        this.objectMapper = objectMapper;
    }

    public UUID compare(ImportCompletedEvent event) {
        return changeSetRepository.findByImportBatchId(event.getImportBatchId())
                .map(ChangeSetEntity::getChangeSetId)
                .orElseGet(() -> createChangeSet(event));
    }

    private UUID createChangeSet(ImportCompletedEvent event) {
        ChangeSetEntity run = ChangeSetEntity.builder()
                .changeSetId(UUID.randomUUID())
                .importedScheduleId(event.getImportedScheduleId())
                .importBatchId(event.getImportBatchId())
                .sourceType(event.getMessageType())
                .airlineCode(event.getAirlineCode())
                .sourceFileName(event.getSourceName())
                .sourceReceivedAt(event.getCompletedAt())
                .startedAt(Instant.now())
                .status(ComparisonStatus.IN_PROGRESS)
                .build();
        changeSetRepository.save(run);

        try {
            ImportBatchDTO importBatch = importBatchClient.getImportBatch(event.getImportBatchId());
            ImportedScheduleDTO importedSchedule = importBatch.getImportedSchedule();
            ActiveScheduleDTO activeSchedule = loadActiveSchedule(event.getAirlineCode());

            compareSchedules(run, importedSchedule, activeSchedule);
            run.setCompletedAt(Instant.now());
            run.setStatus(ComparisonStatus.COMPLETED);
            updateSummary(run);

            ChangeSetEntity saved = changeSetRepository.save(run);
            changeSetCreatedEventPublisher.publish(ChangeSetCreatedEvent.builder()
                    .changeSetId(saved.getChangeSetId())
                    .importedScheduleId(saved.getImportedScheduleId())
                    .importBatchId(saved.getImportBatchId())
                    .messageType(saved.getSourceType())
                    .airlineCode(saved.getAirlineCode())
                    .createdAt(saved.getCompletedAt())
                    .build());
            return saved.getChangeSetId();
        } catch (Exception ex) {
            run.setStatus(ComparisonStatus.FAILED);
            run.setCompletedAt(Instant.now());
            run.setErrorMessage(ex.getMessage());
            changeSetRepository.save(run);
            throw ex;
        }
    }

    private ActiveScheduleDTO loadActiveSchedule(String airlineCode) {
        try {
            return activeScheduleClient.getActiveSchedule(airlineCode);
        } catch (FeignException.NotFound ex) {
            return ActiveScheduleDTO.builder()
                    .airlineCode(airlineCode)
                    .asOf(Instant.now())
                    .build();
        }
    }

    private void compareSchedules(
            ChangeSetEntity run,
            ImportedScheduleDTO importedSchedule,
            ActiveScheduleDTO activeSchedule
    ) {
        Map<String, ScheduleFlightSnapshotDTO> importedFlights = indexFlights(importedSchedule.getFlights());
        Map<String, ScheduleFlightSnapshotDTO> activeFlights = indexFlights(activeSchedule.getFlights());
        Set<String> flightKeys = new LinkedHashSet<>();
        flightKeys.addAll(importedFlights.keySet());
        flightKeys.addAll(activeFlights.keySet());

        for (String flightKey : flightKeys) {
            ScheduleFlightSnapshotDTO importedFlight = importedFlights.get(flightKey);
            ScheduleFlightSnapshotDTO activeFlight = activeFlights.get(flightKey);

            ScheduleFlightChangeEntity flightChange = ScheduleFlightChangeEntity.builder()
                    .airlineCode(importedFlight != null ? importedFlight.getAirlineCode() : activeFlight.getAirlineCode())
                    .flightNumber(importedFlight != null ? importedFlight.getFlightNumber() : activeFlight.getFlightNumber())
                    .operationalSuffix(importedFlight != null ? importedFlight.getOperationalSuffix() : activeFlight.getOperationalSuffix())
                    .itineraryVariationId(importedFlight != null ? importedFlight.getItineraryVariationId() : activeFlight.getItineraryVariationId())
                    .changeSetStatus(ChangeSetStatus.PENDING)
                    .build();

            if (activeFlight == null) {
                importedFlight.getLegs().forEach(leg -> flightChange.addLegChange(buildNewLegChange(importedFlight, leg)));
            } else if (importedFlight == null) {
                activeFlight.getLegs().forEach(leg -> flightChange.addLegChange(buildCancelledLegChange(activeFlight, leg)));
            } else {
                compareFlightLegs(flightChange, importedFlight, activeFlight);
            }

            if (!flightChange.getLegChanges().isEmpty()) {
                run.addFlightChange(flightChange);
            }
        }
    }

    private void compareFlightLegs(
            ScheduleFlightChangeEntity flightChange,
            ScheduleFlightSnapshotDTO importedFlight,
            ScheduleFlightSnapshotDTO activeFlight
    ) {
        Map<String, ScheduleLegSnapshotDTO> importedLegs = indexLegs(importedFlight.getLegs());
        Map<String, ScheduleLegSnapshotDTO> activeLegs = indexLegs(activeFlight.getLegs());
        Set<String> legKeys = new LinkedHashSet<>();
        legKeys.addAll(importedLegs.keySet());
        legKeys.addAll(activeLegs.keySet());

        for (String legKey : legKeys) {
            ScheduleLegSnapshotDTO importedLeg = importedLegs.get(legKey);
            ScheduleLegSnapshotDTO activeLeg = activeLegs.get(legKey);

            if (activeLeg == null) {
                flightChange.addLegChange(buildNewLegChange(importedFlight, importedLeg));
                continue;
            }

            if (importedLeg == null) {
                flightChange.addLegChange(buildCancelledLegChange(activeFlight, activeLeg));
                continue;
            }

            ScheduleLegChangeEntity legChange = buildLegDelta(importedFlight, importedLeg, activeLeg);
            if (legChange != null) {
                flightChange.addLegChange(legChange);
            }
        }
    }

    private ScheduleLegChangeEntity buildNewLegChange(
            ScheduleFlightSnapshotDTO importedFlight,
            ScheduleLegSnapshotDTO importedLeg
    ) {
        ScheduleLegChangeEntity legChange = baseLegChange(importedFlight, importedLeg, null, LegChangeType.NEW);
        populateAddedSegments(legChange, importedLeg.getSegments());
        populateAddedCodeshares(legChange, importedLeg.getCodeshares());
        populateAddedLegDataElements(legChange, importedLeg.getLegDataElements());
        return legChange;
    }

    private ScheduleLegChangeEntity buildCancelledLegChange(
            ScheduleFlightSnapshotDTO activeFlight,
            ScheduleLegSnapshotDTO activeLeg
    ) {
        ScheduleLegChangeEntity legChange = baseLegChange(activeFlight, null, activeLeg, LegChangeType.CNL);
        populateRemovedSegments(legChange, activeLeg.getSegments());
        populateRemovedCodeshares(legChange, activeLeg.getCodeshares());
        populateRemovedLegDataElements(legChange, activeLeg.getLegDataElements());
        return legChange;
    }

    private ScheduleLegChangeEntity buildLegDelta(
            ScheduleFlightSnapshotDTO importedFlight,
            ScheduleLegSnapshotDTO importedLeg,
            ScheduleLegSnapshotDTO activeLeg
    ) {
        List<ScheduleSegmentChangeEntity> segmentChanges = compareSegments(importedLeg.getSegments(), activeLeg.getSegments());
        List<ScheduleDeiChangeEntity> legDataElementChanges = compareDataElements(
                importedLeg.getLegDataElements(),
                activeLeg.getLegDataElements(),
                true
        );
        List<ScheduleCodeshareChangeEntity> codeshareChanges = compareCodeshares(importedLeg.getCodeshares(), activeLeg.getCodeshares());

        LegChangeType changeType = determineLegChangeType(importedLeg, activeLeg, segmentChanges, legDataElementChanges, codeshareChanges);
        if (changeType == null) {
            return null;
        }

        ScheduleLegChangeEntity legChange = baseLegChange(importedFlight, importedLeg, activeLeg, changeType);
        segmentChanges.forEach(legChange::addSegmentChange);
        legDataElementChanges.forEach(legChange::addLegDeiChange);
        codeshareChanges.forEach(legChange::addCodeshareChange);
        return legChange;
    }

    private ScheduleLegChangeEntity baseLegChange(
            ScheduleFlightSnapshotDTO sourceFlight,
            ScheduleLegSnapshotDTO importedLeg,
            ScheduleLegSnapshotDTO activeLeg,
            LegChangeType changeType
    ) {
        ScheduleLegSnapshotDTO reference = importedLeg != null ? importedLeg : activeLeg;
        return ScheduleLegChangeEntity.builder()
                .legSequenceNumber(reference.getLegSequenceNumber())
                .periodStart(reference.getPeriodStart())
                .periodEnd(reference.getPeriodEnd())
                .daysOfOperation(reference.getDaysOfOperation())
                .departureStation(reference.getDepartureStation())
                .arrivalStation(reference.getArrivalStation())
                .changeType(changeType)
                .liveLegId(activeLeg != null ? activeLeg.getLegId() : null)
                .importedLegId(importedLeg != null ? importedLeg.getLegId() : null)
                .liveSnapshot(writeJson(activeLeg))
                .ingestedSnapshot(writeJson(importedLeg))
                .changeSetStatus(ChangeSetStatus.PENDING)
                .build();
    }

    private LegChangeType determineLegChangeType(
            ScheduleLegSnapshotDTO importedLeg,
            ScheduleLegSnapshotDTO activeLeg,
            List<ScheduleSegmentChangeEntity> segmentChanges,
            List<ScheduleDeiChangeEntity> legDataElementChanges,
            List<ScheduleCodeshareChangeEntity> codeshareChanges
    ) {
        boolean timeChanged = !sameTimes(importedLeg, activeLeg);
        boolean equipmentChanged = !sameEquipment(importedLeg, activeLeg);
        boolean periodChanged = !samePeriod(importedLeg, activeLeg);
        boolean routeChanged = !sameRoute(importedLeg, activeLeg);
        boolean serviceChanged = !Objects.equals(importedLeg.getServiceType(), activeLeg.getServiceType());
        boolean segmentChanged = !segmentChanges.isEmpty();
        boolean legDataChanged = !legDataElementChanges.isEmpty();
        boolean codeshareChanged = !codeshareChanges.isEmpty();
        boolean reinstated = "CANCELLED".equalsIgnoreCase(activeLeg.getLegStatus()) && !"CANCELLED".equalsIgnoreCase(importedLeg.getLegStatus());

        if (reinstated) {
            return LegChangeType.RIN;
        }
        if (!timeChanged && !equipmentChanged && !periodChanged && !routeChanged && !serviceChanged && !segmentChanged && !legDataChanged && !codeshareChanged) {
            return null;
        }
        if (codeshareChanged && !timeChanged && !equipmentChanged && !periodChanged && !routeChanged && !serviceChanged && !segmentChanged && !legDataChanged) {
            return LegChangeType.COD;
        }
        if (timeChanged && !equipmentChanged && !periodChanged && !routeChanged && !serviceChanged && !segmentChanged && !legDataChanged && !codeshareChanged) {
            return LegChangeType.TIM;
        }
        if (equipmentChanged && !timeChanged && !periodChanged && !routeChanged && !serviceChanged && !segmentChanged && !legDataChanged && !codeshareChanged) {
            return LegChangeType.EQT;
        }
        if (periodChanged && !timeChanged && !equipmentChanged && !routeChanged && !serviceChanged && !segmentChanged && !legDataChanged && !codeshareChanged) {
            return LegChangeType.PER;
        }
        return LegChangeType.FLT;
    }

    private List<ScheduleSegmentChangeEntity> compareSegments(
            List<ScheduleSegmentSnapshotDTO> importedSegments,
            List<ScheduleSegmentSnapshotDTO> activeSegments
    ) {
        Map<String, ScheduleSegmentSnapshotDTO> imported = indexSegments(importedSegments);
        Map<String, ScheduleSegmentSnapshotDTO> active = indexSegments(activeSegments);
        Set<String> keys = new LinkedHashSet<>();
        keys.addAll(imported.keySet());
        keys.addAll(active.keySet());

        List<ScheduleSegmentChangeEntity> changes = new ArrayList<>();
        for (String key : keys) {
            ScheduleSegmentSnapshotDTO importedSegment = imported.get(key);
            ScheduleSegmentSnapshotDTO activeSegment = active.get(key);

            if (activeSegment == null) {
                ScheduleSegmentChangeEntity segmentChange = ScheduleSegmentChangeEntity.builder()
                        .boardPoint(importedSegment.getBoardPoint())
                        .offPoint(importedSegment.getOffPoint())
                        .segmentChangeType(SegmentChangeType.ADDED)
                        .importedSegmentId(importedSegment.getSegmentId())
                        .changeSetStatus(ChangeSetStatus.PENDING)
                        .build();
                compareDataElements(importedSegment.getDataElements(), List.of(), false)
                        .forEach(segmentChange::addDeiChange);
                changes.add(segmentChange);
                continue;
            }

            if (importedSegment == null) {
                ScheduleSegmentChangeEntity segmentChange = ScheduleSegmentChangeEntity.builder()
                        .boardPoint(activeSegment.getBoardPoint())
                        .offPoint(activeSegment.getOffPoint())
                        .segmentChangeType(SegmentChangeType.REMOVED)
                        .liveSegmentId(activeSegment.getSegmentId())
                        .changeSetStatus(ChangeSetStatus.PENDING)
                        .build();
                compareDataElements(List.of(), activeSegment.getDataElements(), false)
                        .forEach(segmentChange::addDeiChange);
                changes.add(segmentChange);
                continue;
            }

            List<ScheduleDeiChangeEntity> deiChanges = compareDataElements(importedSegment.getDataElements(), activeSegment.getDataElements(), false);
            if (!deiChanges.isEmpty()) {
                ScheduleSegmentChangeEntity segmentChange = ScheduleSegmentChangeEntity.builder()
                        .boardPoint(importedSegment.getBoardPoint())
                        .offPoint(importedSegment.getOffPoint())
                        .segmentChangeType(SegmentChangeType.DEI_CHANGED)
                        .liveSegmentId(activeSegment.getSegmentId())
                        .importedSegmentId(importedSegment.getSegmentId())
                        .changeSetStatus(ChangeSetStatus.PENDING)
                        .build();
                deiChanges.forEach(segmentChange::addDeiChange);
                changes.add(segmentChange);
            }
        }

        return changes;
    }

    private List<ScheduleDeiChangeEntity> compareDataElements(
            List<ScheduleDataElementSnapshotDTO> importedDataElements,
            List<ScheduleDataElementSnapshotDTO> activeDataElements,
            boolean legScope
    ) {
        Map<String, ScheduleDataElementSnapshotDTO> imported = indexDataElements(importedDataElements);
        Map<String, ScheduleDataElementSnapshotDTO> active = indexDataElements(activeDataElements);
        Set<String> keys = new LinkedHashSet<>();
        keys.addAll(imported.keySet());
        keys.addAll(active.keySet());

        List<ScheduleDeiChangeEntity> changes = new ArrayList<>();
        for (String key : keys) {
            ScheduleDataElementSnapshotDTO importedDei = imported.get(key);
            ScheduleDataElementSnapshotDTO activeDei = active.get(key);

            if (activeDei == null) {
                changes.add(buildDeiChange(importedDei, null, DeiChangeType.ADDED, legScope));
                continue;
            }
            if (importedDei == null) {
                changes.add(buildDeiChange(null, activeDei, DeiChangeType.REMOVED, legScope));
                continue;
            }
            if (!Objects.equals(importedDei.getValue(), activeDei.getValue())) {
                changes.add(buildDeiChange(importedDei, activeDei, DeiChangeType.MODIFIED, legScope));
            }
        }

        return changes;
    }

    private ScheduleDeiChangeEntity buildDeiChange(
            ScheduleDataElementSnapshotDTO importedDei,
            ScheduleDataElementSnapshotDTO activeDei,
            DeiChangeType changeType,
            boolean legScope
    ) {
        ScheduleDataElementSnapshotDTO reference = importedDei != null ? importedDei : activeDei;
        return ScheduleDeiChangeEntity.builder()
                .deiScope(legScope
                        ? com.codeshare.airline.platform.core.enums.schedule.DeiScope.LEG
                        : com.codeshare.airline.platform.core.enums.schedule.DeiScope.SEGMENT)
                .deiCode(reference.getCode())
                .sequenceOrder(reference.getSequenceOrder() == null ? 1 : reference.getSequenceOrder())
                .deiChangeType(changeType)
                .liveValue(activeDei != null ? activeDei.getValue() : null)
                .ingestedValue(importedDei != null ? importedDei.getValue() : null)
                .liveDeiId(activeDei != null ? activeDei.getDataElementId() : null)
                .importedDataElementId(importedDei != null ? importedDei.getDataElementId() : null)
                .changeSetStatus(ChangeSetStatus.PENDING)
                .build();
    }

    private List<ScheduleCodeshareChangeEntity> compareCodeshares(
            List<ScheduleCodeshareSnapshotDTO> importedCodeshares,
            List<ScheduleCodeshareSnapshotDTO> activeCodeshares
    ) {
        Map<String, ScheduleCodeshareSnapshotDTO> imported = indexCodeshares(importedCodeshares);
        Map<String, ScheduleCodeshareSnapshotDTO> active = indexCodeshares(activeCodeshares);
        Set<String> keys = new LinkedHashSet<>();
        keys.addAll(imported.keySet());
        keys.addAll(active.keySet());

        List<ScheduleCodeshareChangeEntity> changes = new ArrayList<>();
        for (String key : keys) {
            ScheduleCodeshareSnapshotDTO importedCodeshare = imported.get(key);
            ScheduleCodeshareSnapshotDTO activeCodeshare = active.get(key);

            if (activeCodeshare == null) {
                changes.add(buildCodeshareChange(importedCodeshare, null, CodeshareChangeType.ADDED));
                continue;
            }
            if (importedCodeshare == null) {
                changes.add(buildCodeshareChange(null, activeCodeshare, CodeshareChangeType.REMOVED));
                continue;
            }
            if (!Objects.equals(importedCodeshare.getMarketingBookingDesignator(), activeCodeshare.getMarketingBookingDesignator())
                    || !Objects.equals(importedCodeshare.getMarketingOperationalSuffix(), activeCodeshare.getMarketingOperationalSuffix())
                    || importedCodeshare.isCodeshare() != activeCodeshare.isCodeshare()) {
                changes.add(buildCodeshareChange(importedCodeshare, activeCodeshare, CodeshareChangeType.MODIFIED));
            }
        }
        return changes;
    }

    private ScheduleCodeshareChangeEntity buildCodeshareChange(
            ScheduleCodeshareSnapshotDTO importedCodeshare,
            ScheduleCodeshareSnapshotDTO activeCodeshare,
            CodeshareChangeType changeType
    ) {
        ScheduleCodeshareSnapshotDTO reference = importedCodeshare != null ? importedCodeshare : activeCodeshare;
        return ScheduleCodeshareChangeEntity.builder()
                .changeType(changeType)
                .liveCodeshareId(activeCodeshare != null ? activeCodeshare.getCodeshareId() : null)
                .importedCodeshareId(importedCodeshare != null ? importedCodeshare.getCodeshareId() : null)
                .marketingAirlineCode(reference.getMarketingAirlineCode())
                .marketingFlightNumber(reference.getMarketingFlightNumber())
                .marketingOperationalSuffix(reference.getMarketingOperationalSuffix())
                .boardPoint(reference.getBoardPoint())
                .offPoint(reference.getOffPoint())
                .marketingBookingDesignator(reference.getMarketingBookingDesignator())
                .codeshare(reference.isCodeshare())
                .sourceDeiCode(reference.getSourceDeiCode())
                .sequenceOrder(reference.getSequenceOrder() == null ? 1 : reference.getSequenceOrder())
                .liveSnapshot(writeJson(activeCodeshare))
                .ingestedSnapshot(writeJson(importedCodeshare))
                .changeSetStatus(ChangeSetStatus.PENDING)
                .build();
    }

    private void populateAddedSegments(ScheduleLegChangeEntity legChange, List<ScheduleSegmentSnapshotDTO> segments) {
        if (segments == null) {
            return;
        }
        for (ScheduleSegmentSnapshotDTO segment : segments) {
            if (segment == null) {
                continue;
            }
            ScheduleSegmentChangeEntity segmentChange = ScheduleSegmentChangeEntity.builder()
                    .boardPoint(segment.getBoardPoint())
                    .offPoint(segment.getOffPoint())
                    .segmentChangeType(SegmentChangeType.ADDED)
                    .importedSegmentId(segment.getSegmentId())
                    .changeSetStatus(ChangeSetStatus.PENDING)
                    .build();
            compareDataElements(segment.getDataElements(), List.of(), false).forEach(segmentChange::addDeiChange);
            legChange.addSegmentChange(segmentChange);
        }
    }

    private void populateRemovedSegments(ScheduleLegChangeEntity legChange, List<ScheduleSegmentSnapshotDTO> segments) {
        if (segments == null) {
            return;
        }
        for (ScheduleSegmentSnapshotDTO segment : segments) {
            if (segment == null) {
                continue;
            }
            ScheduleSegmentChangeEntity segmentChange = ScheduleSegmentChangeEntity.builder()
                    .boardPoint(segment.getBoardPoint())
                    .offPoint(segment.getOffPoint())
                    .segmentChangeType(SegmentChangeType.REMOVED)
                    .liveSegmentId(segment.getSegmentId())
                    .changeSetStatus(ChangeSetStatus.PENDING)
                    .build();
            compareDataElements(List.of(), segment.getDataElements(), false).forEach(segmentChange::addDeiChange);
            legChange.addSegmentChange(segmentChange);
        }
    }

    private void populateAddedCodeshares(ScheduleLegChangeEntity legChange, List<ScheduleCodeshareSnapshotDTO> codeshares) {
        if (codeshares == null) {
            return;
        }
        codeshares.stream()
                .filter(Objects::nonNull)
                .map(codeshare -> buildCodeshareChange(codeshare, null, CodeshareChangeType.ADDED))
                .forEach(legChange::addCodeshareChange);
    }

    private void populateRemovedCodeshares(ScheduleLegChangeEntity legChange, List<ScheduleCodeshareSnapshotDTO> codeshares) {
        if (codeshares == null) {
            return;
        }
        codeshares.stream()
                .filter(Objects::nonNull)
                .map(codeshare -> buildCodeshareChange(null, codeshare, CodeshareChangeType.REMOVED))
                .forEach(legChange::addCodeshareChange);
    }

    private void populateAddedLegDataElements(ScheduleLegChangeEntity legChange, List<ScheduleDataElementSnapshotDTO> dataElements) {
        compareDataElements(dataElements, List.of(), true).forEach(legChange::addLegDeiChange);
    }

    private void populateRemovedLegDataElements(ScheduleLegChangeEntity legChange, List<ScheduleDataElementSnapshotDTO> dataElements) {
        compareDataElements(List.of(), dataElements, true).forEach(legChange::addLegDeiChange);
    }

    private void updateSummary(ChangeSetEntity run) {
        int newCount = 0;
        int cancelledCount = 0;
        int retimedCount = 0;
        int equipmentCount = 0;
        int otherCount = 0;

        for (ScheduleFlightChangeEntity flightChange : run.getFlightChanges()) {
            for (ScheduleLegChangeEntity legChange : flightChange.getLegChanges()) {
                switch (legChange.getChangeType()) {
                    case NEW, RIN -> newCount++;
                    case CNL -> cancelledCount++;
                    case TIM -> retimedCount++;
                    case EQT -> equipmentCount++;
                    default -> otherCount++;
                }
            }
        }

        run.setTotalLegsCompared(run.getFlightChanges().stream().mapToInt(flight -> flight.getLegChanges().size()).sum());
        run.setNewCount(newCount);
        run.setCancelledCount(cancelledCount);
        run.setRetimedCount(retimedCount);
        run.setEquipmentCount(equipmentCount);
        run.setOtherChangeCount(otherCount);
        run.setNoChangeCount(0);
    }

    private Map<String, ScheduleFlightSnapshotDTO> indexFlights(List<ScheduleFlightSnapshotDTO> flights) {
        Map<String, ScheduleFlightSnapshotDTO> indexed = new LinkedHashMap<>();
        if (flights == null) {
            return indexed;
        }
        for (ScheduleFlightSnapshotDTO flight : flights) {
            if (flight != null) {
                indexed.put(flightKey(flight), flight);
            }
        }
        return indexed;
    }

    private Map<String, ScheduleLegSnapshotDTO> indexLegs(List<ScheduleLegSnapshotDTO> legs) {
        Map<String, ScheduleLegSnapshotDTO> indexed = new LinkedHashMap<>();
        if (legs == null) {
            return indexed;
        }
        for (ScheduleLegSnapshotDTO leg : legs) {
            if (leg != null) {
                indexed.put(legKey(leg), leg);
            }
        }
        return indexed;
    }

    private Map<String, ScheduleSegmentSnapshotDTO> indexSegments(List<ScheduleSegmentSnapshotDTO> segments) {
        Map<String, ScheduleSegmentSnapshotDTO> indexed = new LinkedHashMap<>();
        if (segments == null) {
            return indexed;
        }
        for (ScheduleSegmentSnapshotDTO segment : segments) {
            if (segment != null) {
                indexed.put(segmentKey(segment), segment);
            }
        }
        return indexed;
    }

    private Map<String, ScheduleDataElementSnapshotDTO> indexDataElements(List<ScheduleDataElementSnapshotDTO> dataElements) {
        Map<String, ScheduleDataElementSnapshotDTO> indexed = new LinkedHashMap<>();
        if (dataElements == null) {
            return indexed;
        }
        for (ScheduleDataElementSnapshotDTO dataElement : dataElements) {
            if (dataElement != null) {
                indexed.put(dataElementKey(dataElement), dataElement);
            }
        }
        return indexed;
    }

    private Map<String, ScheduleCodeshareSnapshotDTO> indexCodeshares(List<ScheduleCodeshareSnapshotDTO> codeshares) {
        Map<String, ScheduleCodeshareSnapshotDTO> indexed = new LinkedHashMap<>();
        if (codeshares == null) {
            return indexed;
        }
        for (ScheduleCodeshareSnapshotDTO codeshare : codeshares) {
            if (codeshare != null) {
                indexed.put(codeshareKey(codeshare), codeshare);
            }
        }
        return indexed;
    }

    private String flightKey(ScheduleFlightSnapshotDTO flight) {
        return safe(flight.getAirlineCode())
                + "|"
                + safe(flight.getFlightNumber())
                + "|"
                + safe(flight.getOperationalSuffix())
                + "|"
                + safe(flight.getItineraryVariationId());
    }

    private String legKey(ScheduleLegSnapshotDTO leg) {
        return leg.getLegSequenceNumber()
                + "|"
                + String.valueOf(leg.getPeriodStart())
                + "|"
                + String.valueOf(leg.getPeriodEnd())
                + "|"
                + safe(leg.getDaysOfOperation());
    }

    private String segmentKey(ScheduleSegmentSnapshotDTO segment) {
        return safe(segment.getBoardPoint()) + "|" + safe(segment.getOffPoint());
    }

    private String dataElementKey(ScheduleDataElementSnapshotDTO dataElement) {
        return safe(dataElement.getCode())
                + "|"
                + safe(dataElement.getScope())
                + "|"
                + Objects.toString(dataElement.getSequenceOrder(), "")
                + "|"
                + safe(dataElement.getBoardPoint())
                + "|"
                + safe(dataElement.getOffPoint());
    }

    private String codeshareKey(ScheduleCodeshareSnapshotDTO codeshare) {
        return safe(codeshare.getMarketingAirlineCode())
                + "|"
                + safe(codeshare.getMarketingFlightNumber())
                + "|"
                + safe(codeshare.getMarketingOperationalSuffix())
                + "|"
                + safe(codeshare.getBoardPoint())
                + "|"
                + safe(codeshare.getOffPoint())
                + "|"
                + Objects.toString(codeshare.getSequenceOrder(), "");
    }

    private boolean sameTimes(ScheduleLegSnapshotDTO importedLeg, ScheduleLegSnapshotDTO activeLeg) {
        return Objects.equals(importedLeg.getScheduledDepartureTime(), activeLeg.getScheduledDepartureTime())
                && Objects.equals(importedLeg.getAircraftDepartureTime(), activeLeg.getAircraftDepartureTime())
                && Objects.equals(importedLeg.getAircraftArrivalTime(), activeLeg.getAircraftArrivalTime())
                && Objects.equals(importedLeg.getScheduledArrivalTime(), activeLeg.getScheduledArrivalTime())
                && Objects.equals(importedLeg.getDepartureUtcOffset(), activeLeg.getDepartureUtcOffset())
                && Objects.equals(importedLeg.getArrivalUtcOffset(), activeLeg.getArrivalUtcOffset())
                && Objects.equals(importedLeg.getDepartureTerminal(), activeLeg.getDepartureTerminal())
                && Objects.equals(importedLeg.getArrivalTerminal(), activeLeg.getArrivalTerminal());
    }

    private boolean sameEquipment(ScheduleLegSnapshotDTO importedLeg, ScheduleLegSnapshotDTO activeLeg) {
        return Objects.equals(importedLeg.getAircraftType(), activeLeg.getAircraftType())
                && Objects.equals(importedLeg.getAircraftConfiguration(), activeLeg.getAircraftConfiguration());
    }

    private boolean samePeriod(ScheduleLegSnapshotDTO importedLeg, ScheduleLegSnapshotDTO activeLeg) {
        return Objects.equals(importedLeg.getPeriodStart(), activeLeg.getPeriodStart())
                && Objects.equals(importedLeg.getPeriodEnd(), activeLeg.getPeriodEnd())
                && Objects.equals(importedLeg.getDaysOfOperation(), activeLeg.getDaysOfOperation())
                && Objects.equals(importedLeg.getFrequencyRate(), activeLeg.getFrequencyRate());
    }

    private boolean sameRoute(ScheduleLegSnapshotDTO importedLeg, ScheduleLegSnapshotDTO activeLeg) {
        return Objects.equals(importedLeg.getDepartureStation(), activeLeg.getDepartureStation())
                && Objects.equals(importedLeg.getArrivalStation(), activeLeg.getArrivalStation());
    }

    private String safe(String value) {
        return value == null ? "" : value.trim();
    }

    private String writeJson(Object value) {
        if (value == null) {
            return null;
        }

        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Unable to serialize schedule snapshot", ex);
        }
    }
}



