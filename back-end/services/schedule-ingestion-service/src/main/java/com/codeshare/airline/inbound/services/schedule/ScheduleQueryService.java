package com.codeshare.airline.inbound.services.schedule;

import com.codeshare.airline.core.enums.MessageType;
import com.codeshare.airline.inbound.api.response.ScheduleFileMessageResponse;
import com.codeshare.airline.inbound.domain.enums.ProcessingStatus;
import com.codeshare.airline.inbound.dto.schedule.ScheduleFileMetaDataDTO;
import com.codeshare.airline.inbound.dto.schedule.ScheduleFlightDTO;
import com.codeshare.airline.inbound.dto.schedule.ScheduleMessageDTO;
import com.codeshare.airline.inbound.entities.schedule.ScheduleFileMetaDataEntity;
import com.codeshare.airline.inbound.entities.schedule.ScheduleFlightEntity;
import com.codeshare.airline.inbound.mappers.schedule.ScheduleFileMetaDataMapper;
import com.codeshare.airline.inbound.mappers.schedule.ScheduleFlightMapper;
import com.codeshare.airline.inbound.mappers.schedule.ScheduleMessageMapper;
import com.codeshare.airline.inbound.repositories.schedule.ScheduleFileMetaDataRepository;
import com.codeshare.airline.inbound.repositories.schedule.ScheduleFlightRepository;
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
public class ScheduleQueryService {

    private final ScheduleFileMetaDataRepository fileRepository;
    private final ScheduleFlightRepository flightRepository;
    private final ScheduleFileMetaDataMapper fileMapper;
    private final ScheduleMessageMapper messageMapper;
    private final ScheduleFlightMapper flightMapper;

    public Page<ScheduleFileMetaDataDTO> searchFiles(
            MessageType messageType,
            String airlineCode,
            ProcessingStatus processingStatus,
            Instant receivedFrom,
            Instant receivedTo,
            String fileName,
            String checksum,
            Pageable pageable
    ) {
        assertScheduleType(messageType);
        return fileRepository.findAll(
                fileSpec(messageType, airlineCode, processingStatus, receivedFrom, receivedTo, fileName, checksum),
                pageable
        ).map(fileMapper::toDto);
    }

    public ScheduleFileMetaDataDTO getFile(MessageType messageType, UUID fileId) {
        return fileMapper.toDto(findFile(messageType, fileId));
    }

    public ScheduleFileMessageResponse getParsedSchedule(MessageType messageType, UUID fileId) {
        ScheduleFileMetaDataEntity file = findFile(messageType, fileId);
        List<ScheduleMessageDTO> messages = file.getSafeEnvelopes()
                .stream()
                .map(messageMapper::toDTO)
                .toList();

        return ScheduleFileMessageResponse.builder()
                .file(fileMapper.toDto(file))
                .messages(messages)
                .build();
    }

    public Page<ScheduleFlightDTO> searchFlights(
            MessageType messageType,
            UUID fileId,
            String airlineCode,
            String flightNumber,
            String origin,
            String destination,
            String aircraftType,
            String serviceType,
            Pageable pageable
    ) {
        assertScheduleType(messageType);
        return flightRepository.findAll(
                flightSpec(messageType, fileId, airlineCode, flightNumber, origin, destination, aircraftType, serviceType),
                pageable
        ).map(flightMapper::toDTO);
    }

    public ScheduleFlightDTO getFlight(MessageType messageType, UUID flightId) {
        assertScheduleType(messageType);
        ScheduleFlightEntity flight = flightRepository.findById(flightId)
                .filter(entity -> entity.getSubMessage() != null
                        && entity.getSubMessage().getMessage() != null
                        && entity.getSubMessage().getMessage().getFile() != null
                        && messageType.equals(entity.getSubMessage().getMessage().getFile().getMessageType()))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        messageType + " flight not found: " + flightId
                ));

        return flightMapper.toDTO(flight);
    }

    private ScheduleFileMetaDataEntity findFile(MessageType messageType, UUID fileId) {
        assertScheduleType(messageType);
        return fileRepository.findByFileId(fileId)
                .filter(file -> messageType.equals(file.getMessageType()))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        messageType + " file not found: " + fileId
                ));
    }

    private Specification<ScheduleFileMetaDataEntity> fileSpec(
            MessageType messageType,
            String airlineCode,
            ProcessingStatus processingStatus,
            Instant receivedFrom,
            Instant receivedTo,
            String fileName,
            String checksum
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.equal(root.get("messageType"), messageType));
            if (hasText(airlineCode)) {
                predicates.add(cb.equal(root.get("airlineCode"), airlineCode.trim()));
            }
            if (processingStatus != null) {
                predicates.add(cb.equal(root.get("processingStatus"), processingStatus));
            }
            if (receivedFrom != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("receivedAt"), receivedFrom));
            }
            if (receivedTo != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("receivedAt"), receivedTo));
            }
            if (hasText(fileName)) {
                predicates.add(cb.like(cb.lower(root.get("fileName")), "%" + fileName.trim().toLowerCase() + "%"));
            }
            if (hasText(checksum)) {
                predicates.add(cb.equal(root.get("checksum"), checksum.trim()));
            }

            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }

    private Specification<ScheduleFlightEntity> flightSpec(
            MessageType messageType,
            UUID fileId,
            String airlineCode,
            String flightNumber,
            String origin,
            String destination,
            String aircraftType,
            String serviceType
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            Join<Object, Object> subMessage = root.join("subMessage");
            Join<Object, Object> message = subMessage.join("message");
            Join<Object, Object> file = message.join("file");

            predicates.add(cb.equal(file.get("messageType"), messageType));
            if (fileId != null) {
                predicates.add(cb.equal(file.get("fileId"), fileId));
            }
            if (hasText(airlineCode)) {
                predicates.add(cb.equal(root.get("carrier"), airlineCode.trim()));
            }
            if (hasText(flightNumber)) {
                predicates.add(cb.equal(root.get("flightNumber"), flightNumber.trim()));
            }
            if (hasText(origin)) {
                Join<Object, Object> leg = root.join("legs");
                predicates.add(cb.equal(leg.get("origin"), origin.trim()));
            }
            if (hasText(destination)) {
                Join<Object, Object> leg = root.join("legs");
                predicates.add(cb.equal(leg.get("destination"), destination.trim()));
            }
            if (hasText(aircraftType)) {
                predicates.add(cb.equal(root.get("aircraftType"), aircraftType.trim()));
            }
            if (hasText(serviceType)) {
                predicates.add(cb.equal(root.get("serviceType"), serviceType.trim()));
            }

            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }

    private void assertScheduleType(MessageType messageType) {
        if (messageType != MessageType.ASM && messageType != MessageType.SSM) {
            throw new IllegalArgumentException("Only ASM and SSM schedules are supported here");
        }
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
