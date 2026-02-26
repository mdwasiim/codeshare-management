package com.codeshare.airline.schedule.source.persistence.entity;

import com.codeshare.airline.persistence.persistence.entity.CSMDataAbstractEntity;
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
        name = "AIRLINE_INGESTION_PROFILE",
        schema = "SCHEDULE_MASTER",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_AIRLINE_PROFILE",
                        columnNames = {"AIRLINE_CODE"}
                )
        },
        indexes = {
                @Index(name = "IDX_PROFILE_AIRLINE", columnList = "AIRLINE_CODE"),
                @Index(name = "IDX_PROFILE_ENABLED", columnList = "ENABLED")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class AirlineIngestionProfile extends CSMDataAbstractEntity {

    @NotBlank
    @Size(min = 2, max = 3)
    @Column(name = "AIRLINE_CODE", length = 3, nullable = false)
    private String airlineCode;

    @NotBlank
    @Size(max = 50)
    @Column(name = "SOURCE_SYSTEM", length = 50, nullable = false)
    private String sourceSystem;

    @NotNull
    @Column(name = "ENABLED", nullable = false)
    private Boolean enabled = Boolean.TRUE;

    @PositiveOrZero
    @Column(name = "POLL_INTERVAL_MS")
    private Long pollIntervalMs;

    @OneToMany(
            mappedBy = "profile",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<AirlineIngestionChannel> channels = new ArrayList<>();

    @Version
    @Column(name = "VERSION")
    private Long version;

    public void addChannel(AirlineIngestionChannel channel) {
        channel.setProfile(this);
        this.channels.add(channel);
    }

    public void removeChannel(AirlineIngestionChannel channel) {
        channel.setProfile(null);
        this.channels.remove(channel);
    }
}