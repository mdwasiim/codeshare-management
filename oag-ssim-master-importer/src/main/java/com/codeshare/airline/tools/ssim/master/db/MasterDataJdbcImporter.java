package com.codeshare.airline.tools.ssim.master.db;

import com.codeshare.airline.tools.ssim.master.catalog.MasterCatalog;
import com.codeshare.airline.tools.ssim.master.model.ImportConfig;
import com.codeshare.airline.tools.ssim.master.model.MasterCodeSet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.util.Set;

public final class MasterDataJdbcImporter {
    private final Connection connection;
    private final ImportConfig config;
    private final String schema;

    public MasterDataJdbcImporter(Connection connection, ImportConfig config) {
        this.connection = connection;
        this.config = config;
        this.schema = quoteIdent(config.schema());
    }

    public void importAll(MasterCodeSet codes) throws SQLException {
        ensureReferenceParents();

        for (String code : codes.timeModes) {
            upsertTimeMode(code);
        }
        for (String code : codes.seasons) {
            upsertSeason(code);
        }
        for (String code : codes.airlines) {
            upsertAirline(code);
        }
        for (String code : codes.airports) {
            upsertAirport(code);
        }
        for (String value : codes.terminals) {
            String[] parts = value.split(":", 2);
            upsertTerminal(parts[0], parts[1]);
        }
        for (String code : codes.aircraftTypes) {
            upsertAircraftType(code);
        }
        for (String code : codes.aircraftOwners) {
            upsertAircraftOwner(code);
        }
        for (String code : codes.cockpitCrewEmployers) {
            upsertCrewEmployer("COCKPIT_CREW_EMPLOYER", "cockpit_crew_employer_seq", code);
        }
        for (String code : codes.cabinCrewEmployers) {
            upsertCrewEmployer("CABIN_CREW_EMPLOYER", "cabin_crew_employer_seq", code);
        }
        for (String code : codes.serviceTypes) {
            upsertSimpleCode("SERVICE_TYPE", "service_type_seq", "SERVICE_TYPE_CODE", "SERVICE_TYPE_NAME",
                    code, MasterCatalog.name(MasterCatalog.SERVICE_TYPES, code, "SSIM service type"));
        }
        for (String code : codes.reservationBookingDesignators) {
            upsertBookingDesignator(code);
        }
        for (String code : codes.reservationBookingModifiers) {
            upsertSimpleCode("RESERVATION_BOOKING_MODIFIER", "reservation_booking_modifier_seq",
                    "MODIFIER_CODE", "MODIFIER_NAME", code, "SSIM booking modifier " + code);
        }
        for (String code : codes.mealServices) {
            upsertSimpleCode("MEAL_SERVICE", "meal_service_seq", "MEAL_CODE", "MEAL_NAME",
                    code, "SSIM meal service " + code);
        }
        for (String code : codes.secureFlightIndicators) {
            upsertSimpleCode("SECURE_FLIGHT_INDICATOR", "secure_flight_indicator_seq",
                    "SECURE_FLIGHT_INDICATOR_CODE", "SECURE_FLIGHT_INDICATOR_NAME",
                    code, MasterCatalog.name(MasterCatalog.SECURE_FLIGHT, code, "SSIM secure flight indicator"));
        }
        for (String code : codes.operationalSuffixes) {
            upsertOperationalSuffix(code);
        }
        for (String code : codes.flightFrequencies) {
            upsertFlightFrequency(code);
        }
        for (String code : codes.trafficRestrictionCodes) {
            upsertSimpleCode("TRAFFIC_RESTRICTION_CODE", "traffic_restriction_code_seq",
                    "RESTRICTION_CODE", "RESTRICTION_NAME", code, "SSIM traffic restriction " + code);
        }
        for (String code : codes.dataElementIdentifiers) {
            upsertDataElementIdentifier(code);
            upsertDeiRegistry(code);
        }

        repairImporterRelationships();
    }

