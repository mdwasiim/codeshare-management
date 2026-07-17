CREATE SCHEMA IF NOT EXISTS schedule_compare;

CREATE SEQUENCE IF NOT EXISTS schedule_compare.schedule_change_set_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS schedule_compare.schedule_flight_change_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS schedule_compare.schedule_leg_change_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS schedule_compare.schedule_segment_change_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS schedule_compare.schedule_dei_change_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS schedule_compare.schedule_codeshare_change_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE IF NOT EXISTS schedule_compare.schedule_change_set (
    id BIGINT PRIMARY KEY,
    change_set_id UUID NOT NULL,
    imported_schedule_id UUID NOT NULL,
    import_batch_id UUID NOT NULL,
    source_type VARCHAR(10) NOT NULL,
    airline_code VARCHAR(3) NOT NULL,
    partner_code VARCHAR(10),
    acceptance_mode VARCHAR(20),
    message_reference VARCHAR(50),
    source_system VARCHAR(50),
    source_file_name VARCHAR(255),
    source_checksum VARCHAR(128),
    source_creation_date_raw VARCHAR(20),
    source_creation_time_raw VARCHAR(20),
    source_created_at TIMESTAMPTZ,
    source_received_at TIMESTAMPTZ,
    started_at TIMESTAMPTZ,
    completed_at TIMESTAMPTZ,
    status VARCHAR(20) NOT NULL,
    error_message TEXT,
    total_legs_compared INTEGER,
    new_count INTEGER,
    cancelled_count INTEGER,
    retimed_count INTEGER,
    equipment_count INTEGER,
    other_change_count INTEGER,
    no_change_count INTEGER,
    created_at TIMESTAMPTZ NOT NULL,
    created_by VARCHAR(100),
    updated_at TIMESTAMPTZ,
    updated_by VARCHAR(100),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at TIMESTAMPTZ,
    deleted_by VARCHAR(100),
    transaction_id VARCHAR(50),
    CONSTRAINT uk_scs_change_set_id UNIQUE (change_set_id),
    CONSTRAINT uk_scs_import_batch_id UNIQUE (import_batch_id)
);

CREATE TABLE IF NOT EXISTS schedule_compare.schedule_flight_change (
    id BIGINT PRIMARY KEY,
    change_set_id BIGINT NOT NULL,
    airline_code VARCHAR(3) NOT NULL,
    flight_number VARCHAR(4) NOT NULL,
    operational_suffix VARCHAR(1),
    itinerary_variation_id VARCHAR(2),
    change_set_status VARCHAR(20) NOT NULL,
    status_recorded_at TIMESTAMPTZ,
    status_reason TEXT,
    created_at TIMESTAMPTZ NOT NULL,
    created_by VARCHAR(100),
    updated_at TIMESTAMPTZ,
    updated_by VARCHAR(100),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at TIMESTAMPTZ,
    deleted_by VARCHAR(100),
    transaction_id VARCHAR(50),
    CONSTRAINT fk_sfc_change_set FOREIGN KEY (change_set_id)
        REFERENCES schedule_compare.schedule_change_set (id) ON DELETE CASCADE,
    CONSTRAINT uk_sfc_change_set_flight UNIQUE (
        change_set_id,
        airline_code,
        flight_number,
        operational_suffix,
        itinerary_variation_id
    )
);

CREATE TABLE IF NOT EXISTS schedule_compare.schedule_leg_change (
    id BIGINT PRIMARY KEY,
    flight_change_id BIGINT NOT NULL,
    leg_sequence_number INTEGER NOT NULL,
    period_start DATE NOT NULL,
    period_end DATE NOT NULL,
    days_of_operation VARCHAR(7),
    departure_station VARCHAR(3),
    arrival_station VARCHAR(3),
    change_type VARCHAR(10) NOT NULL,
    live_leg_id BIGINT,
    ingested_flight_id BIGINT,
    live_snapshot TEXT,
    ingested_snapshot TEXT,
    change_set_status VARCHAR(20) NOT NULL,
    status_recorded_at TIMESTAMPTZ,
    status_reason TEXT,
    created_at TIMESTAMPTZ NOT NULL,
    created_by VARCHAR(100),
    updated_at TIMESTAMPTZ,
    updated_by VARCHAR(100),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at TIMESTAMPTZ,
    deleted_by VARCHAR(100),
    transaction_id VARCHAR(50),
    CONSTRAINT fk_slc_flight_change FOREIGN KEY (flight_change_id)
        REFERENCES schedule_compare.schedule_flight_change (id) ON DELETE CASCADE,
    CONSTRAINT uk_slc_flight_change_leg_type UNIQUE (
        flight_change_id,
        leg_sequence_number,
        period_start,
        period_end,
        days_of_operation,
        change_type
    )
);

