package com.codeshare.airline.schedule.live.domain.entity;

import com.codeshare.airline.platform.core.enums.schedule.DeiCategory;
import com.codeshare.airline.platform.data.jpa.entity.CSMDataAbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(
        name = "live_leg_dei",
        schema = "schedule_live",
        indexes = {
                @Index(name = "idx_lld_leg", columnList = "flight_leg_id"),
                @Index(name = "idx_lld_dei_code", columnList = "data_element_identifier"),
                @Index(name = "idx_lld_category", columnList = "dei_category"),
                @Index(name = "idx_lld_leg_code", columnList = "flight_leg_id, data_element_identifier")
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_live_leg_dei",
                        columnNames = {"flight_leg_id", "data_element_identifier", "sequence_order"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class LiveLegDataElementEntity extends CSMDataAbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "flight_leg_id",
            nullable = false,
            updatable = false,
            foreignKey = @ForeignKey(name = "fk_lld_flight_leg")
    )
    private LiveFlightLegEntity flightLeg;

    @Column(name = "data_element_identifier", length = 3, nullable = false)
    private String dataElementIdentifier;

    @Enumerated(EnumType.STRING)
    @Column(name = "dei_category", length = 20)
    private DeiCategory deiCategory;

    @Column(name = "dei_data", length = 155)
    private String deiData;

    @Column(name = "sequence_order", nullable = false)
    private Integer sequenceOrder;
}