    private void ensureReferenceParents() throws SQLException {
        upsertRegion();
        upsertCountry();
        upsertTimezone();
        upsertCity("SSIM", "SSIM reference city", "SSM");
        upsertAircraftManufacturer();
        upsertAircraftFamily();
    }

    private void upsertRegion() throws SQLException {
        if (existsBy("REGION", "REGION_CODE", "SSIM")) {
            return;
        }
        execute("""
                INSERT INTO %s.REGION (id, REGION_CODE, REGION_NAME, STATUS, created_at, created_by, updated_at, updated_by, active, is_deleted, transaction_id)
                VALUES (nextval('%s.region_seq'), 'SSIM', 'SSIM placeholder region', 'ACTIVE', now(), ?, now(), ?, true, false, 'SSIM_IMPORT')
                """.formatted(schema, config.schema()), config.auditUser(), config.auditUser());
    }

    private void upsertCountry() throws SQLException {
        if (existsBy("COUNTRY", "ISO2_CODE", "ZZ")) {
            return;
        }
        Long regionId = idBy("REGION", "REGION_CODE", "SSIM");
        execute("""
                INSERT INTO %s.COUNTRY (id, ISO2_CODE, ISO3_CODE, COUNTRY_NAME, REGION_ID, STATUS, created_at, created_by, updated_at, updated_by, active, is_deleted, transaction_id)
                VALUES (nextval('%s.country_seq'), 'ZZ', 'ZZZ', 'SSIM placeholder country', ?, 'ACTIVE', now(), ?, now(), ?, true, false, 'SSIM_IMPORT')
                """.formatted(schema, config.schema()), regionId, config.auditUser(), config.auditUser());
    }

    private void upsertTimezone() throws SQLException {
        if (existsBy("TIMEZONE", "TZ_IDENTIFIER", "UTC")) {
            return;
        }
        execute("""
                INSERT INTO %s.TIMEZONE (id, TZ_IDENTIFIER, STANDARD_UTC_OFFSET_MIN, OBSERVES_DST, STATUS, created_at, created_by, updated_at, updated_by, active, is_deleted, transaction_id)
                VALUES (nextval('%s.timezone_seq'), 'UTC', 0, false, 'ACTIVE', now(), ?, now(), ?, true, false, 'SSIM_IMPORT')
                """.formatted(schema, config.schema()), config.auditUser(), config.auditUser());
    }

    private void upsertCity(String key, String name, String iataCode) throws SQLException {
        Long countryId = idBy("COUNTRY", "ISO2_CODE", "ZZ");
        if (existsCity(name + " " + key, countryId)) {
            return;
        }
        execute("""
                INSERT INTO %s.CITY (id, CITY_NAME, IATA_CITY_CODE, COUNTRY_ID, STATUS, created_at, created_by, updated_at, updated_by, active, is_deleted, transaction_id)
                VALUES (nextval('%s.city_seq'), ?, ?, ?, 'ACTIVE', now(), ?, now(), ?, true, false, 'SSIM_IMPORT')
                """.formatted(schema, config.schema()), name + " " + key, iataCode, countryId, config.auditUser(), config.auditUser());
    }

    private void upsertAircraftManufacturer() throws SQLException {
        if (existsBy("AIRCRAFT_MANUFACTURER", "MANUFACTURER_CODE", "SSIM")) {
            return;
        }
        Long countryId = idBy("COUNTRY", "ISO2_CODE", "ZZ");
        execute("""
                INSERT INTO %s.AIRCRAFT_MANUFACTURER (id, MANUFACTURER_CODE, MANUFACTURER_NAME, SHORT_NAME, COUNTRY_ID, ACTIVE, DISPLAY_ORDER, STATUS, created_at, created_by, updated_at, updated_by, is_deleted, transaction_id)
                VALUES (nextval('%s.aircraft_manufacturer_seq'), 'SSIM', 'SSIM placeholder manufacturer', 'SSIM', ?, true, 1, 'ACTIVE', now(), ?, now(), ?, false, 'SSIM_IMPORT')
                """.formatted(schema, config.schema()), countryId, config.auditUser(), config.auditUser());
    }

