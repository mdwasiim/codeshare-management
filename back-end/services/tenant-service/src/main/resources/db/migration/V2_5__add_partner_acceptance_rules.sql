CREATE SEQUENCE IF NOT EXISTS tenant_core.codeshare_partner_acceptance_rule_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE IF NOT EXISTS tenant_core.codeshare_partner_acceptance_rule (
    id BIGINT PRIMARY KEY,
    partner_id BIGINT NOT NULL,
    message_type VARCHAR(30) NOT NULL,
    approval_mode VARCHAR(20) NOT NULL DEFAULT 'MANUAL',
    active BOOLEAN NOT NULL DEFAULT TRUE,
    display_order INTEGER,
    description VARCHAR(500),
    remarks VARCHAR(1000),
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    effective_from DATE,
    effective_to DATE,
    created_at TIMESTAMPTZ NOT NULL,
    created_by VARCHAR(100),
    updated_at TIMESTAMPTZ,
    updated_by VARCHAR(100),
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at TIMESTAMPTZ,
    deleted_by VARCHAR(100),
    transaction_id VARCHAR(50),
    CONSTRAINT uk_partner_acceptance_msg UNIQUE (partner_id, message_type),
    CONSTRAINT fk_partner_acceptance_partner FOREIGN KEY (partner_id)
        REFERENCES tenant_core.codeshare_partner (id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_partner_acceptance_partner
    ON tenant_core.codeshare_partner_acceptance_rule (partner_id);
CREATE INDEX IF NOT EXISTS idx_partner_acceptance_message
    ON tenant_core.codeshare_partner_acceptance_rule (message_type);
CREATE INDEX IF NOT EXISTS idx_partner_acceptance_active
    ON tenant_core.codeshare_partner_acceptance_rule (active);
