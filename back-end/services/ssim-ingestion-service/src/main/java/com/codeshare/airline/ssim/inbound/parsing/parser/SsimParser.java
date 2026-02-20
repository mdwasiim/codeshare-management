package com.codeshare.airline.ssim.inbound.parsing.parser;


import com.codeshare.airline.ssim.inbound.domain.enums.SsimTimeMode;
import com.codeshare.airline.ssim.inbound.persistence.inbound.entity.*;
import org.springframework.stereotype.Component;

@Component
public class SsimParser {

    public SsimInboundHeader processRecord1(
            String line,
            SsimInboundFile inboundFile
    ) {

        if (line == null || line.length() != 200) {
            throw new IllegalStateException("Invalid SSIM record length");
        }

        if (line.charAt(0) != '1') {
            throw new IllegalStateException("Not a T1 record");
        }

        SsimInboundHeader header = new SsimInboundHeader();
        header.setInboundFile(inboundFile);
        header.setRecordType("1");

        // Bytes 2–35 (34 chars)
        header.setTitleOfContents(line.substring(1, 35));

        // Bytes 36–40 (5 chars)
        header.setSpare36To40(line.substring(35, 40));

        // Byte 41 (1 char)
        header.setNumberOfSeasons(line.substring(40, 41));

        // Bytes 42–191 (150 chars)
        header.setSpare42To191(line.substring(41, 191));

        // Bytes 192–194 (3 chars)
        String datasetSerial = line.substring(191, 194);
        if (!datasetSerial.matches("\\d{3}")) {
            throw new IllegalStateException("Invalid Dataset Serial Number in T1");
        }
        header.setDatasetSerialNumber(datasetSerial);

        // Bytes 195–200 (6 chars)
        String recordSerial = line.substring(194, 200);
        if (!recordSerial.matches("\\d{6}")) {
            throw new IllegalStateException("Invalid Record Serial Number in T1");
        }
        header.setRecordSerialNumber(recordSerial);

        header.setRawRecord(line);

        return header;
    }




    public SsimInboundCarrier processRecord2(
            String line,
            SsimInboundFile inboundFile
    ) {

        if (line == null || line.length() != 200) {
            throw new IllegalStateException("Invalid SSIM record length");
        }

        if (line.charAt(0) != '2') {
            throw new IllegalStateException("Not a T2 record");
        }

        SsimInboundCarrier carrier = new SsimInboundCarrier();
        carrier.setInboundFile(inboundFile);
        carrier.setRecordType("2");

        // Byte 2
        carrier.setTimeMode(
                SsimTimeMode.valueOf(line.substring(1, 2))
        );

        // Bytes 3–5
        carrier.setAirlineCode(line.substring(2, 5));

        // Bytes 6–10
        carrier.setSpare6To10(line.substring(5, 10));

        // Bytes 11–13
        carrier.setSeason(line.substring(10, 13));

        // Byte 14
        carrier.setSpare14(line.substring(13, 14));

        // Bytes 15–21
        carrier.setValidityStartRaw(line.substring(14, 21));

        // Bytes 22–28
        carrier.setValidityEndRaw(line.substring(21, 28));

        // Bytes 29–35
        carrier.setCreationDateRaw(line.substring(28, 35));

        // Bytes 36–64
        carrier.setTitleOfData(line.substring(35, 64));

        // Bytes 65–71
        carrier.setReleaseDateRaw(line.substring(64, 71));

        // Byte 72
        carrier.setScheduleStatus(line.substring(71, 72));

        // Bytes 73–107
        carrier.setCreatorReference(line.substring(72, 107));

        // Byte 108
        carrier.setDuplicateDesignatorMarker(line.substring(107, 108));

        // Bytes 109–169
        carrier.setGeneralInformation(line.substring(108, 169));

        // Bytes 170–188
        carrier.setInflightServiceInfo(line.substring(169, 188));

        // Bytes 189–190
        carrier.setElectronicTicketingInfo(line.substring(188, 190));

        // Bytes 191–194
        carrier.setCreationTimeRaw(line.substring(190, 194));

        // Bytes 195–200
        String recordSerial = line.substring(194, 200);
        if (!recordSerial.matches("\\d{6}")) {
            throw new IllegalStateException("Invalid Record Serial Number in T2");
        }
        carrier.setRecordSerialNumber(recordSerial);

        carrier.setRawRecord(line);

        return carrier;
    }




