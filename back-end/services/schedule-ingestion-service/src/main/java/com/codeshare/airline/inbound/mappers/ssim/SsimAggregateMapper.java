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
            for (SsimFlightDTO flightDTO : context.getFlights()) {
                if (flightExists(carrier, flightDTO)) {
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

    private boolean flightExists(SsimCarrierEntity carrier, SsimFlightDTO dto) {
        if (carrier.getFlights() == null || dto == null) {
            return false;
        }

        return carrier.getFlights().stream().anyMatch(existing ->
                same(existing.getAirlineCode(), dto.getAirlineCode())
                        && same(existing.getFlightNumber(), dto.getFlightNumber())
                        && same(existing.getOperationalSuffix(), dto.getOperationalSuffix())
                        && same(existing.getItineraryVariationIdentifier(), dto.getItineraryVariationIdentifier())
                        && same(existing.getLegSequenceNumber(), dto.getLegSequenceNumber())
        );
    }

    private boolean same(String left, String right) {
        return normalize(left).equals(normalize(right));
    }

    private boolean same(Integer left, Integer right) {
        return left == null ? right == null : left.equals(right);
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }
}
