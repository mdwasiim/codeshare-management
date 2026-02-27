package com.codeshare.airline.schedule.persistence.inbound.entity;

import com.codeshare.airline.persistence.persistence.entity.CSMDataAbstractEntity;
import com.codeshare.airline.schedule.parsing.common.dto.ActionIdentifier;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "SCHEDULE_INBOUND_BLOCK",
        schema = "SCHEDULE_OPERATIONAL"
)
@Getter
@Setter
@NoArgsConstructor
public class ScheduleInboundBlock extends CSMDataAbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "FILE_ID",
            foreignKey = @ForeignKey(name = "FK_SCH_BLOCK_FILE")
    )
    private ScheduleInboundFile inboundFile;

    @Enumerated(EnumType.STRING)
    @Column(name = "ACTION_IDENTIFIER", length = 5, nullable = false)
    private ActionIdentifier actionIdentifier;

    @Column(name = "BLOCK_SEQUENCE", nullable = false)
    private Integer blockSequence;

    @Column(name = "RAW_BLOCK", columnDefinition = "TEXT")
    private String rawBlock;

    @OneToMany(
            mappedBy = "block",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ScheduleInboundFlight> flights = new ArrayList<>();
}