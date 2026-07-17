CREATE SCHEMA IF NOT EXISTS distribution_engine;

CREATE SEQUENCE IF NOT EXISTS distribution_engine.distribution_job_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE IF NOT EXISTS distribution_engine.distribution_job (
    id BIGINT PRIMARY KEY,
    distribution_request_id UUID NOT NULL,
    outbound_message_id UUID,
    change_set_id UUID NOT NULL,
    change_request_id BIGINT,
    imported_schedule_id UUID,
    import_batch_id UUID,
    message_type VARCHAR(10) NOT NULL,
    airline_code VARCHAR(3) NOT NULL,
    partner_code VARCHAR(10),
    status VARCHAR(30) NOT NULL,
    requested_at TIMESTAMPTZ,
    started_at TIMESTAMPTZ,
    completed_at TIMESTAMPTZ,
    dispatch_count INTEGER,
    error_message TEXT,
    created_at TIMESTAMPTZ NOT NULL,
    created_by VARCHAR(100),
    updated_at TIMESTAMPTZ,
    updated_by VARCHAR(100),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at TIMESTAMPTZ,
    deleted_by VARCHAR(100),
    transaction_id VARCHAR(50),
    CONSTRAINT uk_dist_job_request UNIQUE (distribution_request_id)
);

CREATE INDEX IF NOT EXISTS idx_dist_job_request
    ON distribution_engine.distribution_job (distribution_request_id);
CREATE INDEX IF NOT EXISTS idx_dist_job_outbound_message
    ON distribution_engine.distribution_job (outbound_message_id);
CREATE INDEX IF NOT EXISTS idx_dist_job_change_set
    ON distribution_engine.distribution_job (change_set_id);
CREATE INDEX IF NOT EXISTS idx_dist_job_airline_partner
    ON distribution_engine.distribution_job (airline_code, partner_code);
CREATE INDEX IF NOT EXISTS idx_dist_job_status
    ON distribution_engine.distribution_job (status);