    public SsimInboundFlightLeg processRecord3(
            String line,
            SsimInboundFile inboundFile
    ) {

        if (line == null || line.length() != 200) {
            throw new IllegalStateException("Invalid SSIM record length");
        }

        if (line.charAt(0) != '3') {
            throw new IllegalStateException("Not a T3 record");
        }

        SsimInboundFlightLeg leg = new SsimInboundFlightLeg();
        leg.setInboundFile(inboundFile);
        leg.setRecordType("3");

        // 2
        leg.setOperationalSuffix(line.substring(1, 2));

        // 3–5
        leg.setAirlineCode(line.substring(2, 5));

        // 6–9
        leg.setFlightNumber(line.substring(5, 9));

        // 10–11
        leg.setItineraryVariationIdentifier(line.substring(9, 11));

        // 12–13
        leg.setLegSequenceNumber(line.substring(11, 13));

        // 14
        leg.setServiceType(line.substring(13, 14));

        // 15–21
        leg.setOperatingPeriodStartRaw(line.substring(14, 21));

        // 22–28
        leg.setOperatingPeriodEndRaw(line.substring(21, 28));

        // 29–35
        leg.setOperatingDays(line.substring(28, 35));

        // 36
        leg.setFrequencyRate(line.substring(35, 36));

        // 37–39
        leg.setDepartureStation(line.substring(36, 39));

        // 40–43
        leg.setPassengerStd(line.substring(39, 43));

        // 44–47
        leg.setAircraftStd(line.substring(43, 47));

        // 48–52
        leg.setDepartureUtcVariation(line.substring(47, 52));

        // 53–54
        leg.setDepartureTerminal(line.substring(52, 54));

        // 55–57
        leg.setArrivalStation(line.substring(54, 57));

        // 58–61
        leg.setAircraftSta(line.substring(57, 61));

        // 62–65
        leg.setPassengerSta(line.substring(61, 65));

        // 66–70
        leg.setArrivalUtcVariation(line.substring(65, 70));

        // 71–72
        leg.setArrivalTerminal(line.substring(70, 72));

        // 73–75
        leg.setAircraftType(line.substring(72, 75));

        // 76–95
        leg.setPassengerReservationBookingDesignator(line.substring(75, 95));

        // 96–100
        leg.setPassengerReservationBookingModifier(line.substring(95, 100));

        // 101–110
        leg.setMealServiceNote(line.substring(100, 110));

        // 111–119
        leg.setJointOperationAirlineDesignators(line.substring(110, 119));

        // 120–121
        leg.setMinimumConnectingTimeStatus(line.substring(119, 121));

        // 122
        leg.setSecureFlightIndicator(line.substring(121, 122));

        // 123–127
        leg.setSpare123To127(line.substring(122, 127));

        // 128
        leg.setItineraryVariationOverflow(line.substring(127, 128));

        // 129–131
        leg.setAircraftOwner(line.substring(128, 131));

        // 132–134
        leg.setCockpitCrewEmployer(line.substring(131, 134));

        // 135–137
        leg.setCabinCrewEmployer(line.substring(134, 137));

        // 138–140
        leg.setOnwardAirlineDesignator(line.substring(137, 140));

        // 141–144
        leg.setOnwardFlightNumber(line.substring(140, 144));

        // 145
        leg.setAircraftRotationLayover(line.substring(144, 145));

        // 146
        leg.setOnwardOperationalSuffix(line.substring(145, 146));

        // 147
        leg.setSpare147(line.substring(146, 147));

        // 148
        leg.setFlightTransitLayover(line.substring(147, 148));

        // 149
        leg.setOperatingAirlineDisclosure(line.substring(148, 149));

        // 150–160
        leg.setTrafficRestrictionCode(line.substring(149, 160));

        // 161
        leg.setTrafficRestrictionOverflow(line.substring(160, 161));

        // 162–172
        leg.setSpare162To172(line.substring(161, 172));

        // 173–192
        leg.setAircraftConfigurationVersion(line.substring(172, 192));

        // 193–194
        leg.setDateVariation(line.substring(192, 194));

        // 195–200
        leg.setRecordSerialNumber(line.substring(194, 200));

        leg.setRawRecord(line);

        return leg;
    }


