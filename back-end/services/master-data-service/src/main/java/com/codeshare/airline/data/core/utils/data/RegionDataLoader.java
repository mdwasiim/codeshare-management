package com.codeshare.airline.data.core.utils.data;

import com.codeshare.airline.core.enums.common.Status;
import com.codeshare.airline.data.core.eitities.Region;
import com.codeshare.airline.data.core.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RegionDataLoader implements CommandLineRunner {

    private final RegionRepository repository;

    @Override
    public void run(String... args) {

        if (repository.count() > 0) return;

        List<Region> regions = List.of(

                build("EUR", "Europe"),
                build("MEA", "Middle East"),
                build("NAM", "North America"),
                build("APAC", "Asia Pacific"),
                build("AFR", "Africa")
        );

        repository.saveAll(regions);
    }

    private Region build(String code, String name) {

        Region region = new Region();
        region.setRegionCode(code);
        region.setRegionName(name);
        region.setStatusCode(Status.ACTIVE);
        region.setEffectiveFrom(LocalDate.of(2000, 1, 1));

        return region;
    }
}