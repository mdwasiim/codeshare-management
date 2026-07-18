-- V2_1__bootstrap_tenant_qr.sql
-- Seeds the QR tenant aggregate (tenant, identity providers, OIDC config, and schedule ingestion channels)

------------------------------------------------------------
-- Tenant
------------------------------------------------------------
INSERT INTO tenant_core.tenants (
    id,
    name,
    tenant_code,
    description,
    status,
    plan,
    subscription_start,
    subscription_end,
    trial,
    contact_email,
    contact_phone,
    logo_url,
    location,
    created_at,
    created_by,
    updated_at,
    updated_by,
    active,
    is_deleted,
    deleted_at,
    deleted_by,
    transaction_id
)
SELECT
    nextval('tenant_core.tenants_seq'),
    'Qatar Airways',
    'QR',
    'Host airline tenant for Qatar Airways',
    'ACTIVE',
    'ENTERPRISE',
    TIMESTAMP '2025-01-01 00:00:00',
    TIMESTAMP '2035-01-01 00:00:00',
    FALSE,
    'platform-admin@qatarairways.com',
    '+974 4022 6000',
    'https://www.qatarairways.com/content/dam/images/renditions/qatar-airways-logo.svg',
    'Qatar',
    CURRENT_TIMESTAMP,
    'FLYWAY',
    CURRENT_TIMESTAMP,
    'FLYWAY',
    TRUE,
    FALSE,
    NULL,
    NULL,
    'FLYWAY'
WHERE NOT EXISTS (
    SELECT 1 FROM tenant_core.tenants WHERE tenant_code = 'QR'
);

------------------------------------------------------------
-- Internal Identity Provider
------------------------------------------------------------
INSERT INTO tenant_core.oidc_identity_provider (
    id,
    tenant_id,
    auth_source,
    enabled,
    priority,
    created_at,
    created_by,
    updated_at,
    updated_by,
    active,
    is_deleted,
    deleted_at,
    deleted_by,
    transaction_id
)
SELECT
    nextval('tenant_core.oidc_identity_provider_seq'),
    t.id,
    'INTERNAL',
    TRUE,
    1,
    CURRENT_TIMESTAMP,
    'FLYWAY',
    CURRENT_TIMESTAMP,
    'FLYWAY',
    TRUE,
    FALSE,
    NULL,
    NULL,
    'FLYWAY'
FROM tenant_core.tenants t
WHERE t.tenant_code = 'QR'
  AND NOT EXISTS (
        SELECT 1
        FROM tenant_core.oidc_identity_provider o
        WHERE o.tenant_id = t.id
          AND o.auth_source = 'INTERNAL'
    );

------------------------------------------------------------
-- Generic OIDC Identity Provider
------------------------------------------------------------
INSERT INTO tenant_core.oidc_identity_provider (
    id,
    tenant_id,
    auth_source,
    enabled,
    priority,
    created_at,
    created_by,
    updated_at,
    updated_by,
    active,
    is_deleted,
    deleted_at,
    deleted_by,
    transaction_id
)
SELECT
    nextval('tenant_core.oidc_identity_provider_seq'),
    t.id,
    'OIDC_GENERIC',
    FALSE,
    100,
    CURRENT_TIMESTAMP,
    'FLYWAY',
    CURRENT_TIMESTAMP,
    'FLYWAY',
    TRUE,
    FALSE,
    NULL,
    NULL,
    'FLYWAY'
FROM tenant_core.tenants t
WHERE t.tenant_code = 'QR'
  AND NOT EXISTS (
        SELECT 1
        FROM tenant_core.oidc_identity_provider o
        WHERE o.tenant_id = t.id
          AND o.auth_source = 'OIDC_GENERIC'
    );

