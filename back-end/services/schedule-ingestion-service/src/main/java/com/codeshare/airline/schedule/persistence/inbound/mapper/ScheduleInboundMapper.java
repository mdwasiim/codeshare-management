package com.codeshare.airline.schedule.persistence.inbound.mapper;

import com.codeshare.airline.schedule.parsing.asm.dto.AsmInboundMessage;
import com.codeshare.airline.schedule.parsing.ssm.dto.SsmInboundMessage;
import com.codeshare.airline.schedule.persistence.inbound.entity.*;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ScheduleInboundMapper {

    /* ======================================================
       ASM Mapping
       ====================================================== */

    public ScheduleInboundBlock mapAsmBlock(
            AsmInboundMessage dto,
            ScheduleInboundFile file,
            int blockSeq) {

        ScheduleInboundBlock block = new ScheduleInboundBlock();
        block.setInboundFile(file);
        block.setActionIdentifier(dto.getActionIdentifier());
        block.setBlockSequence(blockSeq);

        ScheduleInboundFlight flight =
                mapAsmFlight(dto, block);

        block.getFlights().add(flight);

        return block;
    }

    private ScheduleInboundFlight mapAsmFlight(
            AsmInboundMessage dto,
            ScheduleInboundBlock block) {

        ScheduleInboundFlight flight =
                new ScheduleInboundFlight();

        flight.setBlock(block);
        flight.setCarrier(dto.getCarrier());
        flight.setFlightNumber(dto.getFlightNumber());
        flight.setSuffix(dto.getSuffix());

        flight.setPeriodFrom(dto.getPeriodFrom());
        flight.setPeriodTo(dto.getPeriodTo());
        flight.setDaysOfOperation(dto.getDaysOfOperation());
        flight.setAircraftType(dto.getAircraftType());

        AtomicInteger legSeq = new AtomicInteger(1);

        dto.getLegs().forEach(l -> {
            ScheduleInboundLeg leg = new ScheduleInboundLeg();
            leg.setFlight(flight);
            leg.setLegSequence(legSeq.getAndIncrement());
            leg.setOrigin(l.getOrigin());
            leg.setDestination(l.getDestination());
            leg.setDepartureTime(l.getDepartureTime());
            leg.setArrivalTime(l.getArrivalTime());
            flight.getLegs().add(leg);
        });

        dto.getDeis().forEach(d -> {
            ScheduleInboundDei dei = new ScheduleInboundDei();
            dei.setFlight(flight);
            dei.setDeiCode(d.getDeiCode());
            dei.setValue(d.getValue());
            flight.getDeis().add(dei);
        });

        return flight;
    }

    /* ======================================================
       SSM Mapping
       ====================================================== */

    public ScheduleInboundBlock mapSsmBlock(
            SsmInboundMessage dto,
            ScheduleInboundFile file,
            int blockSeq) {

        ScheduleInboundBlock block = new ScheduleInboundBlock();
        block.setInboundFile(file);
        block.setActionIdentifier(dto.getActionIdentifier());
        block.setBlockSequence(blockSeq);

        ScheduleInboundFlight flight =
                mapSsmFlight(dto, block);

        block.getFlights().add(flight);

        return block;
    }

    private ScheduleInboundFlight mapSsmFlight(
            SsmInboundMessage dto,
            ScheduleInboundBlock block) {

        ScheduleInboundFlight flight =
                new ScheduleInboundFlight();

        flight.setBlock(block);
        flight.setCarrier(dto.getCarrier());
        flight.setFlightNumber(dto.getFlightNumber());
        flight.setSuffix(dto.getSuffix());

        flight.setOperationDate(dto.getOperationDate());
        flight.setAircraftType(dto.getAircraftType());

        AtomicInteger legSeq = new AtomicInteger(1);

        dto.getLegs().forEach(l -> {
            ScheduleInboundLeg leg = new ScheduleInboundLeg();
            leg.setFlight(flight);
            leg.setLegSequence(legSeq.getAndIncrement());
            leg.setOrigin(l.getOrigin());
            leg.setDestination(l.getDestination());
            leg.setDepartureTime(l.getDepartureTime());
            leg.setArrivalTime(l.getArrivalTime());
            flight.getLegs().add(leg);
        });

        dto.getDeis().forEach(d -> {
            ScheduleInboundDei dei = new ScheduleInboundDei();
            dei.setFlight(flight);
            dei.setDeiCode(d.getDeiCode());
            dei.setValue(d.getValue());
            flight.getDeis().add(dei);
        });

        return flight;
    }
}