package com.codeshare.airline.schedule.ingestion.persistence.entities.ssim;

import com.codeshare.airline.schedule.ingestion.domain.enums.TimeMode;
import com.codeshare.airline.schedule.ingestion.converter.TimeModeCodeConverter;
import com.codeshare.airline.platform.data.jpa.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "ssim_carrier",
        indexes = {
                @Index(name = "idx_ssim_carrier_airline", columnList = "airline_code")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_ssim_carrier_file", columnNames = "file_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class SsimCarrierEntity extends CSMDataAbstractEntity {

    /* =======================================================
       RELATIONSHIP
       ======================================================= */

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "file_id",
            nullable = false,
            updatable = false,
            foreignKey = @ForeignKey(name = "fk_ssim_carrier_file")
    )
    private SsimFileMetaDataEntity file;

    @OneToMany(
            mappedBy = "carrier",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @OrderBy("flightNumber ASC")
    @BatchSize(size = 100)
    private List<SsimFlightEntity> flights = new ArrayList<>();

    /* =======================================================
       SSIM RECORD TYPE 2 (T2) â€“ 200 BYTES
       ======================================================= */

    // SSIM T2: Byte 1
    @Column(name = "record_type", length = 1, nullable = false)
    private String recordType;

    // SSIM T2: Byte 2
    @Convert(converter = TimeModeCodeConverter.class)
    @Column(name = "time_mode", length = 1, nullable = false)
    private TimeMode timeMode;

    // SSIM T2: Bytes 3â€“5
    @Column(name = "airline_code", length = 3, nullable = false)
    private String airlineCode;

    // SSIM T2: Bytes 6â€“10
    @Column(name = "spare_6_10", length = 5)
    private String spare6To10;

    // SSIM T2: Bytes 11â€“13
    @Column(name = "season", length = 3)
    private String season;

    // SSIM T2: Byte 14
    @Column(name = "spare_14", length = 1)
    private String spare14;

    // SSIM T2: Bytes 15â€“21
    @Column(name = "validity_start_raw", length = 7)
    private String validityStartRaw;

    // SSIM T2: Bytes 22â€“28
    @Column(name = "validity_end_raw", length = 7)
    private String validityEndRaw;

    // SSIM T2: Bytes 29â€“35
    @Column(name = "creation_date_raw", length = 7)
    private String creationDateRaw;

    // SSIM T2: Bytes 36â€“64
    @Column(name = "title_of_data", length = 29)
    private String titleOfData;

    // SSIM T2: Bytes 65â€“71
    @Column(name = "release_date_raw", length = 7)
    private String releaseDateRaw;

    // SSIM T2: Byte 72
    @Column(name = "schedule_status", length = 1)
    private String scheduleStatus;

    // SSIM T2: Bytes 73â€“107
    @Column(name = "creator_reference", length = 35)
    private String creatorReference;

    // SSIM T2: Byte 108
    @Column(name = "duplicate_designator_marker", length = 1)
    private String duplicateDesignatorMarker;

    // SSIM T2: Bytes 109â€“169
    @Column(name = "general_information", length = 61)
    private String generalInformation;

    // SSIM T2: Bytes 170â€“188
    @Column(name = "inflight_service_info", length = 19)
    private String inflightServiceInfo;

    // SSIM T2: Bytes 189â€“190
    @Column(name = "electronic_ticketing_info", length = 2)
    private String electronicTicketingInfo;

    // SSIM T2: Bytes 191â€“194
    @Column(name = "creation_time_raw", length = 4)
    private String creationTimeRaw;

    // SSIM T2: Bytes 195â€“200
    @Column(name = "record_serial_number", length = 6)
    private String recordSerialNumber;


    public void addFlight(SsimFlightEntity flight) {
        if (flight != null) {
            flights.add(flight);
            flight.setCarrier(this); // ðŸ”¥ CRITICAL
        }
    }
}
