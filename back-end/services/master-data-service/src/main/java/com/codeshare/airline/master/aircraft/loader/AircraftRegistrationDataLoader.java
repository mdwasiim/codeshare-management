package com.codeshare.airline.master.aircraft.loader;

import com.codeshare.airline.core.enums.common.RecordStatus;
import com.codeshare.airline.master.aircraft.entities.AircraftConfiguration;
import com.codeshare.airline.master.aircraft.entities.AircraftOwner;
import com.codeshare.airline.master.aircraft.entities.AircraftRegistration;
import com.codeshare.airline.master.aircraft.entities.AircraftType;
import com.codeshare.airline.core.enums.master.aircraft.AircraftRegistrationStatus;
import com.codeshare.airline.master.aircraft.repository.AircraftConfigurationRepository;
import com.codeshare.airline.master.aircraft.repository.AircraftOwnerRepository;
import com.codeshare.airline.master.aircraft.repository.AircraftRegistrationRepository;
import com.codeshare.airline.master.aircraft.repository.AircraftTypeRepository;
import com.codeshare.airline.master.airlines.entities.AirlineCarrier;
import com.codeshare.airline.master.airlines.repository.AirlineCarrierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@Order(280)
@RequiredArgsConstructor
public class AircraftRegistrationDataLoader implements CommandLineRunner {

    private final AircraftRegistrationRepository repository;
    private final AircraftTypeRepository aircraftTypeRepository;
    private final AircraftConfigurationRepository configurationRepository;
    private final AircraftOwnerRepository ownerRepository;
    private final AirlineCarrierRepository airlineCarrierRepository;

    @Override
    public void run(String... args) {

        if (repository.count() > 0) return;

        AirlineCarrier qr = airlineCarrierRepository.findByIataCode("QR").orElseThrow();
        AirlineCarrier ba = airlineCarrierRepository.findByIataCode("BA").orElseThrow();
        AircraftOwner qrOwner = ownerRepository.findByOwnerCode("QR").orElseThrow();
        AircraftOwner baOwner = ownerRepository.findByOwnerCode("BA").orElseThrow();

        List<AircraftRegistration> registrations = List.of(
                build("A7-BAA", "Qatar Airways B777-300ER A7-BAA",
                        aircraftTypeRepository.findByIataCode("77W").orElseThrow(),
                        configurationRepository.findByConfigurationCode("QR77W-QSUITE").orElseThrow(),
                        qrOwner, qr, "36100", "801", LocalDate.of(2008, 2, 15),
                        LocalDate.of(2008, 3, 20), 1),
                build("A7-ALZ", "Qatar Airways A350-900 A7-ALZ",
                        aircraftTypeRepository.findByIataCode("359").orElseThrow(),
                        configurationRepository.findByConfigurationCode("QR359-283").orElseThrow(),
                        qrOwner, qr, "172", "172", LocalDate.of(2017, 6, 1),
                        LocalDate.of(2017, 7, 20), 2),
                build("A7-AHU", "Qatar Airways A320 A7-AHU",
                        aircraftTypeRepository.findByIataCode("320").orElseThrow(),
                        configurationRepository.findByConfigurationCode("QR320-ALL-ECO").orElseThrow(),
                        qrOwner, qr, "4489", "4489", LocalDate.of(2011, 2, 10),
                        LocalDate.of(2011, 3, 15), 3),
                build("G-ZBKA", "British Airways B787-9 G-ZBKA",
                        aircraftTypeRepository.findByIataCode("789").orElseThrow(),
                        configurationRepository.findByConfigurationCode("BA789-3C").orElseThrow(),
                        baOwner, ba, "38616", "423", LocalDate.of(2015, 9, 1),
                        LocalDate.of(2015, 9, 30), 4)
        );

        repository.saveAll(registrations);
    }

    private AircraftRegistration build(String registrationNumber,
                                       String registrationName,
                                       AircraftType aircraftType,
                                       AircraftConfiguration configuration,
                                       AircraftOwner owner,
                                       AirlineCarrier operator,
                                       String manufacturerSerialNumber,
                                       String lineNumber,
                                       LocalDate manufactureDate,
                                       LocalDate deliveryDate,
                                       int displayOrder) {

        AircraftRegistration registration = new AircraftRegistration();
        registration.setRegistrationNumber(registrationNumber);
        registration.setRegistrationName(registrationName);
        registration.setAircraftType(aircraftType);
        registration.setAircraftConfiguration(configuration);
        registration.setAircraftOwner(owner);
        registration.setOperatorAirline(operator);
        registration.setManufacturerSerialNumber(manufacturerSerialNumber);
        registration.setLineNumber(lineNumber);
        registration.setManufactureDate(manufactureDate);
        registration.setDeliveryDate(deliveryDate);
        registration.setRegistrationStatus(AircraftRegistrationStatus.ACTIVE);
        registration.setActive(Boolean.TRUE);
        registration.setDisplayOrder(displayOrder);
        registration.setRecordStatus(RecordStatus.ACTIVE);
        registration.setEffectiveFrom(deliveryDate);

        return registration;
    }
}
