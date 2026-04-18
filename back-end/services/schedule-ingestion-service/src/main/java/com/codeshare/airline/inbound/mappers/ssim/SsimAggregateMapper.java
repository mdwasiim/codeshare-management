package com.codeshare.airline.inbound.mappers.ssim;

import com.codeshare.airline.inbound.dto.common.ssim.SsimDataElementDTO;
import com.codeshare.airline.inbound.dto.common.ssim.SsimFlightDTO;
import com.codeshare.airline.inbound.dto.ssim.SSIMMessageDTO;
import com.codeshare.airline.inbound.entities.ssim.*;
import com.codeshare.airline.ingestion.persistence.entities.ssim.*;
import com.codeshare.airline.persistence.entities.ssim.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SsimAggregateMapper {

    private final SsimFileMetaDataMapper metaDataMapper;
    private final SsimHeaderMapper headerMapper;
    private final SsimCarrierMapper carrierMapper;
    private final SsimFlightMapper flightMapper;
    private final SsimDataElementMapper deiMapper;
    private final SsimTrailerMapper trailerMapper;

    public SsimFileMetaDataEntity toEntity(SSIMMessageDTO context,SsimFileMetaDataEntity fileMetaDataEntity) {

        if (context == null || fileMetaDataEntity == null) return null;

        /* ================= T1: HEADER ================= */

        if (context.getHeader() != null) {
            SsimHeaderEntity header =   headerMapper.toEntity(context.getHeader());
            fileMetaDataEntity.setHeader(header);
            header.setFile(fileMetaDataEntity); // 🔥 IMPORTANT
        }

        /* ================= T2: CARRIER ================= */

        SsimCarrierEntity carrier = null;

        if (context.getCarrier() != null) {
            carrier = carrierMapper.toEntity(context.getCarrier());
            fileMetaDataEntity.setCarrier(carrier);
            carrier.setFile(fileMetaDataEntity);
        }

        /* ================= T3 + T4: FLIGHTS + DEI ================= */

        if (carrier != null && context.getFlights() != null) {

            for (SsimFlightDTO flightDTO : context.getFlights()) {

                SsimFlightEntity flight = flightMapper.toEntity(flightDTO, carrier);
                /* ===== DEI ===== */
                if (flightDTO.getDeis() != null) {
                    for (SsimDataElementDTO deiDTO : flightDTO.getDeis()) {
                        SsimDataElementEntity dei = deiMapper.toEntity(deiDTO, flight);
                        flight.getDeis().add(dei);
                    }
                }
                carrier.addFlight(flight);
            }
        }

        /* ================= T5: TRAILER ================= */

        if (context.getTrailer() != null) {
            SsimTrailerEntity trailer = trailerMapper.toEntity(context.getTrailer());
            fileMetaDataEntity.setTrailer(trailer);
            trailer.setFile(fileMetaDataEntity);

        }

        return fileMetaDataEntity;
    }
}