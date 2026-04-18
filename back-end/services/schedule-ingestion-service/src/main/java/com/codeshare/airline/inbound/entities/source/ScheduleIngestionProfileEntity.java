package com.codeshare.airline.inbound.entities.source;

import com.codeshare.airline.persistence.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "schedule_ingestion_profile",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_airline_profile",
                        columnNames = {"airline_code"}
                )
        },
        indexes = {
                @Index(name = "idx_profile_airline", columnList = "airline_code"),
                @Index(name = "idx_profile_enabled", columnList = "enabled")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class ScheduleIngestionProfileEntity extends CSMDataAbstractEntity {

    @NotBlank
    @Size(min = 2, max = 3)
    @Column(name = "airline_code", length = 3, nullable = false)
    private String airlineCode;

    @NotBlank
    @Size(max = 50)
    @Column(name = "source_system", length = 50, nullable = false)
    private String sourceSystem;

    @NotNull
    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

    @PositiveOrZero
    @Column(name = "poll_interval_ms")
    private Long pollIntervalMs;

    @OneToMany(
            mappedBy = "profile",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<ScheduleIngestionChannelEntity> channels = new ArrayList<>();

    @Version
    @Column(name = "version")
    private Long version;

    public void addChannel(ScheduleIngestionChannelEntity channel) {
        channel.setProfile(this);
        this.channels.add(channel);
    }
}