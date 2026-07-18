package com.codeshare.airline.tools.ssim.master.model;

import java.util.Set;
import java.util.TreeSet;

public final class MasterCodeSet {
    public final Set<String> airlines = new TreeSet<>();
    public final Set<String> airports = new TreeSet<>();
    public final Set<String> terminals = new TreeSet<>();
    public final Set<String> aircraftTypes = new TreeSet<>();
    public final Set<String> aircraftOwners = new TreeSet<>();
    public final Set<String> cockpitCrewEmployers = new TreeSet<>();
    public final Set<String> cabinCrewEmployers = new TreeSet<>();
    public final Set<String> serviceTypes = new TreeSet<>();
    public final Set<String> reservationBookingDesignators = new TreeSet<>();
    public final Set<String> reservationBookingModifiers = new TreeSet<>();
    public final Set<String> mealServices = new TreeSet<>();
    public final Set<String> secureFlightIndicators = new TreeSet<>();
    public final Set<String> operationalSuffixes = new TreeSet<>();
    public final Set<String> flightFrequencies = new TreeSet<>();
    public final Set<String> trafficRestrictionCodes = new TreeSet<>();
    public final Set<String> dataElementIdentifiers = new TreeSet<>();
    public final Set<String> seasons = new TreeSet<>();
    public final Set<String> timeModes = new TreeSet<>();

    public String summary() {
        return """
                SSIM master extraction summary:
                  airlines=%d, airports=%d, terminals=%d, aircraftTypes=%d
                  aircraftOwners=%d, cockpitEmployers=%d, cabinEmployers=%d
                  serviceTypes=%d, bookingDesignators=%d, bookingModifiers=%d, meals=%d
                  secureFlightIndicators=%d, operationalSuffixes=%d, frequencies=%d
                  trafficRestrictions=%d, dataElementIdentifiers=%d, seasons=%d, timeModes=%d
                """.formatted(
                airlines.size(), airports.size(), terminals.size(), aircraftTypes.size(),
                aircraftOwners.size(), cockpitCrewEmployers.size(), cabinCrewEmployers.size(),
                serviceTypes.size(), reservationBookingDesignators.size(), reservationBookingModifiers.size(), mealServices.size(),
                secureFlightIndicators.size(), operationalSuffixes.size(), flightFrequencies.size(),
                trafficRestrictionCodes.size(), dataElementIdentifiers.size(), seasons.size(), timeModes.size()
        );
    }
}
