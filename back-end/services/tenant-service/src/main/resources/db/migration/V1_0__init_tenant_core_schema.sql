-- V1_0__init_tenant_core_schema.sql
-- Sets up the tenant_core schema, sequences, and tables required by tenant bootstrap scripts

CREATE SCHEMA IF NOT EXISTS tenant_core;

-- ---------------------------------------------------------------------------
-- Sequences (one per aggregate to align with Hibernate SequenceStyleGenerator)
-- ---------------------------------------------------------------------------
CREATE SEQUENCE IF NOT EXISTS tenant_core.tenants_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS tenant_core.oidc_identity_provider_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS tenant_core.oidc_config_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS tenant_core.schedule_ingestion_profile_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS tenant_core.schedule_ingestion_channel_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS tenant_core.codeshare_partner_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS tenant_core.codeshare_partner_profile_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS tenant_core.codeshare_partner_communication_profile_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS tenant_core.codeshare_partner_distribution_profile_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS tenant_core.codeshare_partner_acceptance_rule_seq START WITH 1 INCREMENT BY 50;

-- --------------------
-- Tenants
-- --------------------
CREATE TABLE IF NOT EXISTS tenant_core.tenants (
    id BIGINT PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    tenant_code VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    status VARCHAR(20) NOT NULL,
    plan VARCHAR(50),
    subscription_start TIMESTAMP,
    subscription_end TIMESTAMP,
    trial BOOLEAN NOT NULL DEFAULT FALSE,
    contact_email VARCHAR(200),
    contact_phone VARCHAR(50),
    logo_url VARCHAR(500),
    location VARCHAR(100),
    created_at TIMESTAMPTZ NOT NULL,
    created_by VARCHAR(100),
    updated_at TIMESTAMPTZ,
    updated_by VARCHAR(100),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at TIMESTAMPTZ,
    deleted_by VARCHAR(100),
    transaction_id VARCHAR(50),
    CONSTRAINT uk_tenant_code UNIQUE (tenant_code)
);
CREATE INDEX IF NOT EXISTS idx_tenant_code ON tenant_core.tenants (tenant_code);

-- --------------------
-- Identity Providers
-- --------------------
CREATE TABLE IF NOT EXISTS tenant_core.oidc_identity_provider (
    id BIGINT PRIMARY KEY,
    tenant_id BIGINT NOT NULL,
    auth_source VARCHAR(50) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    priority INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMPTZ NOT NULL,
    created_by VARCHAR(100),
    updated_at TIMESTAMPTZ,
    updated_by VARCHAR(100),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at TIMESTAMPTZ,
    deleted_by VARCHAR(100),
    transaction_id VARCHAR(50),
    CONSTRAINT uk_tenant_auth_source UNIQUE (tenant_id, auth_source),
    CONSTRAINT fk_oidc_provider_tenant FOREIGN KEY (tenant_id)
        REFERENCES tenant_core.tenants (id) ON DELETE CASCADE
);
CREATE INDEX IF NOT EXISTS idx_oidc_provider_tenant ON tenant_core.oidc_identity_provider (tenant_id);

CREATE TABLE IF NOT EXISTS tenant_core.oidc_config (
    id BIGINT PRIMARY KEY,
    identity_provider_id BIGINT NOT NULL UNIQUE,
    issuer_uri VARCHAR(300),
    authorization_uri VARCHAR(300),
    token_uri VARCHAR(300),
    jwk_set_uri VARCHAR(300),
    client_id VARCHAR(150),
    client_secret_ref VARCHAR(200),
    redirect_uri VARCHAR(300),
    grant_type VARCHAR(50),
    client_auth_method VARCHAR(50),
    scopes VARCHAR(300),
    enforce_redirect_uri BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL,
    created_by VARCHAR(100),
    updated_at TIMESTAMPTZ,
    updated_by VARCHAR(100),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at TIMESTAMPTZ,
    deleted_by VARCHAR(100),
    transaction_id VARCHAR(50),
    CONSTRAINT fk_oidc_config_provider FOREIGN KEY (identity_provider_id)
        REFERENCES tenant_core.oidc_identity_provider (id) ON DELETE CASCADE
);
CREATE INDEX IF NOT EXISTS idx_oidc_issuer ON tenant_core.oidc_config (issuer_uri);

