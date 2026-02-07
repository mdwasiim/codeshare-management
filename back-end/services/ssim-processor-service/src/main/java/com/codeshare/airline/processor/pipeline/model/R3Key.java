package com.codeshare.airline.processor.pipeline.model;

import java.util.Objects;

public final class R3Key {

    private final String airline;
    private final String flightNumber;
    private final String suffix;
    private final String itineraryVariation;
    private final String legSequence;

    public R3Key(String airline,
                 String flightNumber,
                 String suffix,
                 String itineraryVariation,
                 String legSequence) {
        this.airline = airline;
        this.flightNumber = flightNumber;
        this.suffix = suffix;
        this.itineraryVariation = itineraryVariation;
        this.legSequence = legSequence;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof R3Key)) return false;
        R3Key that = (R3Key) o;
        return Objects.equals(airline, that.airline)
                && Objects.equals(flightNumber, that.flightNumber)
                && Objects.equals(suffix, that.suffix)
                && Objects.equals(itineraryVariation, that.itineraryVariation)
                && Objects.equals(legSequence, that.legSequence);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                airline,
                flightNumber,
                suffix,
                itineraryVariation,
                legSequence
        );
    }
}