------------------------------------------------------------
-- OIDC Configuration
------------------------------------------------------------
INSERT INTO tenant_core.oidc_config (
    id,
    identity_provider_id,
    issuer_uri,
    authorization_uri,
    token_uri,
    jwk_set_uri,
    client_id,
    client_secret_ref,
    redirect_uri,
    grant_type,
    client_auth_method,
    scopes,
    enforce_redirect_uri,
    created_at,
    created_by,
    updated_at,
    updated_by,
    active,
    is_deleted,
    deleted_at,
    deleted_by,
    transaction_id
)
SELECT
    nextval('tenant_core.oidc_config_seq'),
    p.id,
    'https://identity.codeshare.local/realms/qr',
    'https://identity.codeshare.local/realms/qr/protocol/openid-connect/auth',
    'https://identity.codeshare.local/realms/qr/protocol/openid-connect/token',
    'https://identity.codeshare.local/realms/qr/protocol/openid-connect/certs',
    'codeshare-qr-portal',
    'tenant/qr/identity/oidc-client-secret',
    'http://localhost:4201/auth/callback',
    'authorization_code',
    'client_secret_post',
    'openid profile email',
    TRUE,
    CURRENT_TIMESTAMP,
    'FLYWAY',
    CURRENT_TIMESTAMP,
    'FLYWAY',
    TRUE,
    FALSE,
    NULL,
    NULL,
    'FLYWAY'
FROM tenant_core.oidc_identity_provider p
JOIN tenant_core.tenants t ON p.tenant_id = t.id
WHERE t.tenant_code = 'QR'
  AND p.auth_source = 'OIDC_GENERIC'
  AND NOT EXISTS (
        SELECT 1
        FROM tenant_core.oidc_config c
        WHERE c.identity_provider_id = p.id
    );

------------------------------------------------------------
-- Schedule Ingestion Profile
------------------------------------------------------------
INSERT INTO tenant_core.schedule_ingestion_profile (
    id,
    tenant_id,
    source_system,
    enabled,
    poll_interval_ms,
    created_at,
    created_by,
    updated_at,
    updated_by,
    active,
    is_deleted,
    deleted_at,
    deleted_by,
    transaction_id,
    version
)
SELECT
    nextval('tenant_core.schedule_ingestion_profile_seq'),
    t.id,
    'CODESHARE_PLATFORM',
    TRUE,
    300000,
    CURRENT_TIMESTAMP,
    'FLYWAY',
    CURRENT_TIMESTAMP,
    'FLYWAY',
    TRUE,
    FALSE,
    NULL,
    NULL,
    'FLYWAY',
    0
FROM tenant_core.tenants t
WHERE t.tenant_code = 'QR'
  AND NOT EXISTS (
        SELECT 1
        FROM tenant_core.schedule_ingestion_profile sip
        WHERE sip.tenant_id = t.id
    );

------------------------------------------------------------
-- SSIM Local Channel
------------------------------------------------------------
WITH profile AS (
    SELECT sip.id
    FROM tenant_core.schedule_ingestion_profile sip
    JOIN tenant_core.tenants t ON sip.tenant_id = t.id
    WHERE t.tenant_code = 'QR'
)
INSERT INTO tenant_core.schedule_ingestion_channel (
    id,
    profile_id,
    message_type,
    source_type,
    remote_directory,
    file_noop,
    file_delete,
    file_include_pattern,
    file_exclude_pattern,
    file_read_lock,
    file_read_lock_min_age,
    file_read_lock_timeout,
    file_read_lock_check_interval,
    file_poll_delay_ms,
    file_initial_delay_ms,
    file_move,
    file_move_failed,
    file_pre_move,
    file_idempotent,
    file_idempotent_key,
    file_charset,
    max_messages_per_poll,
    is_recursive,
    bridge_error_handler,
    disconnect_flag,
    is_binary,
    passive_mode,
    reconnect_delay_ms,
    max_reconnect_attempts,
    so_timeout_ms,
    retry_attempts,
    retry_delay_ms,
    is_enabled,
    priority,
    created_at,
    created_by,
    updated_at,
    updated_by,
    active,
    is_deleted,
    deleted_at,
    deleted_by,
    transaction_id,
    version
)
SELECT
    nextval('tenant_core.schedule_ingestion_channel_seq'),
    profile.id,
    'SSIM',
    'LOCAL',
    'C:/Users/mdwas/Projects/messages/qr/ssim/inbound',
    FALSE,
    FALSE,
    '(?i).*[.](ssim|txt)',
    '*.tmp,*.bak',
    'changed',
    '2s',
    60000,
    1000,
    60000,
    2000,
    '.processed/${date:now:yyyyMMdd}/${file:name}',
    '.error/${date:now:yyyyMMdd}/${file:name}',
    '.inprogress/${file:name}',
    TRUE,
    '${file:absolute.path}-${file:modified}',
    'UTF-8',
    10,
    FALSE,
    TRUE,
    TRUE,
    FALSE,
    FALSE,
    5000,
    12,
    30000,
    5,
    10000,
    FALSE,
    1,
    CURRENT_TIMESTAMP,
    'FLYWAY',
    CURRENT_TIMESTAMP,
    'FLYWAY',
    TRUE,
    FALSE,
    NULL,
    NULL,
    'FLYWAY',
    0