-- --------------------
-- Schedule Ingestion
-- --------------------
CREATE TABLE IF NOT EXISTS tenant_core.schedule_ingestion_profile (
    id BIGINT PRIMARY KEY,
    tenant_id BIGINT NOT NULL,
    source_system VARCHAR(50) NOT NULL,
    enabled BOOLEAN NOT NULL,
    poll_interval_ms BIGINT,
    version BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMPTZ NOT NULL,
    created_by VARCHAR(100),
    updated_at TIMESTAMPTZ,
    updated_by VARCHAR(100),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at TIMESTAMPTZ,
    deleted_by VARCHAR(100),
    transaction_id VARCHAR(50),
    CONSTRAINT uk_tenant_ingestion_profile UNIQUE (tenant_id),
    CONSTRAINT fk_ingestion_profile_tenant FOREIGN KEY (tenant_id)
        REFERENCES tenant_core.tenants (id) ON DELETE CASCADE
);
CREATE INDEX IF NOT EXISTS idx_ingestion_profile_tenant ON tenant_core.schedule_ingestion_profile (tenant_id);
CREATE INDEX IF NOT EXISTS idx_ingestion_profile_enabled ON tenant_core.schedule_ingestion_profile (enabled);

CREATE TABLE IF NOT EXISTS tenant_core.schedule_ingestion_channel (
    id BIGINT PRIMARY KEY,
    profile_id BIGINT NOT NULL,
    partner_code VARCHAR(10),
    message_type VARCHAR(20) NOT NULL,
    source_type VARCHAR(20) NOT NULL,
    host VARCHAR(255),
    port INTEGER,
    username VARCHAR(255),
    password_encrypted VARCHAR(500),
    remote_directory VARCHAR(500),
    protocol VARCHAR(50),
    mailbox VARCHAR(255),
    mail_delay_ms INTEGER,
    mail_unseen_only BOOLEAN,
    mail_delete BOOLEAN,
    mail_peek BOOLEAN,
    mail_move_to VARCHAR(255),
    broker_url VARCHAR(500),
    queue_name VARCHAR(255),
    topic_name VARCHAR(255),
    concurrent_consumers INTEGER,
    max_concurrent_consumers INTEGER,
    async_consumer BOOLEAN,
    receive_timeout_ms INTEGER,
    max_messages_per_task INTEGER,
    file_noop BOOLEAN,
    file_delete BOOLEAN,
    file_include_pattern VARCHAR(255),
    file_exclude_pattern VARCHAR(255),
    file_read_lock VARCHAR(50),
    file_read_lock_min_age VARCHAR(50),
    file_read_lock_timeout INTEGER,
    file_read_lock_check_interval INTEGER,
    file_poll_delay_ms INTEGER,
    file_initial_delay_ms INTEGER,
    file_move VARCHAR(500),
    file_move_failed VARCHAR(500),
    file_pre_move VARCHAR(500),
    file_idempotent BOOLEAN,
    file_idempotent_key VARCHAR(255),
    file_charset VARCHAR(100),
    max_messages_per_poll INTEGER,
    is_recursive BOOLEAN,
    bridge_error_handler BOOLEAN,
    disconnect_flag BOOLEAN,
    is_binary BOOLEAN,
    passive_mode BOOLEAN,
    reconnect_delay_ms INTEGER,
    max_reconnect_attempts INTEGER,
    so_timeout_ms INTEGER,
    retry_attempts INTEGER,
    retry_delay_ms INTEGER,
    is_enabled BOOLEAN,
    priority INTEGER,
    version BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMPTZ NOT NULL,
    created_by VARCHAR(100),
    updated_at TIMESTAMPTZ,
    updated_by VARCHAR(100),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at TIMESTAMPTZ,
    deleted_by VARCHAR(100),
    transaction_id VARCHAR(50),
    CONSTRAINT fk_ingestion_channel_profile FOREIGN KEY (profile_id)
        REFERENCES tenant_core.schedule_ingestion_profile (id) ON DELETE CASCADE
);
CREATE INDEX IF NOT EXISTS idx_ingestion_channel_profile ON tenant_core.schedule_ingestion_channel (profile_id);
CREATE INDEX IF NOT EXISTS idx_ingestion_channel_partner_code ON tenant_core.schedule_ingestion_channel (partner_code);
CREATE INDEX IF NOT EXISTS idx_ingestion_channel_host ON tenant_core.schedule_ingestion_channel (host);
CREATE INDEX IF NOT EXISTS idx_ingestion_channel_queue ON tenant_core.schedule_ingestion_channel (queue_name);
CREATE INDEX IF NOT EXISTS idx_ingestion_channel_topic ON tenant_core.schedule_ingestion_channel (topic_name);
CREATE UNIQUE INDEX IF NOT EXISTS uk_tenant_profile_msg_source_partner
    ON tenant_core.schedule_ingestion_channel (profile_id, message_type, source_type, COALESCE(partner_code, ''));

