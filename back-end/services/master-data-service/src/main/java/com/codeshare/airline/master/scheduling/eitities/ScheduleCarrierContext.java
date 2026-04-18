package com.codeshare.airline.master.scheduling.eitities;

import com.codeshare.airline.data.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "SCHEDULE_CARRIER_CONTEXT")
public class ScheduleCarrierContext extends CSMDataAbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FLIGHT_ID", nullable = false)
    private ScheduleFlight scheduleFlight;

    @Column(name = "CARRIER_CODE", nullable = false, length = 3)
    private String carrierCode; // QR, BA, AA

    @Column(name = "IS_OPERATING", nullable = false)
    private Boolean isOperating;

    @OneToMany(mappedBy = "carrierContext", cascade = CascadeType.ALL)
    private List<ScheduleDei> deis;
}