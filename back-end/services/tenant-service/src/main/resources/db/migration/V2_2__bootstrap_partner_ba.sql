-- V2_2__bootstrap_partner_ba.sql
-- Seeds the British Airways partner aggregate for tenant QR

------------------------------------------------------------
-- Codeshare Partner (BA)
------------------------------------------------------------
WITH tenant_ctx AS (
    SELECT id AS tenant_id FROM tenant_core.tenants WHERE tenant_code = 'QR'
),
home_airline AS (
    SELECT id AS home_airline_id, iata_code AS home_iata, display_name AS home_display
    FROM schedule_master_data.airline_carrier
    WHERE iata_code = 'QR'
),
partner_airline AS (
    SELECT id AS partner_airline_id, iata_code AS partner_iata, display_name AS partner_display
    FROM schedule_master_data.airline_carrier
    WHERE iata_code = 'BA'
)
INSERT INTO tenant_core.codeshare_partner (
    id,
    tenant_id,
    home_airline_id,
    partner_airline_id,
    agreement_number,
    agreement_type,
    agreement_status,
    active,
    display_order,
    description,
    remarks,
    status,
    effective_from,
    effective_to,
    created_at,
    created_by,
    updated_at,
    updated_by,
    is_deleted,
    deleted_at,
    deleted_by,
    transaction_id
)
SELECT
    nextval('tenant_core.codeshare_partner_seq'),
    tc.tenant_id,
    ha.home_airline_id,
    pa.partner_airline_id,
    concat(ha.home_iata, '-', pa.partner_iata, '-2025'),
    'BILATERAL',
    'ACTIVE',
    TRUE,
    1,
    concat(ha.home_display, ' and ', pa.partner_display, ' tenant partner mapping.'),
    'Bootstrap-managed bilateral partner mapping for schedule, inventory, and operational distribution.',
    'ACTIVE',
    DATE '2025-01-01',
    DATE '2035-01-01',
    CURRENT_TIMESTAMP,
    'FLYWAY',
    CURRENT_TIMESTAMP,
    'FLYWAY',
    FALSE,
    NULL,
    NULL,
    'FLYWAY'
FROM tenant_ctx tc
CROSS JOIN home_airline ha
CROSS JOIN partner_airline pa
WHERE NOT EXISTS (
    SELECT 1
    FROM tenant_core.codeshare_partner cp
    WHERE cp.tenant_id = tc.tenant_id
      AND cp.home_airline_id = ha.home_airline_id
      AND cp.partner_airline_id = pa.partner_airline_id
);

------------------------------------------------------------
-- Partner Profile (BA)
------------------------------------------------------------
WITH partner_ctx AS (
    SELECT
        cp.id AS partner_id,
        cp.display_order,
        ha.iata_code AS home_iata,
        pa.iata_code AS partner_iata,
        pa.display_name AS partner_display
    FROM tenant_core.codeshare_partner cp
    JOIN tenant_core.tenants t ON cp.tenant_id = t.id
    JOIN schedule_master_data.airline_carrier ha ON cp.home_airline_id = ha.id
    JOIN schedule_master_data.airline_carrier pa ON cp.partner_airline_id = pa.id
    WHERE t.tenant_code = 'QR'
      AND ha.iata_code = 'QR'
      AND pa.iata_code = 'BA'
)
INSERT INTO tenant_core.codeshare_partner_profile (
    id,
    partner_id,
    profile_code,
    profile_name,
    partner_type,
    agreement_category,
    inventory_sharing_type,
    priority,
    auto_accept_schedule_changes,
    proration_applicable,
    e_ticket_allowed,
    active,
    display_order,
    description,
    remarks,
    status,
    effective_from,
    effective_to,
    created_at,
    created_by,
    updated_at,
    updated_by,
    is_deleted,
    deleted_at,
    deleted_by,
    transaction_id
)
SELECT
    nextval('tenant_core.codeshare_partner_profile_seq'),
    pc.partner_id,
    concat(pc.home_iata, '-', pc.partner_iata, '-CORE'),
    concat(pc.partner_display, ' Core Codeshare Profile'),
    'RECIPROCAL',
    'BILATERAL',
    'FREE_SALE',
    pc.display_order,
    FALSE,
    TRUE,
    TRUE,
    TRUE,
    pc.display_order,
    concat('Bootstrap-managed operating profile for ', pc.partner_display, '.'),
    'Used for partner-level schedule evaluation and default inventory sharing rules.',
    'ACTIVE',
    DATE '2025-01-01',
    DATE '2035-01-01',
    CURRENT_TIMESTAMP,
    'FLYWAY',
    CURRENT_TIMESTAMP,
    'FLYWAY',
    FALSE,
    NULL,
    NULL,
    'FLYWAY'