    private void upsertAircraftFamily() throws SQLException {
        if (existsBy("AIRCRAFT_FAMILY", "FAMILY_CODE", "SSIM")) {
            return;
        }
        Long manufacturerId = idBy("AIRCRAFT_MANUFACTURER", "MANUFACTURER_CODE", "SSIM");
        execute("""
                INSERT INTO %s.AIRCRAFT_FAMILY (id, FAMILY_CODE, FAMILY_NAME, MANUFACTURER_ID, ACTIVE, DISPLAY_ORDER, STATUS, created_at, created_by, updated_at, updated_by, is_deleted, transaction_id)
                VALUES (nextval('%s.aircraft_family_seq'), 'SSIM', 'SSIM placeholder aircraft family', ?, true, 1, 'ACTIVE', now(), ?, now(), ?, false, 'SSIM_IMPORT')
                """.formatted(schema, config.schema()), manufacturerId, config.auditUser(), config.auditUser());
    }

    private void upsertTimeMode(String code) throws SQLException {
        upsertSimpleCode("TIME_MODE", "time_mode_seq", "TIME_MODE_CODE", "TIME_MODE_NAME",
                code, MasterCatalog.name(MasterCatalog.TIME_MODES, code, "SSIM time mode"));
    }

    private void upsertSeason(String code) throws SQLException {
        if (existsBy("SEASON", "SEASON_CODE", code)) {
            return;
        }
        int year = parseSeasonYear(code);
        boolean winter = code.startsWith("W");
        LocalDate start = winter ? LocalDate.of(year, Month.OCTOBER, 1) : LocalDate.of(year, Month.MARCH, 1);
        LocalDate end = winter ? LocalDate.of(year + 1, Month.MARCH, 31) : LocalDate.of(year, Month.OCTOBER, 31);
        String type = winter ? "WINTER" : "SUMMER";
        execute("""
                INSERT INTO %s.SEASON (id, SEASON_CODE, SEASON_NAME, SEASON_TYPE, SCHEDULE_YEAR, SEASON_START_DATE, SEASON_END_DATE, ACTIVE, DISPLAY_ORDER, STATUS, created_at, created_by, updated_at, updated_by, is_deleted, transaction_id)
                VALUES (nextval('%s.season_seq'), ?, ?, ?, ?, ?, ?, true, 1, 'ACTIVE', now(), ?, now(), ?, false, 'SSIM_IMPORT')
                """.formatted(schema, config.schema()), code, type + " " + year, type, year, start, end, config.auditUser(), config.auditUser());
    }

    private int parseSeasonYear(String code) {
        if (code.length() == 3 && Character.isDigit(code.charAt(1)) && Character.isDigit(code.charAt(2))) {
            int yy = Integer.parseInt(code.substring(1));
            return yy >= 70 ? 1900 + yy : 2000 + yy;
        }
        return LocalDate.now().getYear();
    }

    private void upsertAirline(String code) throws SQLException {
        if (existsBy("AIRLINE_CARRIER", "IATA_CODE", code)) {
            return;
        }
        Long countryId = idBy("COUNTRY", "ISO2_CODE", "ZZ");
        String icao = ("X" + code).substring(0, 3);
        execute("""
                INSERT INTO %s.AIRLINE_CARRIER (id, IATA_CODE, ICAO_CODE, LEGAL_NAME, COMMERCIAL_NAME, DISPLAY_NAME, COUNTRY_ID, ACTIVE, DISPLAY_ORDER, STATUS, created_at, created_by, updated_at, updated_by, is_deleted, transaction_id)
                VALUES (nextval('%s.airline_carrier_seq'), ?, ?, ?, ?, ?, ?, true, 1, 'ACTIVE', now(), ?, now(), ?, false, 'SSIM_IMPORT')
                """.formatted(schema, config.schema()), code, icao, "SSIM airline " + code, "SSIM airline " + code, "SSIM airline " + code, countryId, config.auditUser(), config.auditUser());
    }

