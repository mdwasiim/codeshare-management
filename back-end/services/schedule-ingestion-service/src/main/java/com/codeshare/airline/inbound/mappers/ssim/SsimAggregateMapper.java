package com.codeshare.airline.inbound.mappers.ssim;

import com.codeshare.airline.inbound.dto.common.ssim.SsimFlightDTO;
import com.codeshare.airline.inbound.dto.ssim.SSIMMessageDTO;
import com.codeshare.airline.inbound.entities.ssim.SsimCarrierEntity;
import com.codeshare.airline.inbound.entities.ssim.SsimFileMetaDataEntity;
import com.codeshare.airline.inbound.entities.ssim.SsimFlightEntity;
import com.codeshare.airline.inbound.entities.ssim.SsimHeaderEntity;
import com.codeshare.airline.inbound.entities.ssim.SsimTrailerEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SsimAggregateMapper {

    private final SsimHeaderMapper headerMapper;
    private final SsimCarrierMapper carrierMapper;
    private final SsimFlightMapper flightMapper;
    private final SsimTrailerMapper trailerMapper;

    public SsimFileMetaDataEntity toEntity(SSIMMessageDTO context, SsimFileMetaDataEntity fileMetaDataEntity) {

        if (context == null || fileMetaDataEntity == null) {
            return null;
        }

        if (context.getHeader() != null && fileMetaDataEntity.getHeader() == null) {
            SsimHeaderEntity header = headerMapper.toEntity(context.getHeader());
            fileMetaDataEntity.setHeader(header);
            header.setFile(fileMetaDataEntity);
        }

        SsimCarrierEntity carrier = fileMetaDataEntity.getCarrier();
        if (carrier == null && context.getCarrier() != null) {
            carrier = carrierMapper.toEntity(context.getCarrier());
            fileMetaDataEntity.setCarrier(carrier);
            carrier.setFile(fileMetaDataEntity);
        }

        if (carrier != null && context.getFlights() != null) {
            Set<String> existingFlightKeys = carrier.getFlights() == null
                    ? new HashSet<>()
                    : carrier.getFlights().stream()
                    .map(this::flightKey)
                    .collect(Collectors.toSet());
            for (SsimFlightDTO flightDTO : context.getFlights()) {
                if (!existingFlightKeys.add(flightKey(flightDTO))) {
                    continue;
                }
                SsimFlightEntity flight = flightMapper.toEntity(flightDTO, carrier);
                carrier.addFlight(flight);
            }
        }

        if (context.getTrailer() != null && fileMetaDataEntity.getTrailer() == null) {
            SsimTrailerEntity trailer = trailerMapper.toEntity(context.getTrailer());
            fileMetaDataEntity.setTrailer(trailer);
            trailer.setFile(fileMetaDataEntity);
        }

        return fileMetaDataEntity;
    }

    private String flightKey(SsimFlightEntity flight) {
        if (flight == null) {
            return "";
        }
        return String.join("|",
                normalize(flight.getAirlineCode()),
                normalize(flight.getFlightNumber()),
                normalize(flight.getOperationalSuffix()),
                normalize(flight.getItineraryVariationIdentifier()),
                normalize(flight.getLegSequenceNumber())
        );
    }

    private String flightKey(SsimFlightDTO flight) {
        if (flight == null) {
            return "";
        }
        return String.join("|",
                normalize(flight.getAirlineCode()),
                normalize(flight.getFlightNumber()),
                normalize(flight.getOperationalSuffix()),
                normalize(flight.getItineraryVariationIdentifier()),
                normalize(flight.getLegSequenceNumber())
        );
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }

    private String normalize(Integer value) {
        return value == null ? "" : value.toString();
    }
}
