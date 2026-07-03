package com.codeshare.airline.schedule.ingestion.validation.validator.ssim.structural;

import com.codeshare.airline.core.enums.schedule.MessageType;
import com.codeshare.airline.schedule.ingestion.config.ScheduleIngestionProperties;
import com.codeshare.airline.schedule.ingestion.domain.context.SsimIngestionContext;
import com.codeshare.airline.schedule.ingestion.domain.enums.SsimValidationMode;
import com.codeshare.airline.schedule.ingestion.domain.enums.ValidationStage;
import com.codeshare.airline.schedule.ingestion.validation.model.ValidationResult;
import com.codeshare.airline.schedule.ingestion.validation.validator.StructuralValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class SsimStructuralValidator implements StructuralValidation<SsimIngestionContext> {

    private static final int EXPECTED_LENGTH = 200;
    private static final Set<Character> VALID_RECORDS = Set.of('1', '2', '3', '4', '5');
    private final SsimValidationMode validationMode;

    public SsimStructuralValidator() {
        this.validationMode = SsimValidationMode.STRICT;
    }

    @Autowired
    public SsimStructuralValidator(ScheduleIngestionProperties properties) {
        this.validationMode = properties.getSsim().getValidationMode() == null
                ? SsimValidationMode.RELAXED
                : properties.getSsim().getValidationMode();
    }

    @Override
    public Set<MessageType> supportedTypes() {
        return Set.of(MessageType.SSIM);
    }

    @Override
    public ValidationResult validate(SsimIngestionContext context) {

        ValidationResult result = new ValidationResult();

        if (context == null || context.getMessageLines() == null || context.getMessageLines().isEmpty()) {
            result.addError("SSIM_001", "Empty SSIM message", "", "", ValidationStage.STRUCTURAL);
            return result;
        }

        int lineNo = 1;
        int headerCount = 0;
        int carrierCount = 0;
        int flightCount = 0;
        int trailerCount = 0;
        boolean seenCarrier = false;
        boolean seenFlight = false;
        boolean seenTrailer = false;
        String previousRecordSerial = null;
        Character previousRecordType = null;
        String currentFlightKey = null;

        for (String line : context.getMessageLines()) {

            if (line == null) {
                result.addError("SSIM_005", "Null line detected", null, "line:" + lineNo, ValidationStage.STRUCTURAL);
                lineNo++;
                continue;
            }

            if (line.isEmpty()) {
                result.addError("SSIM_006", "Empty line detected", line, "line:" + lineNo, ValidationStage.STRUCTURAL);
                lineNo++;
                continue;
            }

            if (line.length() != EXPECTED_LENGTH) {
                result.addError(
                        "SSIM_LEN_001",
                        "Invalid record length (expected 200)",
                        line,
                        "line:" + lineNo,
                        ValidationStage.STRUCTURAL
                );
                lineNo++;
                continue;
            }

            char recordType = line.charAt(0);

            if (recordType == '0') {
                validatePaddingRecord(result, line, lineNo);
                lineNo++;
                continue;
            }

            if (!VALID_RECORDS.contains(recordType)) {
                result.addError(
                        "SSIM_008",
                        "Invalid record type",
                        line,
                        "line:" + lineNo,
                        ValidationStage.STRUCTURAL
                );
                lineNo++;
                continue;
            }

            switch (recordType) {
                case '1' -> {
                    headerCount++;
                    validateRecord1(result, line, lineNo);
                    if (seenCarrier || seenFlight || seenTrailer) {
                        result.addError("SSIM_SEQ_001", "Type 1 header must precede carrier, flight, and trailer records", line, "line:" + lineNo, ValidationStage.STRUCTURAL);
                    }
                }
                case '2' -> {
                    carrierCount++;
                    seenCarrier = true;
                    validateRecord2(result, line, lineNo);
                    if (headerCount == 0) {
                        result.addError("SSIM_SEQ_002", "Type 2 carrier found before Type 1 header", line, "line:" + lineNo, ValidationStage.STRUCTURAL);
                    }
                    if (seenFlight || seenTrailer) {
                        result.addError("SSIM_SEQ_003", "Type 2 carrier must precede flight and trailer records", line, "line:" + lineNo, ValidationStage.STRUCTURAL);
                    }
                }
                case '3' -> {
                    flightCount++;
                    seenFlight = true;
                    currentFlightKey = flightKey(line);
                    validateRecord3(result, line, lineNo);
                    if (!seenCarrier) {
                        result.addError("SSIM_SEQ_004", "Type 3 flight found before Type 2 carrier", line, "line:" + lineNo, ValidationStage.STRUCTURAL);
                    }
                    if (seenTrailer) {
                        result.addError("SSIM_SEQ_005", "Type 3 flight found after Type 5 trailer", line, "line:" + lineNo, ValidationStage.STRUCTURAL);
                    }
                }
                case '4' -> {
                    validateRecord4(result, line, lineNo);
                    if (currentFlightKey == null || !currentFlightKey.equals(flightKey(line))) {
                        result.addError("SSIM_SEQ_009", "Type 4 segment record must immediately follow the matching Type 3 flight record", line, "line:" + lineNo, ValidationStage.STRUCTURAL);
                    }
                    if (!seenFlight) {
                        result.addError("SSIM_SEQ_006", "Type 4 DEI found before any Type 3 flight", line, "line:" + lineNo, ValidationStage.STRUCTURAL);
                    }
                    if (seenTrailer) {
                        result.addError("SSIM_SEQ_007", "Type 4 DEI found after Type 5 trailer", line, "line:" + lineNo, ValidationStage.STRUCTURAL);
                    }
                }
                case '5' -> {
                    trailerCount++;
                    seenTrailer = true;
                    validateRecord5(result, line, lineNo, previousRecordSerial);
                    if (!seenFlight) {
                        result.addError("SSIM_SEQ_008", "Type 5 trailer found before any Type 3 flight", line, "line:" + lineNo, ValidationStage.STRUCTURAL);
                    }
                }
                default -> {
                    // Already checked above.
                }
            }

            if (recordType != '5' && previousRecordType != null) {
                validateSerialSequence(result, line, lineNo, previousRecordSerial);
            }
            previousRecordSerial = serial(line);
            previousRecordType = recordType;
            lineNo++;
        }

        if (headerCount == 0) {
            result.addError("SSIM_REQ_001", "Missing Type 1 header record", "", "", ValidationStage.STRUCTURAL);
        } else if (headerCount > 1) {
            result.addError("SSIM_REQ_002", "Multiple Type 1 header records", "", "", ValidationStage.STRUCTURAL);
        }

        if (carrierCount == 0) {
            result.addError("SSIM_REQ_003", "Missing Type 2 carrier record", "", "", ValidationStage.STRUCTURAL);
        } else if (carrierCount > 1) {
            result.addError("SSIM_REQ_004", "Multiple Type 2 carrier records", "", "", ValidationStage.STRUCTURAL);
        }

        if (flightCount == 0) {
            result.addError("SSIM_REQ_005", "Missing Type 3 flight records", "", "", ValidationStage.STRUCTURAL);
        }

        if (trailerCount > 1) {
            result.addError("SSIM_REQ_007", "Multiple Type 5 trailer records", "", "", ValidationStage.STRUCTURAL);
        } else if (trailerCount == 0 && isStrict()) {
            result.addError("SSIM_REQ_006", "Missing Type 5 trailer record", "", "", ValidationStage.STRUCTURAL);
        }

        return result;
    }

    private void validateRecord1(ValidationResult result, String line, int lineNo) {
        requireEquals(result, "SSIM_T1_001", "Invalid Type 1 title of contents", line, 1, 35,
                padRight("AIRLINE STANDARD SCHEDULE DATA SET", 34), lineNo);
        requireBlank(result, "SSIM_T1_002", "Type 1 bytes 36-40 must be blank", line, 35, 40, lineNo);
        requireBlankOrPattern(result, "SSIM_T1_003", "Invalid Type 1 number of seasons", line, 40, 41, "\\d", lineNo);
        requirePattern(result, "SSIM_T1_005", "Invalid Type 1 data set serial number", line, 191, 194, "\\d{3}", lineNo);
        requireEquals(result, "SSIM_T1_006", "Type 1 record serial number must be 000001", line, 194, 200, "000001", lineNo);
    }

    private void validateRecord2(ValidationResult result, String line, int lineNo) {
        requirePattern(result, "SSIM_T2_001", "Invalid Type 2 time mode", line, 1, 2, "[UL]", lineNo);
        requireAirlineDesignator(result, "SSIM_T2_002", "Invalid Type 2 airline designator", line, 2, 5, lineNo);
        requireBlankOrPattern(result, "SSIM_T2_004", "Invalid Type 2 season", line, 10, 13, "[A-Z0-9 ]{3}", lineNo);
        requireBlank(result, "SSIM_T2_005", "Type 2 byte 14 must be blank", line, 13, 14, lineNo);
        requireDate(result, "SSIM_T2_006", "Invalid Type 2 validity start date", line, 14, 21, lineNo);
        requireDate(result, "SSIM_T2_007", "Invalid Type 2 validity end date", line, 21, 28, lineNo);
        requireDate(result, "SSIM_T2_008", "Invalid Type 2 creation date", line, 28, 35, lineNo);
        requireBlankOrPattern(result, "SSIM_T2_014", "Invalid Type 2 release date", line, 64, 71, DATE_PATTERN, lineNo);
        requirePattern(result, "SSIM_T2_009", "Invalid Type 2 schedule status", line, 71, 72, "[PC]", lineNo);
        requireBlankOrPattern(result, "SSIM_T2_010", "Invalid Type 2 duplicate airline designator marker", line, 107, 108, "X", lineNo);
        requireBlankOrPattern(result, "SSIM_T2_011", "Invalid Type 2 electronic ticketing information", line, 188, 190, "E[NT]", lineNo);
        requireTime(result, "SSIM_T2_012", "Invalid Type 2 creation time", line, 190, 194, lineNo);
        requirePattern(result, "SSIM_T2_013", "Invalid Type 2 record serial number", line, 194, 200, "\\d{6}", lineNo);
    }

    private void validateRecord3(ValidationResult result, String line, int lineNo) {
        requireBlankOrPattern(result, "SSIM_T3_024", "Invalid Type 3 operational suffix", line, 1, 2, "[A-Z]", lineNo);
        requireAirlineDesignator(result, "SSIM_T3_001", "Invalid Type 3 airline designator", line, 2, 5, lineNo);
        requireFlightNumber(result, "SSIM_T3_002", "Invalid Type 3 flight number", line, 5, 9, lineNo);
        requireRange01To99(result, "SSIM_T3_003", "Invalid Type 3 itinerary variation identifier", line, 9, 11, lineNo);
        requireRange01To99(result, "SSIM_T3_004", "Invalid Type 3 leg sequence number", line, 11, 13, lineNo);
        requirePattern(result, "SSIM_T3_005", "Invalid Type 3 service type", line, 13, 14, "[A-Z]", lineNo);
        requireDate(result, "SSIM_T3_006", "Invalid Type 3 operating period start", line, 14, 21, lineNo);
        requireDate(result, "SSIM_T3_007", "Invalid Type 3 operating period end", line, 21, 28, lineNo);
        requirePattern(result, "SSIM_T3_008", "Invalid Type 3 operating days", line, 28, 35, "[1-7 ]{7}", lineNo);
        requireNotBlank(result, "SSIM_T3_023", "Missing Type 3 operating days", line, 28, 35, lineNo);
        requireBlankOrPattern(result, "SSIM_T3_025", "Invalid Type 3 frequency rate", line, 35, 36, "\\d", lineNo);
        requireStation(result, "SSIM_T3_009", "Invalid Type 3 departure station", line, 36, 39, lineNo);
        requireTime(result, "SSIM_T3_010", "Invalid Type 3 passenger STD", line, 39, 43, lineNo);
        requireTime(result, "SSIM_T3_011", "Invalid Type 3 aircraft STD", line, 43, 47, lineNo);
        requireUtcVariation(result, "SSIM_T3_012", "Invalid Type 3 departure UTC/local variation", line, 47, 52, lineNo);
        requireBlankOrPattern(result, "SSIM_T3_026", "Invalid Type 3 departure terminal", line, 52, 54, "[A-Z0-9 ]{2}", lineNo);
        requireStation(result, "SSIM_T3_013", "Invalid Type 3 arrival station", line, 54, 57, lineNo);
        requireTime(result, "SSIM_T3_014", "Invalid Type 3 aircraft STA", line, 57, 61, lineNo);
        requireTime(result, "SSIM_T3_015", "Invalid Type 3 passenger STA", line, 61, 65, lineNo);
        requireUtcVariation(result, "SSIM_T3_016", "Invalid Type 3 arrival UTC/local variation", line, 65, 70, lineNo);
        requireBlankOrPattern(result, "SSIM_T3_027", "Invalid Type 3 arrival terminal", line, 70, 72, "[A-Z0-9 ]{2}", lineNo);
        requireNotBlank(result, "SSIM_T3_017", "Missing Type 3 aircraft type", line, 72, 75, lineNo);
        requirePattern(result, "SSIM_T3_028", "Invalid Type 3 aircraft type", line, 72, 75, "[A-Z0-9]{3}", lineNo);
        requireBlankOrPattern(result, "SSIM_T3_029", "Invalid Type 3 PRBD field", line, 75, 95, "[A-Z0-9 ]{20}", lineNo);
        requireBlankOrPattern(result, "SSIM_T3_030", "Invalid Type 3 PRBM field", line, 95, 100, "[A-Z ]{5}", lineNo);
        requireBlankOrPattern(result, "SSIM_T3_031", "Invalid Type 3 joint operation airline designators", line, 110, 119, "[A-Z0-9 ]{9}", lineNo);
        requireBlankOrPattern(result, "SSIM_T3_032", "Invalid Type 3 minimum connecting time status", line, 119, 121, "[A-Z0-9 ]{2}", lineNo);
        requireBlankOrPattern(result, "SSIM_T3_033", "Invalid Type 3 secure flight indicator", line, 121, 122, "[A-Z]", lineNo);
        if (isStrict() && isBlank(line, 75, 95) && isBlank(line, 172, 192)) {
            add(result, "SSIM_T3_018", "Type 3 requires PRBD or aircraft configuration/version", line, lineNo);
        }
        requireOverflowReference(result, "SSIM_T3_034", "Invalid Type 3 PRBD overflow marker", line, 75, 95, lineNo);
        requireBlank(result, "SSIM_T3_019", "Type 3 bytes 123-127 must be blank", line, 122, 127, lineNo);
        requireBlankOrPattern(result, "SSIM_T3_035", "Invalid Type 3 itinerary variation overflow", line, 127, 128, "[A-Z0-9]", lineNo);
        requireBlankOrAirlineDesignator(result, "SSIM_T3_036", "Invalid Type 3 aircraft owner", line, 128, 131, lineNo);
        requireBlankOrAirlineDesignator(result, "SSIM_T3_037", "Invalid Type 3 cockpit crew employer", line, 131, 134, lineNo);
        requireBlankOrAirlineDesignator(result, "SSIM_T3_038", "Invalid Type 3 cabin crew employer", line, 134, 137, lineNo);
        validateOnwardFlight(result, line, lineNo);
        requireBlank(result, "SSIM_T3_020", "Type 3 byte 147 must be blank", line, 146, 147, lineNo);
        requireBlankOrPattern(result, "SSIM_T3_039", "Invalid Type 3 flight transit layover", line, 147, 148, "[A-Z0-9]", lineNo);
        requireBlankOrPattern(result, "SSIM_T3_040", "Invalid Type 3 operating airline disclosure", line, 148, 149, "[A-Z]", lineNo);
        requireBlankOrPattern(result, "SSIM_T3_041", "Invalid Type 3 traffic restriction code", line, 149, 160, "[A-Z0-9 ]{11}", lineNo);
        requireBlankOrPattern(result, "SSIM_T3_042", "Invalid Type 3 traffic restriction overflow", line, 160, 161, "[A-Z0-9]", lineNo);
        requireBlankOrPattern(result, "SSIM_T3_043", "Invalid Type 3 aircraft configuration/version", line, 172, 192, "[A-Z0-9 ]{20}", lineNo);
        requireOverflowReference(result, "SSIM_T3_044", "Invalid Type 3 aircraft configuration/version overflow marker", line, 172, 192, lineNo);
        if (isStrict()) {
            requireBlankOrPattern(result, "SSIM_T3_045", "Invalid Type 3 date variation", line, 192, 194, "\\d{2}|[+-]\\d", lineNo);
        }
        requirePattern(result, "SSIM_T3_022", "Invalid Type 3 record serial number", line, 194, 200, "\\d{6}", lineNo);
    }

    private void validateRecord4(ValidationResult result, String line, int lineNo) {
        requireBlankOrPattern(result, "SSIM_T4_013", "Invalid Type 4 operational suffix", line, 1, 2, "[A-Z]", lineNo);
        requireAirlineDesignator(result, "SSIM_T4_001", "Invalid Type 4 airline designator", line, 2, 5, lineNo);
        requireFlightNumber(result, "SSIM_T4_002", "Invalid Type 4 flight number", line, 5, 9, lineNo);
        requireRange01To99(result, "SSIM_T4_003", "Invalid Type 4 itinerary variation identifier", line, 9, 11, lineNo);
        requireRange01To99(result, "SSIM_T4_004", "Invalid Type 4 leg sequence number", line, 11, 13, lineNo);
        requirePattern(result, "SSIM_T4_005", "Invalid Type 4 service type", line, 13, 14, "[A-Z]", lineNo);
        requireBlank(result, "SSIM_T4_006", "Type 4 bytes 15-27 must be blank", line, 14, 27, lineNo);
        requireBlankOrPattern(result, "SSIM_T4_014", "Invalid Type 4 itinerary variation overflow", line, 27, 28, "[A-Z0-9]", lineNo);
        requirePattern(result, "SSIM_T4_007", "Invalid Type 4 board point indicator", line, 28, 29, "[A-Z]", lineNo);
        requirePattern(result, "SSIM_T4_008", "Invalid Type 4 off point indicator", line, 29, 30, "[A-Z]", lineNo);
        requirePattern(result, "SSIM_T4_009", "Invalid Type 4 data element identifier", line, 30, 33, "\\d{3}", lineNo);
        requireStation(result, "SSIM_T4_010", "Invalid Type 4 board point", line, 33, 36, lineNo);
        requireStation(result, "SSIM_T4_011", "Invalid Type 4 off point", line, 36, 39, lineNo);
        requirePattern(result, "SSIM_T4_012", "Invalid Type 4 record serial number", line, 194, 200, "\\d{6}", lineNo);
    }

    private void validateRecord5(ValidationResult result, String line, int lineNo, String previousRecordSerial) {
        requireBlank(result, "SSIM_T5_001", "Type 5 byte 2 must be blank", line, 1, 2, lineNo);
        requireAirlineDesignator(result, "SSIM_T5_002", "Invalid Type 5 airline designator", line, 2, 5, lineNo);
        requireBlankOrPattern(result, "SSIM_T5_003", "Invalid Type 5 release date", line, 5, 12, DATE_PATTERN, lineNo);
        requireBlank(result, "SSIM_T5_004", "Type 5 bytes 13-187 must be blank", line, 12, 187, lineNo);
        requirePattern(result, "SSIM_T5_005", "Invalid Type 5 serial check reference", line, 187, 193, "\\d{6}", lineNo);
        requirePattern(result, "SSIM_T5_006", "Invalid Type 5 continuation/end code", line, 193, 194, "[CE]", lineNo);
        requirePattern(result, "SSIM_T5_007", "Invalid Type 5 record serial number", line, 194, 200, "\\d{6}", lineNo);
        validateTrailerSerialRelation(result, line, lineNo);
        validateTrailerCheckReference(result, line, lineNo, previousRecordSerial);
    }

    private void validateSerialSequence(ValidationResult result, String line, int lineNo, String previousRecordSerial) {
        if (!isStrict()) {
            return;
        }
        String current = serial(line);
        if (!current.matches("\\d{6}") || previousRecordSerial == null || !previousRecordSerial.matches("\\d{6}")) {
            return;
        }
        int previous = Integer.parseInt(previousRecordSerial);
        int actual = Integer.parseInt(current);
        if (previous < 999999 && actual != previous + 1) {
            add(result, "SSIM_SERIAL_001", "Record serial number is not sequential", line, lineNo);
        }
    }

    private static final String DATE_PATTERN = "(\\d{2}[A-Z]{3}\\d{2}|00XXX00)";

    private void validatePaddingRecord(ValidationResult result, String line, int lineNo) {
        if (!line.matches("0{200}")) {
            add(result, "SSIM_PAD_001", "Invalid SSIM zero padding record", line, lineNo);
        }
    }

    private void validateOnwardFlight(ValidationResult result, String line, int lineNo) {
        boolean hasOnward = !isBlank(line, 137, 146);
        if (!hasOnward) {
            return;
        }

        requireAirlineDesignator(result, "SSIM_T3_046", "Invalid Type 3 onward airline designator", line, 137, 140, lineNo);
        requireFlightNumber(result, "SSIM_T3_047", "Invalid Type 3 onward flight number", line, 140, 144, lineNo);
        requireBlankOrPattern(result, "SSIM_T3_048", "Invalid Type 3 aircraft rotation layover", line, 144, 145, "[A-Z0-9]", lineNo);
        requireBlankOrPattern(result, "SSIM_T3_049", "Invalid Type 3 onward operational suffix", line, 145, 146, "[A-Z]", lineNo);
    }

    private void validateTrailerSerialRelation(ValidationResult result, String line, int lineNo) {
        if (!isStrict()) {
            return;
        }
        String checkReference = line.substring(187, 193);
        String trailerSerial = line.substring(194, 200);
        if (!checkReference.matches("\\d{6}") || !trailerSerial.matches("\\d{6}")) {
            return;
        }

        int expected = Integer.parseInt(checkReference) + 1;
        int actual = Integer.parseInt(trailerSerial);
        if (expected <= 999999 && actual != expected) {
            add(result, "SSIM_T5_008", "Type 5 record serial number must be one greater than serial check reference", line, lineNo);
        }
    }

    private void validateTrailerCheckReference(ValidationResult result, String line, int lineNo, String previousRecordSerial) {
        if (!isStrict()) {
            return;
        }
        String checkReference = line.substring(187, 193);
        if (previousRecordSerial == null
                || !previousRecordSerial.matches("\\d{6}")
                || !checkReference.matches("\\d{6}")) {
            return;
        }

        if (!checkReference.equals(previousRecordSerial)) {
            add(result, "SSIM_T5_009", "Type 5 serial check reference must equal previous record serial number", line, lineNo);
        }
    }

    private void requireDate(ValidationResult result, String code, String message, String line, int start, int end, int lineNo) {
        requirePattern(result, code, message, line, start, end, DATE_PATTERN, lineNo);
    }

    private void requireTime(ValidationResult result, String code, String message, String line, int start, int end, int lineNo) {
        requirePattern(result, code, message, line, start, end, "([01]\\d|2[0-3])[0-5]\\d", lineNo);
    }

    private void requireUtcVariation(ValidationResult result, String code, String message, String line, int start, int end, int lineNo) {
        requirePattern(result, code, message, line, start, end, "[+-]\\d{4}", lineNo);
    }

    private void requireStation(ValidationResult result, String code, String message, String line, int start, int end, int lineNo) {
        requirePattern(result, code, message, line, start, end, "[A-Z]{3}", lineNo);
    }

    private void requireAirlineDesignator(ValidationResult result, String code, String message, String line, int start, int end, int lineNo) {
        requirePattern(result, code, message, line, start, end, AIRLINE_DESIGNATOR_PATTERN, lineNo);
    }

    private void requireBlankOrAirlineDesignator(ValidationResult result, String code, String message, String line, int start, int end, int lineNo) {
        requireBlankOrPattern(result, code, message, line, start, end, AIRLINE_DESIGNATOR_PATTERN, lineNo);
    }

    private void requireFlightNumber(ValidationResult result, String code, String message, String line, int start, int end, int lineNo) {
        requirePattern(result, code, message, line, start, end, "[ 0-9]{4}", lineNo);
        requireNotBlank(result, code, message, line, start, end, lineNo);
    }

    private void requireRange01To99(ValidationResult result, String code, String message, String line, int start, int end, int lineNo) {
        requirePattern(result, code, message, line, start, end, "0[1-9]|[1-9]\\d", lineNo);
    }

    private void requireNotBlank(ValidationResult result, String code, String message, String line, int start, int end, int lineNo) {
        if (isBlank(line, start, end)) {
            add(result, code, message, line, lineNo);
        }
    }

    private void requireBlank(ValidationResult result, String code, String message, String line, int start, int end, int lineNo) {
        if (!isBlank(line, start, end)) {
            add(result, code, message, line, lineNo);
        }
    }

    private void requireBlankOrPattern(ValidationResult result, String code, String message, String line, int start, int end, String pattern, int lineNo) {
        String value = line.substring(start, end);
        if (!value.isBlank() && !value.matches(pattern)) {
            add(result, code, message, line, lineNo);
        }
    }

    private void requirePattern(ValidationResult result, String code, String message, String line, int start, int end, String pattern, int lineNo) {
        if (!line.substring(start, end).matches(pattern)) {
            add(result, code, message, line, lineNo);
        }
    }

    private void requireEquals(ValidationResult result, String code, String message, String line, int start, int end, String expected, int lineNo) {
        if (!line.substring(start, end).equals(expected)) {
            add(result, code, message, line, lineNo);
        }
    }

    private void requireOverflowReference(ValidationResult result, String code, String message, String line, int start, int end, int lineNo) {
        String value = line.substring(start, end);
        if (value.startsWith("XX") && !value.substring(2).isBlank()) {
            add(result, code, message, line, lineNo);
        }
    }

    private boolean isBlank(String line, int start, int end) {
        return line.substring(start, end).isBlank();
    }

    private String serial(String line) {
        return line.substring(194, 200);
    }

    private String flightKey(String line) {
        return line.substring(1, 14);
    }

    private String padRight(String value, int length) {
        return String.format("%-" + length + "s", value);
    }

    private static final String AIRLINE_DESIGNATOR_PATTERN = "[A-Z0-9]{2}[A-Z0-9 ]";

    private boolean isStrict() {
        return validationMode == SsimValidationMode.STRICT;
    }

    private void add(ValidationResult result, String code, String message, String line, int lineNo) {
        result.addError(code, message, line, "line:" + lineNo, ValidationStage.STRUCTURAL);
    }
}