    private void upsertAirport(String code) throws SQLException {
        if (existsBy("AIRPORT", "IATA_CODE", code)) {
            return;
        }
        upsertCity(code, "SSIM station city", code);
        Long cityId = idBy("CITY", "CITY_NAME", "SSIM station city " + code);
        Long countryId = idBy("COUNTRY", "ISO2_CODE", "ZZ");
        Long timezoneId = idBy("TIMEZONE", "TZ_IDENTIFIER", "UTC");
        execute("""
                INSERT INTO %s.AIRPORT (id, IATA_CODE, ICAO_CODE, AIRPORT_NAME, CITY_ID, COUNTRY_ID, TIMEZONE_ID, LATITUDE, LONGITUDE, IS_INTERNATIONAL, IS_HUB, STATUS, created_at, created_by, updated_at, updated_by, active, is_deleted, transaction_id)
                VALUES (nextval('%s.airport_seq'), ?, ?, ?, ?, ?, ?, 0, 0, false, false, 'ACTIVE', now(), ?, now(), ?, true, false, 'SSIM_IMPORT')
                """.formatted(schema, config.schema()), code, "Z" + code, "SSIM airport/station " + code, cityId, countryId, timezoneId, config.auditUser(), config.auditUser());
    }

    private void upsertTerminal(String airportCode, String terminalCode) throws SQLException {
        Long airportId = idBy("AIRPORT", "IATA_CODE", airportCode);
        if (existsTerminal(airportId, terminalCode)) {
            return;
        }
        execute("""
                INSERT INTO %s.TERMINAL (id, AIRPORT_ID, TERMINAL_CODE, TERMINAL_NAME, TERMINAL_TYPE, INTERNATIONAL_FLAG, STATUS_CODE, created_at, created_by, updated_at, updated_by, active, is_deleted, transaction_id)
                VALUES (nextval('%s.terminal_seq'), ?, ?, ?, 'BOTH', false, 'ACTIVE', now(), ?, now(), ?, true, false, 'SSIM_IMPORT')
                """.formatted(schema, config.schema()),
                airportId, terminalCode, airportCode + " terminal " + terminalCode,
                config.auditUser(), config.auditUser());
    }

    private void upsertAircraftType(String code) throws SQLException {
        if (existsBy("AIRCRAFT_TYPE", "IATA_CODE", code)) {
            return;
        }
        Long familyId = idBy("AIRCRAFT_FAMILY", "FAMILY_CODE", "SSIM");
        execute("""
                INSERT INTO %s.AIRCRAFT_TYPE (id, AIRCRAFT_FAMILY_ID, AIRCRAFT_NAME, MODEL, ICAO_CODE, IATA_CODE, CATEGORY, WIDE_BODY, FREIGHTER, ETOPS_CERTIFIED, IN_PRODUCTION, ACTIVE, DISPLAY_ORDER, STATUS, created_at, created_by, updated_at, updated_by, is_deleted, transaction_id)
                VALUES (nextval('%s.aircraft_type_seq'), ?, ?, ?, ?, ?, 'PASSENGER', false, false, false, true, true, 1, 'ACTIVE', now(), ?, now(), ?, false, 'SSIM_IMPORT')
                """.formatted(schema, config.schema()), familyId, "SSIM aircraft type " + code, code, code, code, config.auditUser(), config.auditUser());
    }

