package com.codeshare.airline.master.aircraft.loader;

import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import com.codeshare.airline.master.aircraft.entities.CabinConfigurationTemplate;
import com.codeshare.airline.platform.core.enums.master.aircraft.CabinConfigurationType;
import com.codeshare.airline.master.aircraft.repository.CabinConfigurationTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@Order(235)
@RequiredArgsConstructor
public class CabinConfigurationTemplateDataLoader implements CommandLineRunner {

    private final CabinConfigurationTemplateRepository repository;

    @Override
    public void run(String... args) {

        if (repository.count() > 0) return;

        List<CabinConfigurationTemplate> templates = List.of(
                build("INTL-3C", "International Three Cabin", CabinConfigurationType.INTERNATIONAL, 1),
                build("INTL-2C", "International Two Cabin", CabinConfigurationType.INTERNATIONAL, 2),
                build("DOM-1C", "Domestic Single Cabin", CabinConfigurationType.DOMESTIC, 3),
                build("HD-ECON", "High Density Economy", CabinConfigurationType.HIGH_DENSITY, 4),
                build("CARGO", "Cargo Configuration", CabinConfigurationType.CARGO, 5)
        );

        repository.saveAll(templates);
    }

    private CabinConfigurationTemplate build(String code,
                                             String name,
                                             CabinConfigurationType type,
                                             int displayOrder) {

        CabinConfigurationTemplate template = new CabinConfigurationTemplate();
        template.setConfigurationCode(code);
        template.setConfigurationName(name);
        template.setConfigurationType(type);
        template.setActive(Boolean.TRUE);
        template.setDisplayOrder(displayOrder);
        template.setRecordStatus(RecordStatus.ACTIVE);
        template.setEffectiveFrom(LocalDate.of(2000, 1, 1));

        return template;
    }
}
