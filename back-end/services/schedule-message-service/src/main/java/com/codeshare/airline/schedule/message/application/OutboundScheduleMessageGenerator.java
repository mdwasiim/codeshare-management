package com.codeshare.airline.schedule.message.application;

import com.codeshare.airline.platform.core.dto.schedule.workflow.ChangeSetDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ScheduleFlightChangeDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ScheduleLegChangeDTO;
import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.StringJoiner;

@Component
public class OutboundScheduleMessageGenerator {

    public String generate(ChangeSetDTO changeSet) {
        if (changeSet.getMessageType() == MessageType.SSIM) {
            return generateSsim(changeSet);
        }
        if (changeSet.getMessageType() == MessageType.ASM) {
            return generateActionMessage("ASM", changeSet);
        }
        return generateActionMessage("SSM", changeSet);
    }

    private String generateActionMessage(String messageType, ChangeSetDTO changeSet) {
        StringJoiner joiner = new StringJoiner(System.lineSeparator());
        joiner.add(messageType);
        joiner.add("AIRLINE/" + safe(changeSet.getAirlineCode()));
        joiner.add("PARTNER/" + safe(changeSet.getPartnerCode()));
        joiner.add("CHANGESET/" + changeSet.getChangeSetId());

        for (ScheduleFlightChangeDTO flightChange : changeSet.getFlightChanges()) {
            for (ScheduleLegChangeDTO legChange : flightChange.getLegChanges()) {
                joiner.add(
                        safe(legChange.getChangeType() != null ? legChange.getChangeType().name() : "CHG")
                                + " "
                                + safe(flightChange.getAirlineCode())
                                + safe(flightChange.getFlightNumber())
                                + safe(flightChange.getOperationalSuffix())
                                + " "
                                + safe(legChange.getNewValue() != null ? legChange.getNewValue().getDepartureStation() : null)
                                + safe(legChange.getNewValue() != null ? legChange.getNewValue().getArrivalStation() : null)
                );
            }
        }
        return joiner.toString();
    }

    private String generateSsim(ChangeSetDTO changeSet) {
        StringJoiner joiner = new StringJoiner(System.lineSeparator());
        joiner.add("SSIM");
        joiner.add("AIRLINE/" + safe(changeSet.getAirlineCode()));
        joiner.add("PARTNER/" + safe(changeSet.getPartnerCode()));
        joiner.add("CHANGESET/" + changeSet.getChangeSetId());
        joiner.add("CREATED/" + (changeSet.getCreatedAt() != null
                ? DateTimeFormatter.ISO_INSTANT.format(changeSet.getCreatedAt())
                : ""));

        for (ScheduleFlightChangeDTO flightChange : changeSet.getFlightChanges()) {
            joiner.add(
                    "FLIGHT "
                            + safe(flightChange.getAirlineCode())
                            + safe(flightChange.getFlightNumber())
                            + safe(flightChange.getOperationalSuffix())
                            + " LEGS/"
                            + flightChange.getLegChanges().size()
            );
        }
        return joiner.toString();
    }

    private String safe(String value) {
        return value == null ? "" : value.trim();
    }
}
