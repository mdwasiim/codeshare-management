ALTER TABLE tenant_core.schedule_ingestion_channel
    ADD COLUMN IF NOT EXISTS partner_code VARCHAR(10);

ALTER TABLE tenant_core.schedule_ingestion_channel
    DROP CONSTRAINT IF EXISTS uk_tenant_profile_msg_source;

CREATE INDEX IF NOT EXISTS idx_ingestion_channel_partner_code
    ON tenant_core.schedule_ingestion_channel (partner_code);

CREATE UNIQUE INDEX IF NOT EXISTS uk_tenant_profile_msg_source_partner
    ON tenant_core.schedule_ingestion_channel (profile_id, message_type, source_type, COALESCE(partner_code, ''));
