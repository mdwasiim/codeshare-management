package com.codeshare.airline.master.schedule.eitities;

import com.codeshare.airline.data.entity.CSMDataAbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(name = "SCHEDULE",
        indexes = {
                @Index(name = "IDX_SCHEDULE_SOURCE", columnList = "SOURCE_TYPE"),
                @Index(name = "IDX_SCHEDULE_SEASON", columnList = "SEASON")
        })
public class Schedule extends CSMDataAbstractEntity {

    @Column(name = "SOURCE_TYPE", nullable = false, length = 20)
    private String sourceType; // SSM / ASM / SSIM_DATASET

    @Column(name = "SEASON", length = 10)
    private String season;

    @Column(name = "TIME_MODE", length = 10)
    private String timeMode; // UTC / LOCAL

    @Column(name = "VERSION_NUMBER")
    private Integer versionNumber;

    @Column(name = "STATUS", length = 20)
    private String status;
}