package com.codeshare.airline.schedule.ingestion.persistence.services.ssim;

import com.codeshare.airline.schedule.ingestion.domain.enums.ProcessingStatus;
import com.codeshare.airline.core.enums.schedule.SourceType;
import com.codeshare.airline.schedule.ingestion.api.response.SsimLoadedScheduleDetailResponse;
import com.codeshare.airline.schedule.ingestion.api.response.SsimLoadedScheduleSummaryResponse;
import com.codeshare.airline.schedule.ingestion.dto.ssim.record.SsimDataElementDTO;
import com.codeshare.airline.schedule.ingestion.dto.ssim.record.SsimFlightDTO;
import com.codeshare.airline.schedule.ingestion.dto.ssim.SSIMMessageDTO;
import com.codeshare.airline.schedule.ingestion.dto.ssim.SsimMetaDataDTO;
import com.codeshare.airline.schedule.ingestion.persistence.entities.ssim.SsimFileMetaDataEntity;
import com.codeshare.airline.schedule.ingestion.persistence.entities.ssim.SsimFlightEntity;
import com.codeshare.airline.schedule.ingestion.persistence.mappers.ssim.SsimCarrierMapper;
import com.codeshare.airline.schedule.ingestion.persistence.mappers.ssim.SsimDataElementMapper;
import com.codeshare.airline.schedule.ingestion.persistence.mappers.ssim.SsimFileMetaDataMapper;
import com.codeshare.airline.schedule.ingestion.persistence.mappers.ssim.SsimFlightMapper;
import com.codeshare.airline.schedule.ingestion.persistence.mappers.ssim.SsimHeaderMapper;
import com.codeshare.airline.schedule.ingestion.persistence.mappers.ssim.SsimTrailerMapper;
import com.codeshare.airline.schedule.ingestion.persistence.repositories.ssim.SsimFileMetaDataRepository;
import com.codeshare.airline.schedule.ingestion.persistence.repositories.ssim.SsimFlightRepository;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SsimScheduleQueryService {

    private final SsimFileMetaDataRepository fileRepository;
    private final SsimFlightRepository flightRepository;
    private final SsimFileMetaDataMapper fileMapper;
    private final SsimHeaderMapper headerMapper;
    private final SsimCarrierMapper carrierMapper;
    private final SsimFlightMapper flightMapper;
    private final SsimDataElementMapper deiMapper;

    public Page<SsimMetaDataDTO> searchFiles(
            String airlineCode,
            ProcessingStatus processingStatus,
            Instant receivedFrom,
            Instant receivedTo,
            String fileName,
            SourceType sourceType,
            Pageable pageable
    ) {
        return fileRepository.findAll(
                fileSpec(airlineCode, processingStatus, receivedFrom, receivedTo, fileName, sourceType),
                pageable
        ).map(fileMapper::toDTO);
    }

    public Page<SsimLoadedScheduleSummaryResponse> searchLoadedSchedules(
            String tenantCode,
            Pageable pageable
    ) {
        return fileRepository.findAll(
                fileSpec(normalizeTenantCode(tenantCode), null, null, null, null, null),
                pageable
        ).map(file -> SsimLoadedScheduleSummaryResponse.builder()
                .file(fileMapper.toDTO(file))
                .flightCount(flightRepository.countByCarrier_File_FileId(file.getFileId()))
                .build());
    }

    public SsimMetaDataDTO getFile(UUID fileId) {
        return fileMapper.toDTO(findFile(fileId));
    }

    public SSIMMessageDTO getMessage(UUID fileId) {
        SsimFileMetaDataEntity file = findFile(fileId);
        return toMessage(file);
    }

    public SsimLoadedScheduleDetailResponse getLoadedSchedule(String tenantCode, UUID fileId) {
        SsimFileMetaDataEntity file = findFile(tenantCode, fileId);

        return SsimLoadedScheduleDetailResponse.builder()
                .file(fileMapper.toDTO(file))
                .schedule(toMessage(file))
                .flightCount(flightRepository.countByCarrier_File_FileId(fileId))
                .build();
    }

    public Page<SsimFlightDTO> searchFlights(
            UUID fileId,
            String airlineCode,
            String flightNumber,
            String departureStation,
            String arrivalStation,
            String aircraftType,
            String serviceType,
            String operatingDays,
            Pageable pageable
    ) {
        return flightRepository.findAll(
                flightSpec(
                        fileId,
                        airlineCode,
                        flightNumber,
                        departureStation,
                        arrivalStation,
                        aircraftType,
                        serviceType,
                        operatingDays
                ),
                pageable
        ).map(flightMapper::toDTO);
    }

    public SsimFlightDTO getFlight(UUID flightId) {
        SsimFlightEntity flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "SSIM flight not found: " + flightId));

        return toFlightWithDeis(flight);
    }

    private SsimFileMetaDataEntity findFile(UUID fileId) {
        return fileRepository.findByFileId(fileId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "SSIM file not found: " + fileId));
    }

    private SsimFileMetaDataEntity findFile(String tenantCode, UUID fileId) {
        String normalizedTenantCode = normalizeTenantCode(tenantCode);
        return fileRepository.findByFileId(fileId)
                .filter(file -> normalizedTenantCode.equals(file.getAirlineCode()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "SSIM file not found: " + fileId));
    }

    private SSIMMessageDTO toMessage(SsimFileMetaDataEntity file) {
        SSIMMessageDTO message = new SSIMMessageDTO();

        message.setHeader(headerMapper.toDTO(file.getHeader()));
        message.setCarrier(carrierMapper.toDTO(file.getCarrier()));
        message.setTrailer(SsimTrailerMapper.toDto(file.getTrailer()));

        if (file.getCarrier() != null && file.getCarrier().getFlights() != null) {
            List<SsimFlightDTO> flights = file.getCarrier().getFlights()
                    .stream()
                    .map(this::toFlightWithDeis)
                    .toList();
            message.setFlights(flights);
            if (!flights.isEmpty()) {
                message.setAirlineCode(flights.getFirst().getAirlineCode());
                message.setFlightNumber(flights.getFirst().getFlightNumber());
            }
        }

        return message;
    }

    private SsimFlightDTO toFlightWithDeis(SsimFlightEntity flight) {
        SsimFlightDTO dto = flightMapper.toDTO(flight);
        if (flight.getDeis() != null) {
            List<SsimDataElementDTO> deis = flight.getDeis()
                    .stream()
                    .map(deiMapper::toDTO)
                    .toList();
            dto.setDeis(deis);
        }
        return dto;
    }

    private Specification<SsimFileMetaDataEntity> fileSpec(
            String airlineCode,
            ProcessingStatus processingStatus,
            Instant receivedFrom,
            Instant receivedTo,
            String fileName,
            SourceType sourceType
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (hasText(airlineCode)) {
                predicates.add(cb.equal(root.get("airlineCode"), airlineCode.trim()));
            }
            if (processingStatus != null) {
                predicates.add(cb.equal(root.get("processingStatus"), processingStatus));
            }
            if (receivedFrom != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("receivedTimestamp"), receivedFrom));
            }
            if (receivedTo != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("receivedTimestamp"), receivedTo));
            }
            if (hasText(fileName)) {
                predicates.add(cb.like(cb.lower(root.get("fileName")), "%" + fileName.trim().toLowerCase() + "%"));
            }
            if (sourceType != null) {
                predicates.add(cb.equal(root.get("sourceType"), sourceType));
            }

            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }

    private Specification<SsimFlightEntity> flightSpec(
            UUID fileId,
            String airlineCode,
            String flightNumber,
            String departureStation,
            String arrivalStation,
            String aircraftType,
            String serviceType,
            String operatingDays
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            Join<Object, Object> carrier = root.join("carrier");
            Join<Object, Object> file = carrier.join("file");

            predicates.add(cb.equal(file.get("fileId"), fileId));

            if (hasText(airlineCode)) {
                predicates.add(cb.equal(root.get("airlineCode"), airlineCode.trim()));
            }
            if (hasText(flightNumber)) {
                predicates.add(cb.equal(root.get("flightNumber"), flightNumber.trim()));
            }
            if (hasText(departureStation)) {
                predicates.add(cb.equal(root.get("departureStation"), departureStation.trim()));
            }
            if (hasText(arrivalStation)) {
                predicates.add(cb.equal(root.get("arrivalStation"), arrivalStation.trim()));
            }
            if (hasText(aircraftType)) {
                predicates.add(cb.equal(root.get("aircraftType"), aircraftType.trim()));
            }
            if (hasText(serviceType)) {
                predicates.add(cb.equal(root.get("serviceType"), serviceType.trim()));
            }
            if (hasText(operatingDays)) {
                predicates.add(cb.like(root.get("operatingDays"), "%" + operatingDays.trim() + "%"));
            }

            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    private String normalizeTenantCode(String tenantCode) {
        if (!hasText(tenantCode)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing tenant code");
        }
        return tenantCode.trim().toUpperCase();
    }
}

