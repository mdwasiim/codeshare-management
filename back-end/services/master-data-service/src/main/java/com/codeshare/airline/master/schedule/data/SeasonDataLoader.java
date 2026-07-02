package com.codeshare.airline.master.schedule.data;

import com.codeshare.airline.core.enums.common.RecordStatus;
import com.codeshare.airline.core.enums.schedule.SeasonType;
import com.codeshare.airline.master.schedule.entities.Season;
import com.codeshare.airline.master.schedule.repository.SeasonRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class SeasonDataLoader {

    private final SeasonRepository seasonRepository;

    @PostConstruct
    public void load() {

        if (seasonRepository.count() > 0) return;

        Season s25 = new Season();
        s25.setSeasonCode("S25");
        s25.setSeasonName("Summer 2025");
        s25.setSeasonType(SeasonType.SUMMER);
        s25.setScheduleYear(2025);
        s25.setSeasonStartDate(LocalDate.of(2025, 3, 30));
        s25.setSeasonEndDate(LocalDate.of(2025, 10, 25));
        s25.setRecordStatus(RecordStatus.ACTIVE);
        s25.setEffectiveFrom(LocalDate.of(2025, 3, 30));
        s25.setEffectiveTo(LocalDate.of(2025, 10, 25));

        Season w25 = new Season();
        w25.setSeasonCode("W25");
        w25.setSeasonName("Winter 2025");
        w25.setSeasonType(SeasonType.WINTER);
        w25.setScheduleYear(2025);
        w25.setSeasonStartDate(LocalDate.of(2025, 10, 26));
        w25.setSeasonEndDate(LocalDate.of(2026, 3, 28));
        w25.setRecordStatus(RecordStatus.ACTIVE);
        w25.setEffectiveFrom(LocalDate.of(2025, 10, 26));
        w25.setEffectiveTo(LocalDate.of(2026, 3, 28));

        seasonRepository.save(s25);
        seasonRepository.save(w25);
    }
}