    private void upsertAircraftOwner(String code) throws SQLException {
        if (existsBy("AIRCRAFT_OWNER", "OWNER_CODE", code)) {
            return;
        }
        Long countryId = idBy("COUNTRY", "ISO2_CODE", "ZZ");
        execute("""
                INSERT INTO %s.AIRCRAFT_OWNER (id, OWNER_CODE, OWNER_NAME, OWNER_TYPE, IATA_CODE, COUNTRY_ID, ACTIVE, DISPLAY_ORDER, STATUS, created_at, created_by, updated_at, updated_by, is_deleted, transaction_id)
                VALUES (nextval('%s.aircraft_owner_seq'), ?, ?, 'AIRLINE', ?, ?, true, 1, 'ACTIVE', now(), ?, now(), ?, false, 'SSIM_IMPORT')
                """.formatted(schema, config.schema()), code, "SSIM aircraft owner " + code, code, countryId, config.auditUser(), config.auditUser());
    }

    private void upsertCrewEmployer(String table, String sequence, String code) throws SQLException {
        if (existsBy(table, "EMPLOYER_CODE", code)) {
            return;
        }
        Long airlineId = optionalIdBy("AIRLINE_CARRIER", "IATA_CODE", code);
        Long countryId = idBy("COUNTRY", "ISO2_CODE", "ZZ");
        execute("""
                INSERT INTO %s.%s (id, EMPLOYER_CODE, EMPLOYER_NAME, EMPLOYER_TYPE, IATA_CODE, COUNTRY_ID, AIRLINE_ID, ACTIVE, DISPLAY_ORDER, STATUS, created_at, created_by, updated_at, updated_by, is_deleted, transaction_id)
                VALUES (nextval('%s.%s'), ?, ?, 'AIRLINE', ?, ?, ?, true, 1, 'ACTIVE', now(), ?, now(), ?, false, 'SSIM_IMPORT')
                """.formatted(schema, table, config.schema(), sequence),
                code, "SSIM crew employer " + code, code, countryId, airlineId, config.auditUser(), config.auditUser());
    }

    private void repairImporterRelationships() throws SQLException {
        Long countryId = idBy("COUNTRY", "ISO2_CODE", "ZZ");
        Long familyId = idBy("AIRCRAFT_FAMILY", "FAMILY_CODE", "SSIM");
        Long manufacturerId = idBy("AIRCRAFT_MANUFACTURER", "MANUFACTURER_CODE", "SSIM");

        execute("""
                UPDATE %s.AIRLINE_CARRIER
                SET COUNTRY_ID = ?, updated_at = now(), updated_by = ?
                WHERE COUNTRY_ID IS NULL AND transaction_id = 'SSIM_IMPORT'
                """.formatted(schema), countryId, config.auditUser());

        execute("""
                UPDATE %s.AIRCRAFT_MANUFACTURER
                SET COUNTRY_ID = ?, updated_at = now(), updated_by = ?
                WHERE COUNTRY_ID IS NULL AND transaction_id = 'SSIM_IMPORT'
                """.formatted(schema), countryId, config.auditUser());

        execute("""
                UPDATE %s.AIRCRAFT_FAMILY
                SET MANUFACTURER_ID = ?, updated_at = now(), updated_by = ?
                WHERE MANUFACTURER_ID IS NULL AND transaction_id = 'SSIM_IMPORT'
                """.formatted(schema), manufacturerId, config.auditUser());

        execute("""
                UPDATE %s.AIRCRAFT_TYPE
                SET AIRCRAFT_FAMILY_ID = ?, updated_at = now(), updated_by = ?
                WHERE AIRCRAFT_FAMILY_ID IS NULL AND transaction_id = 'SSIM_IMPORT'
                """.formatted(schema), familyId, config.auditUser());

        execute("""
                UPDATE %s.AIRCRAFT_OWNER
                SET COUNTRY_ID = ?, updated_at = now(), updated_by = ?
                WHERE COUNTRY_ID IS NULL AND transaction_id = 'SSIM_IMPORT'
                """.formatted(schema), countryId, config.auditUser());

        repairCrewEmployerRelationships("COCKPIT_CREW_EMPLOYER", countryId);
        repairCrewEmployerRelationships("CABIN_CREW_EMPLOYER", countryId);
    }

