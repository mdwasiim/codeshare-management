package com.codeshare.airline.tenant.entities.ingestion;

import com.codeshare.airline.platform.data.jpa.entity.CSMDataAbstractEntity;
import com.codeshare.airline.tenant.entities.Tenant;
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
                @UniqueConstraint(name = "uk_tenant_ingestion_profile", columnNames = {"tenant_id"})
        },
        indexes = {
                @Index(name = "idx_ingestion_profile_tenant", columnList = "tenant_id"),
                @Index(name = "idx_ingestion_profile_enabled", columnList = "enabled")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class ScheduleIngestionProfileEntity extends CSMDataAbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tenant_id", nullable = false)
    @NotNull
    private Tenant tenant;

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

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ScheduleIngestionChannelEntity> channels = new ArrayList<>();

    @Version
    @Column(name = "version")
    private Long version;

    public void addChannel(ScheduleIngestionChannelEntity channel) {
        channel.setProfile(this);
        channels.add(channel);
    }
}
