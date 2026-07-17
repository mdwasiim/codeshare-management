package com.codeshare.airline.schedule.live.application;

import com.codeshare.airline.platform.core.dto.schedule.workflow.ChangeSetDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ScheduleCodeshareSnapshotDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ScheduleDataElementSnapshotDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ScheduleFlightChangeDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ScheduleFlightSnapshotDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ScheduleLegChangeDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ScheduleLegSnapshotDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ScheduleSegmentSnapshotDTO;
import com.codeshare.airline.schedule.live.domain.entity.ActiveScheduleEntity;
import com.codeshare.airline.schedule.live.domain.entity.LiveCodeshareDesignatorEntity;
import com.codeshare.airline.schedule.live.domain.entity.LiveFlightEntity;
import com.codeshare.airline.schedule.live.domain.entity.LiveFlightLegEntity;
import com.codeshare.airline.schedule.live.domain.entity.LiveLegDataElementEntity;
import com.codeshare.airline.schedule.live.domain.entity.LiveScheduleVersionEntity;
import com.codeshare.airline.schedule.live.domain.entity.LiveSegmentDeiEntity;
import com.codeshare.airline.schedule.live.domain.entity.LiveSegmentEntity;
import com.codeshare.airline.schedule.live.domain.enums.LiveScheduleSource;
import com.codeshare.airline.schedule.live.domain.enums.LiveScheduleStatus;
import com.codeshare.airline.schedule.live.domain.enums.ScheduleChangeType;
import com.codeshare.airline.schedule.live.domain.repository.ActiveScheduleRepository;
import com.codeshare.airline.schedule.live.domain.repository.LiveFlightRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ScheduleChangeApplicationService {

    private final LiveFlightRepository liveFlightRepository;
    private final ActiveScheduleRepository activeScheduleRepository;
    private final ActiveScheduleMapper snapshotMapper;
    private final ObjectMapper objectMapper;

    public ScheduleChangeApplicationService(
            LiveFlightRepository liveFlightRepository,
            ActiveScheduleRepository activeScheduleRepository,
            ActiveScheduleMapper snapshotMapper,
            ObjectMapper objectMapper
    ) {
        this.liveFlightRepository = liveFlightRepository;
        this.activeScheduleRepository = activeScheduleRepository;
        this.snapshotMapper = snapshotMapper;
        this.objectMapper = objectMapper;
    }

    public void apply(ChangeSetDTO changeSet) {
        ActiveScheduleEntity activeSchedule = activeScheduleRepository.findByAirlineCode(changeSet.getAirlineCode())
                .orElseGet(() -> ActiveScheduleEntity.builder()
                        .airlineCode(changeSet.getAirlineCode())
                        .build());

        for (ScheduleFlightChangeDTO flightChange : changeSet.getFlightChanges()) {
            applyFlightChange(changeSet, flightChange);
        }

        activeSchedule.setLastAppliedChangeSetId(changeSet.getChangeSetId());
        activeSchedule.setLastImportedScheduleId(changeSet.getImportedScheduleId());
        activeSchedule.setLastImportBatchId(changeSet.getImportBatchId());
        activeSchedule.setLastMessageType(changeSet.getMessageType());
        activeSchedule.setLastUpdatedAt(Instant.now());
        activeScheduleRepository.save(activeSchedule);
    }

    private void applyFlightChange(ChangeSetDTO changeSet, ScheduleFlightChangeDTO flightChange) {
        LiveFlightEntity flight = liveFlightRepository.findByAirlineCodeAndFlightNumberAndOperationalSuffixAndItineraryVariationId(
                        flightChange.getAirlineCode(),
                        flightChange.getFlightNumber(),
                        flightChange.getOperationalSuffix(),
                        flightChange.getItineraryVariationId())
                .orElseGet(() -> newFlight(flightChange));

        for (ScheduleLegChangeDTO legChange : flightChange.getLegChanges()) {
            applyLegChange(changeSet, flight, legChange);
        }

        flight.setFlightStatus(resolveFlightStatus(flight));
        liveFlightRepository.save(flight);
    }

    private void applyLegChange(
            ChangeSetDTO changeSet,
            LiveFlightEntity flight,
            ScheduleLegChangeDTO legChange
    ) {
        ScheduleLegSnapshotDTO newValue = legChange.getNewValue();
        ScheduleLegSnapshotDTO oldValue = legChange.getOldValue();
        ScheduleLegSnapshotDTO reference = newValue != null ? newValue : oldValue;

        LiveFlightLegEntity leg = locateLeg(flight, legChange, reference);
        if (leg == null) {
            leg = new LiveFlightLegEntity();
            flight.addLeg(leg);
        }

        if (legChange.getChangeType() == com.codeshare.airline.platform.core.enums.schedule.ScheduleLegChangeType.CNL) {
            if (oldValue != null) {
                mapLegSnapshot(leg, oldValue, changeSet);
            }
            leg.setLegStatus(LiveScheduleStatus.CANCELLED);
            appendVersion(changeSet, leg, oldValue, ScheduleChangeType.CANCELLED);
            return;
        }

        if (newValue == null) {
            return;
        }

        mapLegSnapshot(leg, newValue, changeSet);
        replaceLegDataElements(leg, newValue.getLegDataElements());
        replaceSegments(leg, newValue.getSegments());
        replaceCodeshares(leg, newValue.getCodeshares());
        leg.setLegStatus(LiveScheduleStatus.ACTIVE);
        appendVersion(changeSet, leg, newValue, mapChangeType(legChange.getChangeType()));
    }

    private LiveFlightLegEntity locateLeg(
            LiveFlightEntity flight,
            ScheduleLegChangeDTO legChange,
            ScheduleLegSnapshotDTO reference
    ) {
        if (legChange.getLiveLegId() != null && flight.getLegs() != null) {
            for (LiveFlightLegEntity leg : flight.getLegs()) {
                if (legChange.getLiveLegId().equals(leg.getId())) {
                    return leg;
                }
            }
        }

        if (flight.getLegs() == null) {
            return null;
        }

        for (LiveFlightLegEntity leg : flight.getLegs()) {
            if (reference != null
                    && reference.getLegSequenceNumber() != null
                    && reference.getLegSequenceNumber().equals(leg.getLegSequenceNumber())
                    && java.util.Objects.equals(reference.getPeriodStart(), leg.getPeriodStart())
                    && java.util.Objects.equals(reference.getPeriodEnd(), leg.getPeriodEnd())
                    && java.util.Objects.equals(reference.getDaysOfOperation(), leg.getDaysOfOperation())) {
                return leg;
            }
        }

        return null;
    }

    private void mapLegSnapshot(
            LiveFlightLegEntity leg,
            ScheduleLegSnapshotDTO snapshot,
            ChangeSetDTO changeSet
    ) {
        leg.setSource(LiveScheduleSource.valueOf(changeSet.getMessageType().name()));
        leg.setLegSequenceNumber(snapshot.getLegSequenceNumber());
        leg.setServiceType(snapshot.getServiceType());
        leg.setPeriodStart(snapshot.getPeriodStart());
        leg.setPeriodEnd(snapshot.getPeriodEnd());
        leg.setDaysOfOperation(snapshot.getDaysOfOperation());
        leg.setFrequencyRate(snapshot.getFrequencyRate());
        leg.setDepartureStation(snapshot.getDepartureStation());
        leg.setScheduledDepartureTime(snapshot.getScheduledDepartureTime());
        leg.setAircraftDepartureTime(snapshot.getAircraftDepartureTime());
        leg.setDepartureUtcOffset(snapshot.getDepartureUtcOffset());
        leg.setDepartureTerminal(snapshot.getDepartureTerminal());
        leg.setArrivalStation(snapshot.getArrivalStation());
        leg.setAircraftArrivalTime(snapshot.getAircraftArrivalTime());
        leg.setScheduledArrivalTime(snapshot.getScheduledArrivalTime());
        leg.setArrivalUtcOffset(snapshot.getArrivalUtcOffset());
        leg.setArrivalTerminal(snapshot.getArrivalTerminal());
        leg.setDateVariation(snapshot.getDateVariation());
        leg.setAircraftType(snapshot.getAircraftType());
        leg.setAircraftConfiguration(snapshot.getAircraftConfiguration());
        leg.setBookingDesignator(snapshot.getBookingDesignator());
        leg.setBookingModifier(snapshot.getBookingModifier());
        leg.setMealServiceNote(snapshot.getMealServiceNote());
        leg.setJointOperationAirlines(snapshot.getJointOperationAirlines());
        leg.setMinimumConnectingTimeStatus(snapshot.getMinimumConnectingTimeStatus());
        leg.setSecureFlightIndicator(snapshot.getSecureFlightIndicator());
        leg.setAircraftOwner(snapshot.getAircraftOwner());
        leg.setCockpitCrewEmployer(snapshot.getCockpitCrewEmployer());
        leg.setCabinCrewEmployer(snapshot.getCabinCrewEmployer());
        leg.setOnwardAirlineDesignator(snapshot.getOnwardAirlineDesignator());
        leg.setOnwardFlightNumber(snapshot.getOnwardFlightNumber());
        leg.setOnwardOperationalSuffix(snapshot.getOnwardOperationalSuffix());
        leg.setAircraftRotationLayover(snapshot.getAircraftRotationLayover());
        leg.setFlightTransitLayover(snapshot.getFlightTransitLayover());
        leg.setOperatingAirlineDisclosure(snapshot.getOperatingAirlineDisclosure());
        leg.setTrafficRestrictionCode(snapshot.getTrafficRestrictionCode());
    }

    private void replaceLegDataElements(LiveFlightLegEntity leg, List<ScheduleDataElementSnapshotDTO> dataElements) {
        leg.getLegDataElements().clear();
        if (dataElements == null) {
            return;
        }

        for (ScheduleDataElementSnapshotDTO dataElementSnapshot : dataElements) {
            LiveLegDataElementEntity dei = new LiveLegDataElementEntity();
            dei.setDataElementIdentifier(dataElementSnapshot.getCode());
            dei.setDeiData(dataElementSnapshot.getValue());
            dei.setSequenceOrder(dataElementSnapshot.getSequenceOrder() == null ? 1 : dataElementSnapshot.getSequenceOrder());
            leg.addLegDataElement(dei);
        }
    }

    private void replaceSegments(LiveFlightLegEntity leg, List<ScheduleSegmentSnapshotDTO> segments) {
        leg.getSegments().clear();
        if (segments == null) {
            return;
        }

        for (ScheduleSegmentSnapshotDTO segmentSnapshot : segments) {
            LiveSegmentEntity segment = new LiveSegmentEntity();
            segment.setBoardPoint(segmentSnapshot.getBoardPoint());
            segment.setOffPoint(segmentSnapshot.getOffPoint());

            if (segmentSnapshot.getDataElements() != null) {
                for (var dataElement : segmentSnapshot.getDataElements()) {
                    LiveSegmentDeiEntity dei = new LiveSegmentDeiEntity();
                    dei.setDataElementIdentifier(dataElement.getCode());
                    dei.setDeiData(dataElement.getValue());
                    dei.setSequenceOrder(dataElement.getSequenceOrder() == null ? 1 : dataElement.getSequenceOrder());
                    segment.addDei(dei);
                }
            }

            leg.addSegment(segment);
        }
    }

    private void replaceCodeshares(LiveFlightLegEntity leg, List<ScheduleCodeshareSnapshotDTO> codeshares) {
        leg.getCodeshareDesignators().clear();
        if (codeshares == null) {
            return;
        }

        for (ScheduleCodeshareSnapshotDTO codeshareSnapshot : codeshares) {
            LiveCodeshareDesignatorEntity codeshare = new LiveCodeshareDesignatorEntity();
            codeshare.setMarketingAirlineCode(codeshareSnapshot.getMarketingAirlineCode());
            codeshare.setMarketingFlightNumber(codeshareSnapshot.getMarketingFlightNumber());
            codeshare.setMarketingOperationalSuffix(codeshareSnapshot.getMarketingOperationalSuffix());
            codeshare.setBoardPoint(codeshareSnapshot.getBoardPoint());
            codeshare.setOffPoint(codeshareSnapshot.getOffPoint());
            codeshare.setMarketingBookingDesignator(codeshareSnapshot.getMarketingBookingDesignator());
            codeshare.setSourceDeiCode(codeshareSnapshot.getSourceDeiCode());
            codeshare.setCodeshare(codeshareSnapshot.isCodeshare());
            codeshare.setSequenceOrder(codeshareSnapshot.getSequenceOrder() == null ? 1 : codeshareSnapshot.getSequenceOrder());
            leg.addCodeshareDesignator(codeshare);
        }
    }

    private void appendVersion(
            ChangeSetDTO changeSet,
            LiveFlightLegEntity leg,
            ScheduleLegSnapshotDTO sourceSnapshot,
            ScheduleChangeType changeType
    ) {
        ScheduleLegSnapshotDTO currentSnapshot = snapshotMapper.toLegSnapshot(leg);
        LiveScheduleVersionEntity version = new LiveScheduleVersionEntity();
        version.setVersionNumber(nextVersionNumber(leg));
        version.setChangeType(changeType);
        version.setSource(LiveScheduleSource.valueOf(changeSet.getMessageType().name()));
        version.setMessageType(changeSet.getMessageType());
        version.setSourceReferenceId(changeSet.getChangeSetId());
        version.setSourceFileId(changeSet.getImportedScheduleId());
        version.setSourceLoadId(changeSet.getImportBatchId());
        version.setLegSnapshot(writeJson(currentSnapshot));
        version.setChangeSummary(writeJson(detectFieldChanges(sourceSnapshot, currentSnapshot)));
        version.setAppliedAt(Instant.now());
        version.setAppliedBy("schedule-service");
        version.setSnapshotPeriodStart(currentSnapshot.getPeriodStart());
        version.setSnapshotPeriodEnd(currentSnapshot.getPeriodEnd());
        version.setSnapshotDaysOfOperation(currentSnapshot.getDaysOfOperation());
        version.setSnapshotDepartureStation(currentSnapshot.getDepartureStation());
        version.setSnapshotArrivalStation(currentSnapshot.getArrivalStation());
        version.setSnapshotAircraftType(currentSnapshot.getAircraftType());
        leg.addVersion(version);
    }

    private Long nextVersionNumber(LiveFlightLegEntity leg) {
        return leg.getVersions().stream()
                .map(LiveScheduleVersionEntity::getVersionNumber)
                .filter(java.util.Objects::nonNull)
                .max(Long::compareTo)
                .orElse(0L) + 1;
    }

    private Map<String, List<String>> detectFieldChanges(
            ScheduleLegSnapshotDTO before,
            ScheduleLegSnapshotDTO after
    ) {
        Map<String, List<String>> changes = new LinkedHashMap<>();
        if (before == null || after == null) {
            changes.put("snapshot", List.of(writeJson(before), writeJson(after)));
            return changes;
        }

        addChange(changes, "periodStart", before.getPeriodStart(), after.getPeriodStart());
        addChange(changes, "periodEnd", before.getPeriodEnd(), after.getPeriodEnd());
        addChange(changes, "daysOfOperation", before.getDaysOfOperation(), after.getDaysOfOperation());
        addChange(changes, "departureStation", before.getDepartureStation(), after.getDepartureStation());
        addChange(changes, "arrivalStation", before.getArrivalStation(), after.getArrivalStation());
        addChange(changes, "scheduledDepartureTime", before.getScheduledDepartureTime(), after.getScheduledDepartureTime());
        addChange(changes, "scheduledArrivalTime", before.getScheduledArrivalTime(), after.getScheduledArrivalTime());
        addChange(changes, "aircraftType", before.getAircraftType(), after.getAircraftType());
        addChange(changes, "aircraftConfiguration", before.getAircraftConfiguration(), after.getAircraftConfiguration());
        addChange(changes, "serviceType", before.getServiceType(), after.getServiceType());
        addChange(changes, "trafficRestrictionCode", before.getTrafficRestrictionCode(), after.getTrafficRestrictionCode());
        return changes;
    }

    private void addChange(Map<String, List<String>> changes, String field, Object before, Object after) {
        String beforeValue = before == null ? null : before.toString();
        String afterValue = after == null ? null : after.toString();
        if (!java.util.Objects.equals(beforeValue, afterValue)) {
            changes.put(field, List.of(String.valueOf(beforeValue), String.valueOf(afterValue)));
        }
    }

    private ScheduleChangeType mapChangeType(com.codeshare.airline.platform.core.enums.schedule.ScheduleLegChangeType changeType) {
        return switch (changeType) {
            case NEW -> ScheduleChangeType.NEW;
            case TIM -> ScheduleChangeType.RETIMED;
            case EQT -> ScheduleChangeType.EQUIPMENT;
            case CNL -> ScheduleChangeType.CANCELLED;
            case RIN -> ScheduleChangeType.REINSTATED;
            default -> ScheduleChangeType.REVISED;
        };
    }

    private LiveScheduleStatus resolveFlightStatus(LiveFlightEntity flight) {
        boolean hasActive = flight.getLegs().stream().anyMatch(leg -> leg.getLegStatus() == LiveScheduleStatus.ACTIVE);
        if (hasActive) {
            return LiveScheduleStatus.ACTIVE;
        }
        boolean hasCancelled = flight.getLegs().stream().anyMatch(leg -> leg.getLegStatus() == LiveScheduleStatus.CANCELLED);
        return hasCancelled ? LiveScheduleStatus.CANCELLED : LiveScheduleStatus.WITHDRAWN;
    }

    private LiveFlightEntity newFlight(ScheduleFlightChangeDTO flightChange) {
        LiveFlightEntity flight = new LiveFlightEntity();
        flight.setAirlineCode(flightChange.getAirlineCode());
        flight.setFlightNumber(flightChange.getFlightNumber());
        flight.setOperationalSuffix(flightChange.getOperationalSuffix());
        flight.setItineraryVariationId(flightChange.getItineraryVariationId());
        flight.setFlightStatus(LiveScheduleStatus.ACTIVE);
        return flight;
    }

    private String writeJson(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Unable to serialize live schedule payload", ex);
        }
    }
}

