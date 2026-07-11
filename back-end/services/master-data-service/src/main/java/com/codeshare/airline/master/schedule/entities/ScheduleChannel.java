package com.codeshare.airline.master.schedule.entities;

import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import com.codeshare.airline.platform.data.jpa.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity(name = "Schedule_Channel")
@Table(
        name = "SCHEDULE_CHANNEL",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_SCHEDULE_CHANNEL", columnNames = "CHANNEL_CODE")
        },
        indexes = {
                @Index(name = "IDX_SCHEDULE_CHANNEL_STATUS", columnList = "STATUS")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class ScheduleChannel extends CSMDataAbstractEntity {
    @Column(name = "CHANNEL_CODE", nullable = false, length = 20)
    private String channelCode;

    @Column(name = "CHANNEL_NAME", nullable = false, length = 100)
    private String channelName;


    @Column(name = "DESCRIPTION", length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false, length = 20)
    private RecordStatus recordStatus;

    @Column(name = "EFFECTIVE_FROM")
    private LocalDate effectiveFrom;

    @Column(name = "EFFECTIVE_TO")
    private LocalDate effectiveTo;

    @PrePersist
    @PreUpdate
    private void normalizeAndValidate() {
        if (channelCode != null) {
            channelCode = channelCode.trim().toUpperCase();
        }

        if (channelName != null) {
            channelName = channelName.trim();
        }

        if (effectiveFrom != null && effectiveTo != null &&
                effectiveFrom.isAfter(effectiveTo)) {
            throw new IllegalStateException("Invalid effective period.");
        }
    }
}