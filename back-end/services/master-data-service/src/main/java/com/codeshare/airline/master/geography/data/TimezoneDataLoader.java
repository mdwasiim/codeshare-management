package com.codeshare.airline.master.geography.data;

import com.codeshare.airline.core.enums.common.RecordStatus;
import com.codeshare.airline.master.geography.entities.Timezone;
import com.codeshare.airline.master.geography.repository.TimezoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TimezoneDataLoader implements CommandLineRunner {

    private final TimezoneRepository repository;

    @Override
    public void run(String... args) {

        if (repository.count() > 0) return;

        List<Timezone> zones = List.of(

                build("Asia/Qatar"),
                build("Europe/London"),
                build("America/New_York"),
                build("Asia/Dubai"),
                build("Asia/Kolkata")
        );

        repository.saveAll(zones);
    }

    private Timezone build(String identifier) {

        Timezone tz = new Timezone();
        tz.setTzIdentifier(identifier);
        tz.setRecordStatus(RecordStatus.ACTIVE);

        return tz;
    }
}