package com.codeshare.airline.schedule.persistence.inbound.entity;

import com.codeshare.airline.persistence.persistence.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "SCHEDULE_INBOUND_DEI",
        schema = "SCHEDULE_OPERATIONAL"
)
@Getter
@Setter
@NoArgsConstructor
public class ScheduleInboundDei extends CSMDataAbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "FLIGHT_ID",
            foreignKey = @ForeignKey(name = "FK_SCH_DEI_FLIGHT")
    )
    private ScheduleInboundFlight flight;

    @Column(name = "DEI_CODE", nullable = false)
    private Integer deiCode;

    @Column(name = "DEI_VALUE", length = 500)
    private String value;
}