    private void repairCrewEmployerRelationships(String table, Long countryId) throws SQLException {
        execute("""
                UPDATE %s.%s employer
                SET COUNTRY_ID = ?, updated_at = now(), updated_by = ?
                WHERE employer.COUNTRY_ID IS NULL AND employer.transaction_id = 'SSIM_IMPORT'
                """.formatted(schema, table), countryId, config.auditUser());

        execute("""
                UPDATE %s.%s employer
                SET AIRLINE_ID = airline.id, updated_at = now(), updated_by = ?
                FROM %s.AIRLINE_CARRIER airline
                WHERE employer.AIRLINE_ID IS NULL
                  AND employer.IATA_CODE = airline.IATA_CODE
                  AND employer.transaction_id = 'SSIM_IMPORT'
                """.formatted(schema, table, schema), config.auditUser());
    }

    private void upsertBookingDesignator(String code) throws SQLException {
        if (existsBy("RESERVATION_BOOKING_DESIGNATOR", "BOOKING_DESIGNATOR", code)) {
            return;
        }
        execute("""
                INSERT INTO %s.RESERVATION_BOOKING_DESIGNATOR (id, BOOKING_DESIGNATOR, BOOKING_NAME, CABIN_CLASS, ACTIVE, DISPLAY_ORDER, STATUS, created_at, created_by, updated_at, updated_by, is_deleted, transaction_id)
                VALUES (nextval('%s.reservation_booking_designator_seq'), ?, ?, ?, true, 1, 'ACTIVE', now(), ?, now(), ?, false, 'SSIM_IMPORT')
                """.formatted(schema, config.schema()), code, "SSIM booking class " + code, cabinClassForRbd(code), config.auditUser(), config.auditUser());
    }

    private String cabinClassForRbd(String code) {
        return switch (code) {
            case "F", "P", "A", "R" -> "FIRST";
            case "J", "C", "D", "I", "Z" -> "BUSINESS";
            case "W", "S", "T" -> "PREMIUM_ECONOMY";
            default -> "ECONOMY";
        };
    }

    private void upsertOperationalSuffix(String code) throws SQLException {
        if (existsBy("OPERATIONAL_SUFFIX", "SUFFIX_CODE", code)) {
            return;
        }
        execute("""
                INSERT INTO %s.OPERATIONAL_SUFFIX (id, SUFFIX_CODE, SUFFIX_NAME, STATUS, created_at, created_by, updated_at, updated_by, active, is_deleted, transaction_id)
                VALUES (nextval('%s.operational_suffix_seq'), ?, ?, 'ACTIVE', now(), ?, now(), ?, true, false, 'SSIM_IMPORT')
                """.formatted(schema, config.schema()), code, "SSIM operational suffix " + code, config.auditUser(), config.auditUser());
    }

    private void upsertFlightFrequency(String code) throws SQLException {
        if (existsBy("FLIGHT_FREQUENCY", "FREQUENCY_CODE", code)) {
            return;
        }
        execute("""
                INSERT INTO %s.FLIGHT_FREQUENCY (id, FREQUENCY_CODE, FREQUENCY_NAME, OPERATING_DAYS, ACTIVE, DISPLAY_ORDER, STATUS, created_at, created_by, updated_at, updated_by, is_deleted, transaction_id)
                VALUES (nextval('%s.flight_frequency_seq'), ?, ?, ?, true, 1, 'ACTIVE', now(), ?, now(), ?, false, 'SSIM_IMPORT')
                """.formatted(schema, config.schema()), code, "SSIM frequency " + code, code.length(), config.auditUser(), config.auditUser());
    }

