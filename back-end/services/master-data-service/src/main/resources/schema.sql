CREATE SCHEMA IF NOT EXISTS schedule_master_data;

CREATE SEQUENCE IF NOT EXISTS schedule_master_data.common_reference_option_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS schedule_master_data.action_code_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS schedule_master_data.action_identifier_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS schedule_master_data.aircraft_cabin_layout_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS schedule_master_data.aircraft_configuration_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS schedule_master_data.aircraft_configuration_revision_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS schedule_master_data.aircraft_family_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS schedule_master_data.aircraft_manufacturer_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS schedule_master_data.aircraft_owner_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS schedule_master_data.aircraft_registration_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS schedule_master_data.aircraft_type_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS schedule_master_data.airline_alias_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS schedule_master_data.airline_business_role_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS schedule_master_data.airline_carrier_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS schedule_master_data.airline_contact_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS schedule_master_data.airline_fleet_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS schedule_master_data.airport_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS schedule_master_data.airport_terminal_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS schedule_master_data.alliance_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS schedule_master_data.alliance_member_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS schedule_master_data.cabin_configuration_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS schedule_master_data.cabin_crew_employer_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS schedule_master_data.city_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS schedule_master_data.cockpit_crew_employer_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS schedule_master_data.country_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS schedule_master_data.data_element_identifier_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS schedule_master_data.daylight_saving_rule_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS schedule_master_data.dei_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS schedule_master_data.distribution_channel_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS schedule_master_data.electronic_ticket_indicator_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS schedule_master_data.flight_frequency_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS schedule_master_data.flight_suffix_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS schedule_master_data.meal_service_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS schedule_master_data.message_priority_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS schedule_master_data.message_status_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS schedule_master_data.operational_suffix_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS schedule_master_data.region_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS schedule_master_data.reject_reason_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS schedule_master_data.reservation_booking_designator_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS schedule_master_data.reservation_booking_modifier_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS schedule_master_data.schedule_category_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS schedule_master_data.schedule_channel_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS schedule_master_data.schedule_message_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS schedule_master_data.schedule_priority_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS schedule_master_data.schedule_source_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS schedule_master_data.schedule_status_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS schedule_master_data.schedule_type_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS schedule_master_data.season_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS schedule_master_data.secure_flight_indicator_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS schedule_master_data.service_type_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS schedule_master_data.standard_message_identifier_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS schedule_master_data.state_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS schedule_master_data.terminal_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS schedule_master_data.time_mode_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS schedule_master_data.timezone_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS schedule_master_data.timezone_dst_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS schedule_master_data.traffic_conference_area_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS schedule_master_data.traffic_restriction_code_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS schedule_master_data.traffic_restriction_qualifier_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS schedule_master_data.utc_offset_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE IF NOT EXISTS schedule_master_data.common_reference_option (
    id BIGINT PRIMARY KEY,
    category_code VARCHAR(100) NOT NULL,
    option_code VARCHAR(100) NOT NULL,
    option_label VARCHAR(200) NOT NULL,
    description VARCHAR(500),
    display_order INTEGER,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    created_by VARCHAR(100),
    updated_at TIMESTAMP,
    updated_by VARCHAR(100),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at TIMESTAMP,
    deleted_by VARCHAR(100),
    transaction_id VARCHAR(50),
    CONSTRAINT uk_common_reference_option UNIQUE (category_code, option_code)
);

CREATE INDEX IF NOT EXISTS idx_common_reference_option_category
    ON schedule_master_data.common_reference_option (category_code);

CREATE INDEX IF NOT EXISTS idx_common_reference_option_status
    ON schedule_master_data.common_reference_option (status);