FROM partner_ctx pc
WHERE NOT EXISTS (
    SELECT 1
    FROM tenant_core.codeshare_partner_profile cpp
    WHERE cpp.partner_id = pc.partner_id
      AND cpp.profile_code = concat(pc.home_iata, '-', pc.partner_iata, '-CORE')
);

------------------------------------------------------------
-- Communication Profile (BA)
------------------------------------------------------------
WITH partner_ctx AS (
    SELECT
        cp.id AS partner_id,
        cp.display_order,
        ha.iata_code AS home_iata,
        lower(ha.iata_code) AS home_iata_lower,
        pa.iata_code AS partner_iata,
        lower(pa.iata_code) AS partner_iata_lower,
        pa.display_name AS partner_display
    FROM tenant_core.codeshare_partner cp
    JOIN tenant_core.tenants t ON cp.tenant_id = t.id
    JOIN schedule_master_data.airline_carrier ha ON cp.home_airline_id = ha.id
    JOIN schedule_master_data.airline_carrier pa ON cp.partner_airline_id = pa.id
    WHERE t.tenant_code = 'QR'
      AND ha.iata_code = 'QR'
      AND pa.iata_code = 'BA'
)
INSERT INTO tenant_core.codeshare_partner_communication_profile (
    id,
    partner_id,
    profile_code,
    profile_name,
    protocol,
    transport_type,
    message_format,
    authentication_type,
    endpoint_url,
    username,
    credential_alias,
    connection_timeout,
    read_timeout,
    retry_count,
    compression_enabled,
    encryption_enabled,
    active,
    display_order,
    description,
    remarks,
    status,
    effective_from,
    effective_to,
    created_at,
    created_by,
    updated_at,
    updated_by,
    is_deleted,
    deleted_at,
    deleted_by,
    transaction_id
)
SELECT
    nextval('tenant_core.codeshare_partner_communication_profile_seq'),
    pc.partner_id,
    concat(pc.home_iata, '-', pc.partner_iata, '-COMM'),
    concat(pc.partner_display, ' Schedule Communication Profile'),
    'API',
    'HTTPS',
    'JSON',
    'API_KEY',
    concat('https://api.', pc.partner_iata_lower, '.codeshare.local/schedules'),
    concat(pc.home_iata_lower, '_schedule_exchange'),
    concat('tenant/qr/partners/', pc.partner_iata_lower, '/api-key'),
    10000,
    30000,
    5,
    TRUE,
    TRUE,
    TRUE,
    pc.display_order,
    'Bootstrap-managed partner communication profile for schedule exchange.',
    concat('Primary communication route for SSIM, ASM, and SSM exchange with ', pc.partner_display, '.'),
    'ACTIVE',
    DATE '2025-01-01',
    DATE '2035-01-01',
    CURRENT_TIMESTAMP,
    'FLYWAY',
    CURRENT_TIMESTAMP,
    'FLYWAY',
    FALSE,
    NULL,
    NULL,
    'FLYWAY'