FROM profile
WHERE NOT EXISTS (
    SELECT 1
    FROM tenant_core.schedule_ingestion_channel sic
    WHERE sic.profile_id = profile.id
      AND sic.message_type = 'SSIM'
      AND sic.source_type = 'LOCAL'
);

------------------------------------------------------------
-- SSIM SFTP Channel
------------------------------------------------------------
WITH profile AS (
    SELECT sip.id
    FROM tenant_core.schedule_ingestion_profile sip
    JOIN tenant_core.tenants t ON sip.tenant_id = t.id
    WHERE t.tenant_code = 'QR'
)
INSERT INTO tenant_core.schedule_ingestion_channel (
    id,
    profile_id,
    message_type,
    source_type,
    host,
    port,
    username,
    password_encrypted,
    remote_directory,
    protocol,
    file_noop,
    file_delete,
    file_include_pattern,
    file_exclude_pattern,
    file_read_lock,
    file_read_lock_min_age,
    file_read_lock_timeout,
    file_read_lock_check_interval,
    file_poll_delay_ms,
    file_initial_delay_ms,
    file_move,
    file_move_failed,
    file_pre_move,
    file_idempotent,
    file_idempotent_key,
    file_charset,
    max_messages_per_poll,
    is_recursive,
    bridge_error_handler,
    disconnect_flag,
    is_binary,
    passive_mode,
    reconnect_delay_ms,
    max_reconnect_attempts,
    so_timeout_ms,
    retry_attempts,
    retry_delay_ms,
    is_enabled,
    priority,
    created_at,
    created_by,
    updated_at,
    updated_by,
    active,
    is_deleted,
    deleted_at,
    deleted_by,
    transaction_id,
    version
)
SELECT
    nextval('tenant_core.schedule_ingestion_channel_seq'),
    profile.id,
    'SSIM',
    'SFTP',
    '127.0.0.1',
    22,
    'sftpuser',
    'ljFeL/iBdZmXxg4GgANXksZVjFFcNBAB4vvwqnqd8o+n7Z4R',
    'QR/SSIM',
    'SFTP',
    FALSE,
    FALSE,
    '(?i).*[.]ssim',
    '*.tmp,*.bak',
    'changed',
    '30s',
    300000,
    10000,
    15000,
    5000,
    '.processed/${file:name}',
    '.failed/${file:name}',
    '.processing/${file:name}',
    TRUE,
    '${file:name}-${file:size}-${file:modified}',
    'UTF-8',
    10,
    FALSE,
    TRUE,
    TRUE,
    FALSE,
    FALSE,
    5000,
    12,
    30000,
    5,
    10000,
    TRUE,
    2,
    CURRENT_TIMESTAMP,
    'FLYWAY',
    CURRENT_TIMESTAMP,
    'FLYWAY',
    TRUE,
    FALSE,
    NULL,
    NULL,
    'FLYWAY',
    0
FROM profile
WHERE NOT EXISTS (
    SELECT 1
    FROM tenant_core.schedule_ingestion_channel sic
    WHERE sic.profile_id = profile.id
      AND sic.message_type = 'SSIM'
      AND sic.source_type = 'SFTP'
);

