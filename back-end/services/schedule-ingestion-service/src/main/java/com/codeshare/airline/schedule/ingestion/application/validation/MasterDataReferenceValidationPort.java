package com.codeshare.airline.schedule.ingestion.application.validation;

public interface MasterDataReferenceValidationPort {

    boolean airlineExists(String airlineCode);

    boolean airportExists(String airportCode);

    boolean aircraftExists(String aircraftCode);

    boolean serviceTypeExists(String serviceTypeCode);

    boolean trafficRestrictionExists(String trafficRestrictionCode);
}
