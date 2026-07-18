package com.codeshare.airline.schedule.processing.domain.entity;

import com.codeshare.airline.platform.data.jpa.entity.CSMDataAbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(
        name = "schedule_processing_business_error",
        schema = "schedule_processing",
        indexes = {
                @Index(name = "idx_spbe_processing_job", columnList = "processing_job_id"),
                @Index(name = "idx_spbe_rule_code", columnList = "rule_code"),
                @Index(name = "idx_spbe_record_type", columnList = "record_type")
        }
)
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class ProcessingBusinessErrorEntity extends CSMDataAbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "processing_job_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_spbe_processing_job")
    )
    private ProcessingJobEntity processingJob;

    @Column(name = "rule_code", length = 50, nullable = false)
    private String ruleCode;

    @Column(name = "message", columnDefinition = "TEXT", nullable = false)
    private String message;

    @Column(name = "record_type", length = 50)
    private String recordType;

    @Column(name = "record_key", length = 255)
    private String recordKey;
}