------------------------------------------------------------
-- ASM Local Channel
------------------------------------------------------------
WITH profile AS (
    SELECT sip.id
    FROM tenant_core.schedule_ingestion_profile sip
    JOIN tenant_core.tenants t ON sip.tenant_id = t.id
    WHERE t.tenant_code = 'QR'
)
INSERT INTO tenant_core.schedule_ingestion_channel (
    id,
    profile_id,
    message_type,
    source_type,
    remote_directory,
    file_noop,
    file_delete,
    file_include_pattern,
    file_exclude_pattern,
    file_read_lock,
    file_read_lock_min_age,
    file_read_lock_timeout,
    file_read_lock_check_interval,
    file_poll_delay_ms,
    file_initial_delay_ms,
    file_move,
    file_move_failed,
    file_pre_move,
    file_idempotent,
    file_idempotent_key,
    file_charset,
    max_messages_per_poll,
    is_recursive,
    bridge_error_handler,
    disconnect_flag,
    is_binary,
    passive_mode,
    reconnect_delay_ms,
    max_reconnect_attempts,
    so_timeout_ms,
    retry_attempts,
    retry_delay_ms,
    is_enabled,
    priority,
    created_at,
    created_by,
    updated_at,
    updated_by,
    active,
    is_deleted,
    deleted_at,
    deleted_by,
    transaction_id,
    version
)
SELECT
    nextval('tenant_core.schedule_ingestion_channel_seq'),
    profile.id,
    'ASM',
    'LOCAL',
    'C:/Users/mdwas/Projects/messages/qr/asm/inbound',
    FALSE,
    FALSE,
    '(?i).*[.](asm|txt)',
    '*.tmp,*.bak',
    'changed',
    '2s',
    60000,
    1000,
    60000,
    2000,
    '.processed/${date:now:yyyyMMdd}/${file:name}',
    '.error/${date:now:yyyyMMdd}/${file:name}',
    '.inprogress/${file:name}',
    TRUE,
    '${file:absolute.path}-${file:modified}',
    'UTF-8',
    10,
    FALSE,
    TRUE,
    TRUE,
    FALSE,
    FALSE,
    5000,
    12,
    30000,
    5,
    10000,
    FALSE,
    3,
    CURRENT_TIMESTAMP,
    'FLYWAY',
    CURRENT_TIMESTAMP,
    'FLYWAY',
    TRUE,
    FALSE,
    NULL,
    NULL,
    'FLYWAY',
    0
FROM profile
WHERE NOT EXISTS (
    SELECT 1
    FROM tenant_core.schedule_ingestion_channel sic
    WHERE sic.profile_id = profile.id
      AND sic.message_type = 'ASM'
      AND sic.source_type = 'LOCAL'
);

------------------------------------------------------------
-- ASM MQ Channel
------------------------------------------------------------
WITH profile AS (
    SELECT sip.id
    FROM tenant_core.schedule_ingestion_profile sip
    JOIN tenant_core.tenants t ON sip.tenant_id = t.id
    WHERE t.tenant_code = 'QR'
)
INSERT INTO tenant_core.schedule_ingestion_channel (
    id,
    profile_id,
    message_type,
    source_type,
    protocol,
    broker_url,
    queue_name,
    concurrent_consumers,
    max_concurrent_consumers,
    async_consumer,
    receive_timeout_ms,
    max_messages_per_task,
    reconnect_delay_ms,
    max_reconnect_attempts,
    so_timeout_ms,
    retry_attempts,
    retry_delay_ms,
    is_enabled,
    priority,
    created_at,
    created_by,
    updated_at,
    updated_by,
    active,
    is_deleted,
    deleted_at,
    deleted_by,
    transaction_id,
    version
)
SELECT
    nextval('tenant_core.schedule_ingestion_channel_seq'),
    profile.id,
    'ASM',
    'MQ',
    'JMS',
    'tcp://mq.codeshare.local:61616',
    'schedule.asm.inbound.qr',
    2,
    6,
    TRUE,
    1000,
    100,
    5000,
    12,
    30000,
    5,
    5000,
    FALSE,
    4,
    CURRENT_TIMESTAMP,
    'FLYWAY',
    CURRENT_TIMESTAMP,
    'FLYWAY',
    TRUE,
    FALSE,
    NULL,
    NULL,
    'FLYWAY',
    0
FROM profile
WHERE NOT EXISTS (
    SELECT 1
    FROM tenant_core.schedule_ingestion_channel sic
    WHERE sic.profile_id = profile.id
      AND sic.message_type = 'ASM'
      AND sic.source_type = 'MQ'
);

