package com.codeshare.airline.master.aircraft.loader;

import com.codeshare.airline.core.enums.common.RecordStatus;
import com.codeshare.airline.master.aircraft.entities.AircraftConfiguration;
import com.codeshare.airline.master.aircraft.entities.AircraftConfigurationRevision;
import com.codeshare.airline.master.aircraft.entities.enums.ConfigurationRevisionStatus;
import com.codeshare.airline.master.aircraft.repository.AircraftConfigurationRepository;
import com.codeshare.airline.master.aircraft.repository.AircraftConfigurationRevisionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@Order(270)
@RequiredArgsConstructor
public class AircraftConfigurationRevisionDataLoader implements CommandLineRunner {

    private final AircraftConfigurationRevisionRepository repository;
    private final AircraftConfigurationRepository configurationRepository;

    @Override
    public void run(String... args) {

        if (repository.count() > 0) return;

        List<AircraftConfigurationRevision> revisions = List.of(
                build(configurationRepository.findByConfigurationCode("QR77W-QSUITE").orElseThrow(),
                        1, "QR77W-R1", "Initial Qsuite Configuration",
                        ConfigurationRevisionStatus.PUBLISHED, LocalDate.of(2020, 1, 1), 1),
                build(configurationRepository.findByConfigurationCode("QR359-283").orElseThrow(),
                        1, "QR359-R1", "Initial A350-900 Configuration",
                        ConfigurationRevisionStatus.PUBLISHED, LocalDate.of(2020, 1, 1), 2),
                build(configurationRepository.findByConfigurationCode("QR320-ALL-ECO").orElseThrow(),
                        1, "QR320-R1", "Initial A320 All Economy Configuration",
                        ConfigurationRevisionStatus.PUBLISHED, LocalDate.of(2020, 1, 1), 3),
                build(configurationRepository.findByConfigurationCode("BA789-3C").orElseThrow(),
                        1, "BA789-R1", "Initial 787-9 Three Cabin Configuration",
                        ConfigurationRevisionStatus.PUBLISHED, LocalDate.of(2020, 1, 1), 4)
        );

        repository.saveAll(revisions);
    }

    private AircraftConfigurationRevision build(AircraftConfiguration configuration,
                                                int revisionNumber,
                                                String revisionCode,
                                                String revisionName,
                                                ConfigurationRevisionStatus revisionStatus,
                                                LocalDate publishedDate,
                                                int displayOrder) {

        AircraftConfigurationRevision revision = new AircraftConfigurationRevision();
        revision.setAircraftConfiguration(configuration);
        revision.setRevisionNumber(revisionNumber);
        revision.setRevisionCode(revisionCode);
        revision.setRevisionName(revisionName);
        revision.setRevisionStatus(revisionStatus);
        revision.setChangeReason("Initial master data load");
        revision.setPublishedDate(publishedDate);
        revision.setPublishedBy("SYSTEM");
        revision.setCurrentRevision(Boolean.TRUE);
        revision.setActive(Boolean.TRUE);
        revision.setDisplayOrder(displayOrder);
        revision.setRecordStatus(RecordStatus.ACTIVE);
        revision.setEffectiveFrom(publishedDate);

        return revision;
    }
}
