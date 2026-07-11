package com.codeshare.airline.master.terminal.entities;

import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import com.codeshare.airline.master.geography.entities.Airport;
import com.codeshare.airline.platform.data.jpa.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "TERMINAL",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_AIRPORT_TERMINAL",
                        columnNames = {"AIRPORT_ID", "TERMINAL_CODE"}
                )
        },
        indexes = {
                @Index(name = "IDX_TERMINAL_AIRPORT", columnList = "AIRPORT_ID"),
                @Index(name = "IDX_TERMINAL_STATUS", columnList = "STATUS_CODE")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class PassengerTerminal extends CSMDataAbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "AIRPORT_ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_TERMINAL_AIRPORT")
    )
    private Airport airport;

    @Column(name = "TERMINAL_CODE", nullable = false, length = 10)
    private String terminalCode;

    @Column(name = "TERMINAL_NAME", length = 100)
    private String terminalName;

    @Column(name = "TERMINAL_TYPE", length = 20)
    private String terminalType; // ARRIVAL / DEPARTURE / BOTH

    @Column(name = "INTERNATIONAL_FLAG")
    private Boolean internationalFlag;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS_CODE", length = 20)
    private RecordStatus status;
}