------------------------------------------------------------
-- SSM Local Channel
------------------------------------------------------------
WITH profile AS (
    SELECT sip.id
    FROM tenant_core.schedule_ingestion_profile sip
    JOIN tenant_core.tenants t ON sip.tenant_id = t.id
    WHERE t.tenant_code = 'QR'
)
INSERT INTO tenant_core.schedule_ingestion_channel (
    id,
    profile_id,
    message_type,
    source_type,
    remote_directory,
    file_noop,
    file_delete,
    file_include_pattern,
    file_exclude_pattern,
    file_read_lock,
    file_read_lock_min_age,
    file_read_lock_timeout,
    file_read_lock_check_interval,
    file_poll_delay_ms,
    file_initial_delay_ms,
    file_move,
    file_move_failed,
    file_pre_move,
    file_idempotent,
    file_idempotent_key,
    file_charset,
    max_messages_per_poll,
    is_recursive,
    bridge_error_handler,
    disconnect_flag,
    is_binary,
    passive_mode,
    reconnect_delay_ms,
    max_reconnect_attempts,
    so_timeout_ms,
    retry_attempts,
    retry_delay_ms,
    is_enabled,
    priority,
    created_at,
    created_by,
    updated_at,
    updated_by,
    active,
    is_deleted,
    deleted_at,
    deleted_by,
    transaction_id,
    version
)
SELECT
    nextval('tenant_core.schedule_ingestion_channel_seq'),
    profile.id,
    'SSM',
    'LOCAL',
    'C:/Users/mdwas/Projects/messages/qr/ssm/inbound',
    FALSE,
    FALSE,
    '(?i).*[.](ssm|txt)',
    '*.tmp,*.bak',
    'changed',
    '2s',
    60000,
    1000,
    60000,
    2000,
    '.processed/${date:now:yyyyMMdd}/${file:name}',
    '.error/${date:now:yyyyMMdd}/${file:name}',
    '.inprogress/${file:name}',
    TRUE,
    '${file:absolute.path}-${file:modified}',
    'UTF-8',
    10,
    FALSE,
    TRUE,
    TRUE,
    FALSE,
    FALSE,
    5000,
    12,
    30000,
    5,
    10000,
    FALSE,
    5,
    CURRENT_TIMESTAMP,
    'FLYWAY',
    CURRENT_TIMESTAMP,
    'FLYWAY',
    TRUE,
    FALSE,
    NULL,
    NULL,
    'FLYWAY',
    0
FROM profile
WHERE NOT EXISTS (
    SELECT 1
    FROM tenant_core.schedule_ingestion_channel sic
    WHERE sic.profile_id = profile.id
      AND sic.message_type = 'SSM'
      AND sic.source_type = 'LOCAL'
);

------------------------------------------------------------
-- SSM MQ Channel
------------------------------------------------------------
WITH profile AS (
    SELECT sip.id
    FROM tenant_core.schedule_ingestion_profile sip
    JOIN tenant_core.tenants t ON sip.tenant_id = t.id
    WHERE t.tenant_code = 'QR'
)
INSERT INTO tenant_core.schedule_ingestion_channel (
    id,
    profile_id,
    message_type,
    source_type,
    protocol,
    broker_url,
    queue_name,
    concurrent_consumers,
    max_concurrent_consumers,
    async_consumer,
    receive_timeout_ms,
    max_messages_per_task,
    reconnect_delay_ms,
    max_reconnect_attempts,
    so_timeout_ms,
    retry_attempts,
    retry_delay_ms,
    is_enabled,
    priority,
    created_at,
    created_by,
    updated_at,
    updated_by,
    active,
    is_deleted,
    deleted_at,
    deleted_by,
    transaction_id,
    version
)
SELECT
    nextval('tenant_core.schedule_ingestion_channel_seq'),
    profile.id,
    'SSM',
    'MQ',
    'JMS',
    'tcp://mq.codeshare.local:61616',
    'schedule.ssm.inbound.qr',
    2,
    6,
    TRUE,
    1000,
    100,
    5000,
    12,
    30000,
    5,
    5000,
    FALSE,
    6,
    CURRENT_TIMESTAMP,
    'FLYWAY',
    CURRENT_TIMESTAMP,
    'FLYWAY',
    TRUE,
    FALSE,
    NULL,
    NULL,
    'FLYWAY',
    0
FROM profile
WHERE NOT EXISTS (
    SELECT 1
    FROM tenant_core.schedule_ingestion_channel sic
    WHERE sic.profile_id = profile.id
      AND sic.message_type = 'SSM'
      AND sic.source_type = 'MQ'
);