-- --------------------
-- Codeshare Partners
-- --------------------
CREATE TABLE IF NOT EXISTS tenant_core.codeshare_partner (
    id BIGINT PRIMARY KEY,
    tenant_id BIGINT NOT NULL,
    home_airline_id BIGINT NOT NULL,
    partner_airline_id BIGINT NOT NULL,
    agreement_number VARCHAR(50),
    agreement_type VARCHAR(30) NOT NULL,
    agreement_status VARCHAR(30) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    display_order INTEGER,
    description VARCHAR(500),
    remarks VARCHAR(1000),
    status VARCHAR(20) NOT NULL,
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
    CONSTRAINT uk_codeshare_partner UNIQUE (tenant_id, home_airline_id, partner_airline_id),
    CONSTRAINT fk_codeshare_partner_tenant FOREIGN KEY (tenant_id)
        REFERENCES tenant_core.tenants (id) ON DELETE CASCADE
);
CREATE INDEX IF NOT EXISTS idx_codeshare_tenant ON tenant_core.codeshare_partner (tenant_id);
CREATE INDEX IF NOT EXISTS idx_codeshare_home_airline ON tenant_core.codeshare_partner (home_airline_id);
CREATE INDEX IF NOT EXISTS idx_codeshare_partner_airline ON tenant_core.codeshare_partner (partner_airline_id);
CREATE INDEX IF NOT EXISTS idx_codeshare_status ON tenant_core.codeshare_partner (status);
CREATE INDEX IF NOT EXISTS idx_codeshare_active ON tenant_core.codeshare_partner (active);

CREATE TABLE IF NOT EXISTS tenant_core.codeshare_partner_profile (
    id BIGINT PRIMARY KEY,
    partner_id BIGINT NOT NULL,
    profile_code VARCHAR(30) NOT NULL,
    profile_name VARCHAR(150) NOT NULL,
    partner_type VARCHAR(30) NOT NULL,
    agreement_category VARCHAR(30) NOT NULL,
    inventory_sharing_type VARCHAR(30) NOT NULL,
    priority INTEGER,
    auto_accept_schedule_changes BOOLEAN NOT NULL DEFAULT FALSE,
    proration_applicable BOOLEAN NOT NULL DEFAULT TRUE,
    e_ticket_allowed BOOLEAN NOT NULL DEFAULT TRUE,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    display_order INTEGER,
    description VARCHAR(500),
    remarks VARCHAR(1000),
    status VARCHAR(20) NOT NULL,
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
    CONSTRAINT fk_codeshare_profile_partner FOREIGN KEY (partner_id)
        REFERENCES tenant_core.codeshare_partner (id) ON DELETE CASCADE
);
CREATE INDEX IF NOT EXISTS idx_codeshare_profile_partner ON tenant_core.codeshare_partner_profile (partner_id);

CREATE TABLE IF NOT EXISTS tenant_core.codeshare_partner_communication_profile (
    id BIGINT PRIMARY KEY,
    partner_id BIGINT NOT NULL,
    profile_code VARCHAR(30) NOT NULL,
    profile_name VARCHAR(150) NOT NULL,
    protocol VARCHAR(30) NOT NULL,
    transport_type VARCHAR(30) NOT NULL,
    message_format VARCHAR(30) NOT NULL,
    authentication_type VARCHAR(30) NOT NULL,
    endpoint_url VARCHAR(1000),
    username VARCHAR(255),
    credential_alias VARCHAR(255),
    connection_timeout INTEGER,
    read_timeout INTEGER,
    retry_count INTEGER,
    compression_enabled BOOLEAN NOT NULL DEFAULT FALSE,
    encryption_enabled BOOLEAN NOT NULL DEFAULT FALSE,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    display_order INTEGER,
    description VARCHAR(500),
    remarks VARCHAR(1000),
    status VARCHAR(20) NOT NULL,
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
    CONSTRAINT fk_codeshare_comm_partner FOREIGN KEY (partner_id)
        REFERENCES tenant_core.codeshare_partner (id) ON DELETE CASCADE
);
CREATE INDEX IF NOT EXISTS idx_codeshare_comm_partner ON tenant_core.codeshare_partner_communication_profile (partner_id);

CREATE TABLE IF NOT EXISTS tenant_core.codeshare_partner_distribution_profile (
    id BIGINT PRIMARY KEY,
    partner_id BIGINT NOT NULL,
    profile_code VARCHAR(30) NOT NULL,
    profile_name VARCHAR(150) NOT NULL,
    distribution_channel VARCHAR(30) NOT NULL,
    distribution_mode VARCHAR(30) NOT NULL,
    message_type VARCHAR(30) NOT NULL,
    real_time_enabled BOOLEAN NOT NULL DEFAULT FALSE,
    acknowledgement_required BOOLEAN NOT NULL DEFAULT FALSE,
    retry_enabled BOOLEAN NOT NULL DEFAULT FALSE,
    retry_count INTEGER,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    display_order INTEGER,
    description VARCHAR(500),
    remarks VARCHAR(1000),
    status VARCHAR(20) NOT NULL,
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
    CONSTRAINT fk_codeshare_dist_partner FOREIGN KEY (partner_id)
        REFERENCES tenant_core.codeshare_partner (id) ON DELETE CASCADE
);
CREATE INDEX IF NOT EXISTS idx_codeshare_dist_partner ON tenant_core.codeshare_partner_distribution_profile (partner_id);

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
