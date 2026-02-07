package com.codeshare.airline.processor.processing.persistence;

import com.codeshare.airline.processor.model.raw.*;
import com.codeshare.airline.processor.pipeline.dto.SsimR1HeaderRecordDTO;
import com.codeshare.airline.processor.pipeline.dto.SsimR3FlightLegRecordDTO;
import com.codeshare.airline.processor.pipeline.dto.SsimR4DateVariationRecordDTO;
import com.codeshare.airline.processor.pipeline.dto.SsimR5ContinuationRecordDTO;
import com.codeshare.airline.processor.pipeline.enm.SsimLoadStatus;
import com.codeshare.airline.processor.pipeline.model.ParsedSsimResult;
import com.codeshare.airline.processor.pipeline.model.SsimLoadContext;
import com.codeshare.airline.processor.processing.persistence.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SsimPersistenceServiceImpl implements SsimPersistenceService {

    private final SsimInboundDatasetRepository datasetRepository;
    private final SsimR1HeaderRepository r1Repository;
    private final SsimR3FlightLegRepository r3Repository;
    private final SsimR4DateVariationRepository r4Repository;
    private final SsimR5DateVariationContinuationRepository r5Repository;
    private final SsimAppendixHCodeshareRepository appendixHRepository;

    @Override
    public SsimLoadContext persist(ParsedSsimResult parsedSsimResult) {

        /* =========================================================
         * 0️⃣ STRUCTURAL SAFETY
         * ========================================================= */

        if (!parsedSsimResult.getStructuralErrors().isEmpty()) {
            throw new IllegalStateException("SSIM contains structural errors: " + parsedSsimResult.getStructuralErrors());
        }

        if (parsedSsimResult.getHeader() == null) {
            throw new IllegalStateException("SSIM missing mandatory Record-1");
        }

        SsimR1HeaderRecordDTO r1Dto = parsedSsimResult.getHeader();

        /* =========================================================
         * 1️⃣ DATASET METADATA (AUTHORITATIVE)
         * ========================================================= */

        datasetRepository.findByDatasetSerialNumber(r1Dto.getDatasetSerialNumber()).ifPresent(d -> {
                    throw new IllegalStateException("Dataset already loaded: " + r1Dto.getDatasetSerialNumber());
                });

        SsimInboundDatasetMetadata ssimInboundDatasetMetadata = SsimInboundDatasetMetadata.builder()
                .datasetSerialNumber(r1Dto.getDatasetSerialNumber())
                .publishingCarrier(r1Dto.getAirlineDesignator())
                .receivedAt(Instant.now())
                .loadedAt(Instant.now())
                .status(SsimLoadStatus.LOADED)
                .build();
        SsimInboundDatasetMetadata dataset = datasetRepository.save(ssimInboundDatasetMetadata);

        /* =========================================================
         * 2️⃣ R1 — HEADER (DATA ONLY)
         * ========================================================= */
        SsimR1HeaderRecord ssimR1HeaderRecord = SsimR1HeaderRecord.builder()
                .dataset(dataset)
                .recordType("1")
                .airlineDesignator(r1Dto.getAirlineDesignator())
                .datasetSerialNumber(r1Dto.getDatasetSerialNumber())
                .creationDate(r1Dto.getCreationDate())
                .scheduleType(r1Dto.getScheduleType())
                .periodStartDate(r1Dto.getPeriodStartDate())
                .periodEndDate(r1Dto.getPeriodEndDate())
                .versionNumber(r1Dto.getVersionNumber())
                .continuationIndicator(r1Dto.getContinuationIndicator())
                .generalInformation(r1Dto.getGeneralInformation())
                .build();
        SsimR1HeaderRecord header = r1Repository.save(ssimR1HeaderRecord);

        /* =========================================================
         * 3️⃣ R3 — FLIGHT LEGS (ORDER MATTERS)
         * ========================================================= */

        List<SsimR3FlightLegRecord> persistedR3 = new ArrayList<>();

        for (SsimR3FlightLegRecordDTO dto : parsedSsimResult.getFlightLegs()) {
            persistedR3.add(r3Repository.save(toR3Entity(dto, dataset)));
        }

        /* =========================================================
         * 4️⃣ R4 + R5 — DATE VARIATIONS (CONTEXT BASED)
         * ========================================================= */

        List<SsimR4DateVariationRecord> persistedR4 = new ArrayList<>();
        List<SsimR5ContinuationRecord> persistedR5 = new ArrayList<>();

        SsimR3FlightLegRecord currentR3 = null;
        SsimR4DateVariationRecord currentR4 = null;

        int r3Pointer = 0;

        for (Object record : parsedSsimResult.getOrderedRecords()) {

            /* ----------------- R3 ----------------- */
            if (record instanceof SsimR3FlightLegRecordDTO) {
                currentR3 = persistedR3.get(r3Pointer++);
                currentR4 = null;
                continue;
            }

            /* ----------------- R4 ----------------- */
            if (record instanceof SsimR4DateVariationRecordDTO r4Dto) {

                if (currentR3 == null) {
                    throw new IllegalStateException("R4 without preceding R3");
                }

                currentR4 = r4Repository.save( toR4Entity(r4Dto, currentR3));

                persistedR4.add(currentR4);
                continue;
            }

            /* ----------------- R5 ----------------- */
            if (record instanceof SsimR5ContinuationRecordDTO r5Dto) {

                if (currentR4 == null) {
                    throw new IllegalStateException("R5 without preceding R4");
                }
                SsimR5ContinuationRecord ssimR5ContinuationRecord = SsimR5ContinuationRecord.builder()
                        .r4(currentR4)
                        .recordType("6")
                        .continuationData(r5Dto.getContinuationData())
                        .build();
                persistedR5.add(r5Repository.save(ssimR5ContinuationRecord));
            }
        }
        /* =========================================================
         * 5️⃣ APPENDIX-H — CODESHARE
         * ========================================================= */

        List<SsimAppendixHCodeshare> appendixH = new ArrayList<>();

        parsedSsimResult.getCodeshares().forEach(dto -> {

            SsimR3FlightLegRecord operatingLeg = persistedR3.stream()
                    .filter(r3 ->
                            r3.getAirlineDesignator().equals(dto.getOperatingAirlineDesignator())
                                    && r3.getFlightNumber().equals(dto.getOperatingFlightNumber()))
                    .findFirst()
                    .orElseThrow(() ->
                            new IllegalStateException("Orphan Appendix-H record: " + dto));

            appendixH.add(SsimAppendixHCodeshare.builder()
                    .operatingLeg(operatingLeg)
                    .marketingCarrier(dto.getMarketingCarrier())
                    .marketingFlightNumber(dto.getMarketingFlightNumber())
                    .priorityOrder(dto.getPriorityOrder())
                    .disclosureIndicator(dto.getDisclosureIndicator())
                    .codeshareType(dto.getCodeshareType())
                    .build());
        });

        appendixHRepository.saveAll(appendixH);

        /* =========================================================
         * 6️⃣ FINALIZE DATASET
         * ========================================================= */

        dataset.setTotalFlightLegs(persistedR3.size());
        dataset.setTotalDateVariations(persistedR4.size());
        dataset.setTotalContinuationRecords(persistedR5.size());
        dataset.setStatus(SsimLoadStatus.PERSISTED);

        datasetRepository.save(dataset);
        
        return SsimLoadContext.builder()
                .datasetId(dataset.getId())
                .status(dataset.getStatus())
                .build();
    }

    /* =========================================================
     * ENTITY MAPPERS
     * ========================================================= */

    private SsimR3FlightLegRecord toR3Entity(
            SsimR3FlightLegRecordDTO dto,
            SsimInboundDatasetMetadata dataset) {

        return SsimR3FlightLegRecord.builder()
                .dataset(dataset)
                .recordType("3")
                .airlineDesignator(dto.getAirlineDesignator())
                .flightNumber(dto.getFlightNumber())
                .operationalSuffix(dto.getOperationalSuffix())
                .originAirport(dto.getOriginAirport())
                .destinationAirport(dto.getDestinationAirport())
                .scheduledDepartureTime(dto.getScheduledDepartureTime())
                .scheduledArrivalTime(dto.getScheduledArrivalTime())
                .overMidnightIndicator(dto.getOvernightIndicator())
                .periodStartDate(dto.getPeriodStartDate())
                .periodEndDate(dto.getPeriodEndDate())
                .daysOfOperation(dto.getDaysOfOperation())
                .aircraftType(dto.getAircraftType())
                .aircraftConfiguration(dto.getAircraftConfiguration())
                .serviceType(dto.getServiceType())
                .trafficRestrictionCode(dto.getTrafficRestrictionCode())
                .remarks(dto.getRemarks())
                .build();
    }

    private SsimR4DateVariationRecord toR4Entity(
            SsimR4DateVariationRecordDTO dto,
            SsimR3FlightLegRecord parent) {

        return SsimR4DateVariationRecord.builder()
                .flightLeg(parent)
                .recordType("4")
                .actionCode(dto.getActionCode())
                .variationDate(dto.getVariationDate())
                .dayChangeIndicator(dto.getDayChangeIndicator())
                .scheduledDepartureTime(dto.getScheduledDepartureTime())
                .scheduledArrivalTime(dto.getScheduledArrivalTime())
                .aircraftType(dto.getAircraftType())
                .trafficRestrictionCode(dto.getTrafficRestrictionCode())
                .remarks(dto.getRemarks())
                .build();
    }


    @Override
    public SsimLoadContext updateStatus(SsimLoadContext context, SsimLoadStatus status) {
        datasetRepository.updateStatus(context.getDatasetId(), status, Instant.now());
        return context.toBuilder()
                .status(status)
                .build();
    }
}