FROM partner_ctx pc
WHERE NOT EXISTS (
    SELECT 1
    FROM tenant_core.codeshare_partner_communication_profile cpcp
    WHERE cpcp.partner_id = pc.partner_id
      AND cpcp.profile_code = concat(pc.home_iata, '-', pc.partner_iata, '-COMM')
);

------------------------------------------------------------
-- Distribution Profile (BA - SSIM)
------------------------------------------------------------
WITH partner_ctx AS (
    SELECT
        cp.id AS partner_id,
        cp.display_order,
        ha.iata_code AS home_iata,
        pa.iata_code AS partner_iata,
        pa.display_name AS partner_display
    FROM tenant_core.codeshare_partner cp
    JOIN tenant_core.tenants t ON cp.tenant_id = t.id
    JOIN schedule_master_data.airline_carrier ha ON cp.home_airline_id = ha.id
    JOIN schedule_master_data.airline_carrier pa ON cp.partner_airline_id = pa.id
    WHERE t.tenant_code = 'QR'
      AND ha.iata_code = 'QR'
      AND pa.iata_code = 'BA'
)
INSERT INTO tenant_core.codeshare_partner_distribution_profile (
    id,
    partner_id,
    profile_code,
    profile_name,
    distribution_channel,
    distribution_mode,
    message_type,
    real_time_enabled,
    acknowledgement_required,
    retry_enabled,
    retry_count,
    active,
    display_order,
    description,
    remarks,
    status,
    effective_from,
    effective_to,
    created_at,
    created_by,
    updated_at,
    updated_by,
    is_deleted,
    deleted_at,
    deleted_by,
    transaction_id
)
SELECT
    nextval('tenant_core.codeshare_partner_distribution_profile_seq'),
    pc.partner_id,
    concat(pc.home_iata, '-', pc.partner_iata, '-DIST-SSIM'),
    concat(pc.partner_display, ' SSIM Distribution Profile'),
    'SFTP',
    'SCHEDULED',
    'SSIM',
    FALSE,
    TRUE,
    TRUE,
    5,
    TRUE,
    1,
    concat('Bootstrap-managed SSIM distribution profile for ', pc.partner_display, '.'),
    'Default outbound SSIM routing profile for bootstrap tenant data.',
    'ACTIVE',
    DATE '2025-01-01',
    DATE '2035-01-01',
    CURRENT_TIMESTAMP,
    'FLYWAY',
    CURRENT_TIMESTAMP,
    'FLYWAY',
    FALSE,
    NULL,
    NULL,
    'FLYWAY'
FROM partner_ctx pc
WHERE NOT EXISTS (
    SELECT 1
    FROM tenant_core.codeshare_partner_distribution_profile cpdp
    WHERE cpdp.partner_id = pc.partner_id
      AND cpdp.profile_code = concat(pc.home_iata, '-', pc.partner_iata, '-DIST-SSIM')
);

------------------------------------------------------------
-- Distribution Profile (BA - ASM)
------------------------------------------------------------
WITH partner_ctx AS (
    SELECT
        cp.id AS partner_id,
        cp.display_order,
        ha.iata_code AS home_iata,
        pa.iata_code AS partner_iata,
        pa.display_name AS partner_display
    FROM tenant_core.codeshare_partner cp
    JOIN tenant_core.tenants t ON cp.tenant_id = t.id
    JOIN schedule_master_data.airline_carrier ha ON cp.home_airline_id = ha.id
    JOIN schedule_master_data.airline_carrier pa ON cp.partner_airline_id = pa.id
    WHERE t.tenant_code = 'QR'
      AND ha.iata_code = 'QR'
      AND pa.iata_code = 'BA'
)
INSERT INTO tenant_core.codeshare_partner_distribution_profile (
    id,
    partner_id,
    profile_code,
    profile_name,
    distribution_channel,
    distribution_mode,
    message_type,
    real_time_enabled,
    acknowledgement_required,
    retry_enabled,
    retry_count,
    active,
    display_order,
    description,
    remarks,
    status,
    effective_from,
    effective_to,
    created_at,
    created_by,
    updated_at,
    updated_by,
    is_deleted,
    deleted_at,
    deleted_by,
    transaction_id
)
SELECT
    nextval('tenant_core.codeshare_partner_distribution_profile_seq'),
    pc.partner_id,
    concat(pc.home_iata, '-', pc.partner_iata, '-DIST-ASM'),
    concat(pc.partner_display, ' ASM Distribution Profile'),
    'MQ',
    'REAL_TIME',
    'ASM',
    TRUE,
    TRUE,
    TRUE,
    5,
    TRUE,
    2,
    concat('Bootstrap-managed ASM distribution profile for ', pc.partner_display, '.'),
    'Default outbound ASM routing profile for bootstrap tenant data.',
    'ACTIVE',
    DATE '2025-01-01',
    DATE '2035-01-01',
    CURRENT_TIMESTAMP,
    'FLYWAY',
    CURRENT_TIMESTAMP,
    'FLYWAY',
    FALSE,
    NULL,
    NULL,
    'FLYWAY'