CREATE TABLE IF NOT EXISTS schedule_compare.schedule_segment_change (
    id BIGINT PRIMARY KEY,
    leg_change_id BIGINT NOT NULL,
    board_point VARCHAR(3) NOT NULL,
    off_point VARCHAR(3) NOT NULL,
    segment_change_type VARCHAR(15) NOT NULL,
    live_segment_id BIGINT,
    ingested_segment_id BIGINT,
    change_set_status VARCHAR(20) NOT NULL,
    status_recorded_at TIMESTAMPTZ,
    status_reason TEXT,
    created_at TIMESTAMPTZ NOT NULL,
    created_by VARCHAR(100),
    updated_at TIMESTAMPTZ,
    updated_by VARCHAR(100),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at TIMESTAMPTZ,
    deleted_by VARCHAR(100),
    transaction_id VARCHAR(50),
    CONSTRAINT fk_ssc_leg_change FOREIGN KEY (leg_change_id)
        REFERENCES schedule_compare.schedule_leg_change (id) ON DELETE CASCADE,
    CONSTRAINT uk_ssc_leg_segment UNIQUE (leg_change_id, board_point, off_point)
);

CREATE TABLE IF NOT EXISTS schedule_compare.schedule_dei_change (
    id BIGINT PRIMARY KEY,
    leg_change_id BIGINT,
    segment_change_id BIGINT,
    dei_scope VARCHAR(10) NOT NULL,
    dei_code VARCHAR(3) NOT NULL,
    sequence_order INTEGER NOT NULL,
    dei_change_type VARCHAR(10) NOT NULL,
    live_value TEXT,
    ingested_value TEXT,
    live_dei_id BIGINT,
    ingested_dei_id BIGINT,
    change_set_status VARCHAR(20) NOT NULL,
    status_recorded_at TIMESTAMPTZ,
    status_reason TEXT,
    created_at TIMESTAMPTZ NOT NULL,
    created_by VARCHAR(100),
    updated_at TIMESTAMPTZ,
    updated_by VARCHAR(100),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at TIMESTAMPTZ,
    deleted_by VARCHAR(100),
    transaction_id VARCHAR(50),
    CONSTRAINT fk_sdc_leg_change FOREIGN KEY (leg_change_id)
        REFERENCES schedule_compare.schedule_leg_change (id) ON DELETE CASCADE,
    CONSTRAINT fk_sdc_segment_change FOREIGN KEY (segment_change_id)
        REFERENCES schedule_compare.schedule_segment_change (id) ON DELETE CASCADE,
    CONSTRAINT ck_sdc_single_parent CHECK (
        (leg_change_id IS NOT NULL AND segment_change_id IS NULL)
        OR (leg_change_id IS NULL AND segment_change_id IS NOT NULL)
    ),
    CONSTRAINT uk_sdc_leg_dei UNIQUE (leg_change_id, dei_code, sequence_order),
    CONSTRAINT uk_sdc_segment_dei UNIQUE (segment_change_id, dei_code, sequence_order)
);

CREATE TABLE IF NOT EXISTS schedule_compare.schedule_codeshare_change (
    id BIGINT PRIMARY KEY,
    leg_change_id BIGINT NOT NULL,
    change_type VARCHAR(10) NOT NULL,
    live_codeshare_id BIGINT,
    ingested_source_id BIGINT,
    marketing_airline_code VARCHAR(3) NOT NULL,
    marketing_flight_number VARCHAR(4) NOT NULL,
    marketing_operational_suffix VARCHAR(1),
    board_point VARCHAR(3),
    off_point VARCHAR(3),
    marketing_booking_designator VARCHAR(20),
    is_codeshare BOOLEAN NOT NULL,
    source_dei_code VARCHAR(3),
    sequence_order INTEGER NOT NULL,
    live_snapshot TEXT,
    ingested_snapshot TEXT,
    change_set_status VARCHAR(20) NOT NULL,
    status_recorded_at TIMESTAMPTZ,
    status_reason TEXT,
    created_at TIMESTAMPTZ NOT NULL,
    created_by VARCHAR(100),
    updated_at TIMESTAMPTZ,
    updated_by VARCHAR(100),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at TIMESTAMPTZ,
    deleted_by VARCHAR(100),
    transaction_id VARCHAR(50),
    CONSTRAINT fk_scc_leg_change FOREIGN KEY (leg_change_id)
        REFERENCES schedule_compare.schedule_leg_change (id) ON DELETE CASCADE,
    CONSTRAINT uk_scc_leg_codeshare UNIQUE (
        leg_change_id,
        marketing_airline_code,
        marketing_flight_number,
        board_point,
        off_point,
        sequence_order
    )
);

