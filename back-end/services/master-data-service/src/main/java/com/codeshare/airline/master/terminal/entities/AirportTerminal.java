package com.codeshare.airline.master.terminal.entities;

import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import com.codeshare.airline.platform.data.jpa.entity.CSMDataAbstractEntity;
import com.codeshare.airline.master.geography.entities.Airport;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity(name = "Airport_Terminal")
@Table(
        name = "AIRPORT_TERMINAL",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_AIRPORT_TERMINAL", columnNames = "TERMINAL_CODE")
        },
        indexes = {
                @Index(name = "IDX_AIRPORT_TERMINAL_STATUS", columnList = "STATUS")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class AirportTerminal extends CSMDataAbstractEntity {
    @Column(name = "TERMINAL_CODE", nullable = false, length = 10)
    private String terminalCode;

    @Column(name = "TERMINAL_NAME", nullable = false, length = 100)
    private String terminalName;

    @Column(name = "IATA_TERMINAL_CODE", length = 5)
    private String iataTerminalCode;


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
        if (terminalCode != null) {
            terminalCode = terminalCode.trim().toUpperCase();
        }

        if (terminalName != null) {
            terminalName = terminalName.trim();
        }

        if (effectiveFrom != null && effectiveTo != null &&
                effectiveFrom.isAfter(effectiveTo)) {
            throw new IllegalStateException("Invalid effective period.");
        }
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "AIRPORT_ID",
            foreignKey = @ForeignKey(name = "FK_AIRPORT_TERMINAL_AIRPORT")
    )
    private Airport airport;
}