FROM partner_ctx pc
WHERE NOT EXISTS (
    SELECT 1
    FROM tenant_core.codeshare_partner_distribution_profile cpdp
    WHERE cpdp.partner_id = pc.partner_id
      AND cpdp.profile_code = concat(pc.home_iata, '-', pc.partner_iata, '-DIST-ASM')
);

------------------------------------------------------------
-- Distribution Profile (BA - SSM)
------------------------------------------------------------
WITH partner_ctx AS (
    SELECT
        cp.id AS partner_id,
        cp.display_order,
        ha.iata_code AS home_iata,
        pa.iata_code AS partner_iata,
        pa.display_name AS partner_display
    FROM tenant_core.codeshare_partner cp
    JOIN tenant_core.tenants t ON cp.tenant_id = t.id
    JOIN schedule_master_data.airline_carrier ha ON cp.home_airline_id = ha.id
    JOIN schedule_master_data.airline_carrier pa ON cp.partner_airline_id = pa.id
    WHERE t.tenant_code = 'QR'
      AND ha.iata_code = 'QR'
      AND pa.iata_code = 'BA'
)
INSERT INTO tenant_core.codeshare_partner_distribution_profile (
    id,
    partner_id,
    profile_code,
    profile_name,
    distribution_channel,
    distribution_mode,
    message_type,
    real_time_enabled,
    acknowledgement_required,
    retry_enabled,
    retry_count,
    active,
    display_order,
    description,
    remarks,
    status,
    effective_from,
    effective_to,
    created_at,
    created_by,
    updated_at,
    updated_by,
    is_deleted,
    deleted_at,
    deleted_by,
    transaction_id
)
SELECT
    nextval('tenant_core.codeshare_partner_distribution_profile_seq'),
    pc.partner_id,
    concat(pc.home_iata, '-', pc.partner_iata, '-DIST-SSM'),
    concat(pc.partner_display, ' SSM Distribution Profile'),
    'MQ',
    'REAL_TIME',
    'SSM',
    TRUE,
    TRUE,
    TRUE,
    5,
    TRUE,
    3,
    concat('Bootstrap-managed SSM distribution profile for ', pc.partner_display, '.'),
    'Default outbound SSM routing profile for bootstrap tenant data.',
    'ACTIVE',
    DATE '2025-01-01',
    DATE '2035-01-01',
    CURRENT_TIMESTAMP,
    'FLYWAY',
    CURRENT_TIMESTAMP,
    'FLYWAY',
    FALSE,
    NULL,
    NULL,
    'FLYWAY'
FROM partner_ctx pc
WHERE NOT EXISTS (
    SELECT 1
    FROM tenant_core.codeshare_partner_distribution_profile cpdp
    WHERE cpdp.partner_id = pc.partner_id
      AND cpdp.profile_code = concat(pc.home_iata, '-', pc.partner_iata, '-DIST-SSM')
);