    private void upsertDataElementIdentifier(String code) throws SQLException {
        String normalized = code.length() == 3 ? code : "0".repeat(3 - code.length()) + code;
        if (existsBy("DATA_ELEMENT_IDENTIFIER", "DEI_CODE", normalized)) {
            return;
        }
        execute("""
                INSERT INTO %s.DATA_ELEMENT_IDENTIFIER (id, DEI_CODE, DEI_NAME, DEI_SCOPE, STATUS, created_at, created_by, updated_at, updated_by, active, is_deleted, transaction_id)
                VALUES (nextval('%s.data_element_identifier_seq'), ?, ?, 'LEG', 'ACTIVE', now(), ?, now(), ?, true, false, 'SSIM_IMPORT')
                """.formatted(schema, config.schema()), normalized, "SSIM data element identifier " + normalized, config.auditUser(), config.auditUser());
    }

    private void upsertDeiRegistry(String code) throws SQLException {
        String normalized = code.length() == 3 ? code : "0".repeat(3 - code.length()) + code;
        if (existsBy("DEI", "DEI_NUMBER", normalized)) {
            return;
        }
        execute("""
                INSERT INTO %s.DEI (id, DEI_NUMBER, DEI_NAME, SCOPE_LEVEL, FUNCTION_TYPE, STATUS, created_at, created_by, updated_at, updated_by, active, is_deleted, transaction_id)
                VALUES (nextval('%s.dei_seq'), ?, ?, 'LEG', 'GENERAL', 'ACTIVE', now(), ?, now(), ?, true, false, 'SSIM_IMPORT')
                """.formatted(schema, config.schema()), normalized, "SSIM DEI " + normalized, config.auditUser(), config.auditUser());
    }

    private void upsertSimpleCode(String table, String sequence, String codeColumn, String nameColumn, String code, String name) throws SQLException {
        if (existsBy(table, codeColumn, code)) {
            return;
        }
        execute("""
                INSERT INTO %s.%s (id, %s, %s, ACTIVE, DISPLAY_ORDER, STATUS, created_at, created_by, updated_at, updated_by, is_deleted, transaction_id)
                VALUES (nextval('%s.%s'), ?, ?, true, 1, 'ACTIVE', now(), ?, now(), ?, false, 'SSIM_IMPORT')
                """.formatted(schema, table, codeColumn, nameColumn, config.schema(), sequence),
                code, name, config.auditUser(), config.auditUser());
    }

    private boolean existsBy(String table, String column, String value) throws SQLException {
        return optionalIdBy(table, column, value) != null;
    }

    private boolean existsCity(String cityName, Long countryId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("SELECT 1 FROM " + schema + ".CITY WHERE CITY_NAME = ? AND COUNTRY_ID = ?")) {
            statement.setString(1, cityName);
            statement.setLong(2, countryId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    private boolean existsTerminal(Long airportId, String terminalCode) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("SELECT 1 FROM " + schema + ".TERMINAL WHERE AIRPORT_ID = ? AND TERMINAL_CODE = ?")) {
            statement.setLong(1, airportId);
            statement.setString(2, terminalCode);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    private Long idBy(String table, String column, String value) throws SQLException {
        Long id = optionalIdBy(table, column, value);
        if (id == null) {
            throw new SQLException("Missing required row " + table + "." + column + "=" + value);
        }
        return id;
    }

    private Long optionalIdBy(String table, String column, String value) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("SELECT id FROM " + schema + "." + table + " WHERE " + column + " = ?")) {
            statement.setString(1, value);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? resultSet.getLong(1) : null;
            }
        }
    }

    private void execute(String sql, Object... args) throws SQLException {
        executeUpdate(sql, args);
    }

    private int executeUpdate(String sql, Object... args) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 0; i < args.length; i++) {
                statement.setObject(i + 1, args[i]);
            }
            return statement.executeUpdate();
        }
    }

    private static String quoteIdent(String value) {
        if (!value.matches("[A-Za-z_][A-Za-z0-9_]*")) {
            throw new IllegalArgumentException("Invalid schema identifier: " + value);
        }
        return value;
    }
}