    public SsimInboundSegmentDei processRecord4(
            String line,
            SsimInboundFlightLeg flightLeg
    ) {

        if (line == null || line.length() != 200) {
            throw new IllegalStateException("Invalid SSIM record length");
        }

        if (line.charAt(0) != '4') {
            throw new IllegalStateException("Not a T4 record");
        }

        SsimInboundSegmentDei dei = new SsimInboundSegmentDei();

        dei.setFlightLeg(flightLeg);
        dei.setRecordType("4");

    /* ======================================================
       2–14 HEADER
       ====================================================== */

        dei.setOperationalSuffix(line.substring(1, 2));           // 2
        dei.setAirlineCode(line.substring(2, 5).trim());          // 3–5
        dei.setFlightNumber(line.substring(5, 9).trim());         // 6–9
        dei.setItineraryVariationIdentifier(line.substring(9, 11)); // 10–11
        dei.setLegSequenceNumber(line.substring(11, 13));         // 12–13
        dei.setServiceType(line.substring(13, 14));               // 14

    /* ======================================================
       15–28 STRUCTURE
       ====================================================== */

        dei.setSpare15To27(line.substring(14, 27));               // 15–27
        dei.setItineraryVariationOverflow(line.substring(27, 28)); // 28

    /* ======================================================
       29–39 SEGMENT IDENTIFIERS
       ====================================================== */

        dei.setBoardPointIndicator(line.substring(28, 29));       // 29
        dei.setOffPointIndicator(line.substring(29, 30));         // 30

        String deiNumber = line.substring(30, 33);                // 31–33
        if (!deiNumber.matches("\\d{3}")) {
            throw new IllegalStateException("Invalid DEI number in T4");
        }
        dei.setDataElementIdentifier(deiNumber);

        dei.setBoardPoint(line.substring(33, 36));                // 34–36
        dei.setOffPoint(line.substring(36, 39));                  // 37–39

    /* ======================================================
       40–194 DEI DATA
       ====================================================== */

        dei.setDeiData(line.substring(39, 194));                  // 155 chars

    /* ======================================================
       195–200 FOOTER
       ====================================================== */

        String recordSerial = line.substring(194, 200);
        if (!recordSerial.matches("\\d{6}")) {
            throw new IllegalStateException("Invalid Record Serial Number in T4");
        }
        dei.setRecordSerialNumber(recordSerial);

        dei.setRawRecord(line);

        return dei;
    }


    public SsimInboundTrailer processRecord5(
            String line,
            SsimInboundFile inboundFile
    ) {

        if (line == null || line.length() != 200) {
            throw new IllegalStateException("Invalid SSIM record length");
        }

        if (line.charAt(0) != '5') {
            throw new IllegalStateException("Not a T5 record");
        }

        SsimInboundTrailer trailer = new SsimInboundTrailer();

        trailer.setInboundFile(inboundFile);
        trailer.setRecordType(line.substring(0, 1));         // 1
        trailer.setSpareByte2(line.substring(1, 2));         // 2
        trailer.setAirlineDesignator(line.substring(2, 5));  // 3–5
        trailer.setReleaseDateRaw(line.substring(5, 12));    // 6–12
        trailer.setSpare13To187(line.substring(12, 187));    // 13–187
        trailer.setSerialCheckReference(line.substring(187, 193)); // 188–193
        trailer.setContinuationEndCode(line.substring(193, 194));  // 194
        trailer.setRecordSerialNumber(line.substring(194, 200));   // 195–200

        trailer.setRawRecord(line);

        return trailer;
    }



 /*   public static void main(String[] args) {

        String line = "3 AS   670501J03SEP1110SEP11     67 SEA15351535-0700  KTN16371637-0800  734FUYSBMHQLVKGT            L L                                                          O                              00330966";
        for (int i = 0; i < line.length(); i++) {
            System.out.printf("%3d : %s%n", i+1, line.charAt(i));
        }
    }*/


}
