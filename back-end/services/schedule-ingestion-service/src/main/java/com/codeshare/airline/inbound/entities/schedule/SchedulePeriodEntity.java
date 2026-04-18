package com.codeshare.airline.inbound.entities.schedule;

import com.codeshare.airline.data.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(
        name = "schedule_period",
        indexes = {
                @Index(name = "idx_period_flight", columnList = "flight_id"),
                @Index(name = "idx_period_dates", columnList = "start_date,end_date")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class SchedulePeriodEntity extends CSMDataAbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_id", nullable = false)
    private ScheduleFlightEntity flight;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "days_of_operation", length = 7)
    private String daysOfOperation;

    @Column(name = "frequency_rate")
    private Integer frequencyRate;
}