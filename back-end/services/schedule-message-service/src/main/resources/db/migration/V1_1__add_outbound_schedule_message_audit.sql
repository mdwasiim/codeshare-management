CREATE SEQUENCE IF NOT EXISTS schedule_message.outbound_schedule_message_audit_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE IF NOT EXISTS schedule_message.outbound_schedule_message_audit (
    id BIGINT PRIMARY KEY,
    outbound_message_id UUID NOT NULL,
    change_set_id UUID NOT NULL,
    change_request_id BIGINT,
    imported_schedule_id UUID,
    import_batch_id UUID,
    message_type VARCHAR(10) NOT NULL,
    airline_code VARCHAR(3) NOT NULL,
    partner_code VARCHAR(10),
    event_type VARCHAR(40) NOT NULL,
    event_at TIMESTAMPTZ NOT NULL,
    correlation_id UUID,
    causation_id UUID,
    details TEXT,
    created_at TIMESTAMPTZ NOT NULL,
    created_by VARCHAR(100),
    updated_at TIMESTAMPTZ,
    updated_by VARCHAR(100),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at TIMESTAMPTZ,
    deleted_by VARCHAR(100),
    transaction_id VARCHAR(50)
);

CREATE INDEX IF NOT EXISTS idx_osm_audit_message_id
    ON schedule_message.outbound_schedule_message_audit (outbound_message_id);
CREATE INDEX IF NOT EXISTS idx_osm_audit_change_set
    ON schedule_message.outbound_schedule_message_audit (change_set_id);
CREATE INDEX IF NOT EXISTS idx_osm_audit_event_type
    ON schedule_message.outbound_schedule_message_audit (event_type);
CREATE INDEX IF NOT EXISTS idx_osm_audit_event_at
    ON schedule_message.outbound_schedule_message_audit (event_at);