CREATE INDEX IF NOT EXISTS idx_scs_change_set_id ON schedule_compare.schedule_change_set (change_set_id);
CREATE INDEX IF NOT EXISTS idx_scs_airline ON schedule_compare.schedule_change_set (airline_code);
CREATE INDEX IF NOT EXISTS idx_scs_partner ON schedule_compare.schedule_change_set (partner_code);
CREATE INDEX IF NOT EXISTS idx_scs_status ON schedule_compare.schedule_change_set (status);
CREATE INDEX IF NOT EXISTS idx_scs_source_type ON schedule_compare.schedule_change_set (source_type);
CREATE INDEX IF NOT EXISTS idx_scs_imported_schedule ON schedule_compare.schedule_change_set (imported_schedule_id);
CREATE INDEX IF NOT EXISTS idx_scs_import_batch ON schedule_compare.schedule_change_set (import_batch_id);
CREATE INDEX IF NOT EXISTS idx_scs_started_at ON schedule_compare.schedule_change_set (started_at);

CREATE INDEX IF NOT EXISTS idx_sfc_change_set ON schedule_compare.schedule_flight_change (change_set_id);
CREATE INDEX IF NOT EXISTS idx_sfc_airline_flt ON schedule_compare.schedule_flight_change (airline_code, flight_number);
CREATE INDEX IF NOT EXISTS idx_sfc_change_set_status ON schedule_compare.schedule_flight_change (change_set_status);

CREATE INDEX IF NOT EXISTS idx_slc_flight_change ON schedule_compare.schedule_leg_change (flight_change_id);
CREATE INDEX IF NOT EXISTS idx_slc_change_type ON schedule_compare.schedule_leg_change (change_type);
CREATE INDEX IF NOT EXISTS idx_slc_change_set_status ON schedule_compare.schedule_leg_change (change_set_status);
CREATE INDEX IF NOT EXISTS idx_slc_live_leg ON schedule_compare.schedule_leg_change (live_leg_id);
CREATE INDEX IF NOT EXISTS idx_slc_period ON schedule_compare.schedule_leg_change (period_start, period_end);

CREATE INDEX IF NOT EXISTS idx_ssc_leg_change ON schedule_compare.schedule_segment_change (leg_change_id);
CREATE INDEX IF NOT EXISTS idx_ssc_segment ON schedule_compare.schedule_segment_change (board_point, off_point);
CREATE INDEX IF NOT EXISTS idx_ssc_change_type ON schedule_compare.schedule_segment_change (segment_change_type);
CREATE INDEX IF NOT EXISTS idx_ssc_change_set_status ON schedule_compare.schedule_segment_change (change_set_status);
CREATE INDEX IF NOT EXISTS idx_ssc_live_seg ON schedule_compare.schedule_segment_change (live_segment_id);

CREATE INDEX IF NOT EXISTS idx_sdc_leg_change ON schedule_compare.schedule_dei_change (leg_change_id);
CREATE INDEX IF NOT EXISTS idx_sdc_segment_change ON schedule_compare.schedule_dei_change (segment_change_id);
CREATE INDEX IF NOT EXISTS idx_sdc_dei_code ON schedule_compare.schedule_dei_change (dei_code);
CREATE INDEX IF NOT EXISTS idx_sdc_change_type ON schedule_compare.schedule_dei_change (dei_change_type);
CREATE INDEX IF NOT EXISTS idx_sdc_change_set_status ON schedule_compare.schedule_dei_change (change_set_status);
CREATE INDEX IF NOT EXISTS idx_sdc_scope ON schedule_compare.schedule_dei_change (dei_scope);

CREATE INDEX IF NOT EXISTS idx_scc_leg_change ON schedule_compare.schedule_codeshare_change (leg_change_id);
CREATE INDEX IF NOT EXISTS idx_scc_change_type ON schedule_compare.schedule_codeshare_change (change_type);
CREATE INDEX IF NOT EXISTS idx_scc_change_set_status ON schedule_compare.schedule_codeshare_change (change_set_status);
CREATE INDEX IF NOT EXISTS idx_scc_live_codeshare ON schedule_compare.schedule_codeshare_change (live_codeshare_id);
CREATE INDEX IF NOT EXISTS idx_scc_marketing ON schedule_compare.schedule_codeshare_change (marketing_airline_code, marketing_flight_number);
