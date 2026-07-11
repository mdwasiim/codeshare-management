package com.codeshare.airline.master.data;

import com.codeshare.airline.platform.core.enums.common.CabinClass;
import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import com.codeshare.airline.platform.core.enums.master.airline.AirlineAliasType;
import com.codeshare.airline.platform.core.enums.master.airline.AirlineContactType;
import com.codeshare.airline.platform.core.enums.master.airline.AirlineRoleCategory;
import com.codeshare.airline.platform.core.enums.master.airline.AirlineRoleScope;
import com.codeshare.airline.platform.core.enums.master.airline.AllianceMembershipStatus;
import com.codeshare.airline.platform.core.enums.master.airline.AllianceMembershipType;
import com.codeshare.airline.platform.core.enums.master.airline.CommunicationMethod;
import com.codeshare.airline.platform.core.enums.schedule.SeasonType;
import com.codeshare.airline.master.airlines.entities.AirlineAlias;
import com.codeshare.airline.master.airlines.entities.AirlineBusinessRole;
import com.codeshare.airline.master.airlines.entities.AirlineCarrier;
import com.codeshare.airline.master.airlines.entities.AirlineContact;
import com.codeshare.airline.master.airlines.entities.Alliance;
import com.codeshare.airline.master.airlines.entities.AllianceMember;
import com.codeshare.airline.master.airlines.repository.AirlineAliasRepository;
import com.codeshare.airline.master.airlines.repository.AirlineBusinessRoleRepository;
import com.codeshare.airline.master.airlines.repository.AirlineCarrierRepository;
import com.codeshare.airline.master.airlines.repository.AirlineContactRepository;
import com.codeshare.airline.master.airlines.repository.AllianceMemberRepository;
import com.codeshare.airline.master.airlines.repository.AllianceRepository;
import com.codeshare.airline.master.flight.passenger.entities.ElectronicTicketIndicator;
import com.codeshare.airline.master.flight.passenger.entities.MealService;
import com.codeshare.airline.master.flight.passenger.entities.ReservationBookingDesignator;
import com.codeshare.airline.master.flight.passenger.entities.ReservationBookingModifier;
import com.codeshare.airline.master.flight.passenger.entities.SecureFlightIndicator;
import com.codeshare.airline.master.flight.passenger.entities.ServiceType;
import com.codeshare.airline.master.flight.passenger.repository.ElectronicTicketIndicatorRepository;
import com.codeshare.airline.master.flight.passenger.repository.MealServiceRepository;
import com.codeshare.airline.master.flight.passenger.repository.ReservationBookingDesignatorRepository;
import com.codeshare.airline.master.flight.passenger.repository.ReservationBookingModifierRepository;
import com.codeshare.airline.master.flight.passenger.repository.SecureFlightIndicatorRepository;
import com.codeshare.airline.master.flight.passenger.repository.ServiceTypeRepository;
import com.codeshare.airline.master.flight.schedule.entities.FlightFrequency;
import com.codeshare.airline.master.flight.schedule.entities.FlightSuffix;
import com.codeshare.airline.master.flight.schedule.entities.TimeMode;
import com.codeshare.airline.master.flight.schedule.entities.TrafficRestrictionCode;
import com.codeshare.airline.master.flight.schedule.entities.TrafficRestrictionQualifier;
import com.codeshare.airline.master.flight.schedule.repository.FlightFrequencyRepository;
import com.codeshare.airline.master.flight.schedule.repository.FlightSuffixRepository;
import com.codeshare.airline.master.flight.schedule.repository.TimeModeRepository;
import com.codeshare.airline.master.flight.schedule.repository.TrafficRestrictionCodeRepository;
import com.codeshare.airline.master.flight.schedule.repository.TrafficRestrictionQualifierRepository;
import com.codeshare.airline.master.geography.entities.TimezoneDLS;
import com.codeshare.airline.master.geography.repository.AirportRepository;
import com.codeshare.airline.master.geography.repository.DstRuleRepository;
import com.codeshare.airline.master.geography.repository.TimezoneRepository;
import com.codeshare.airline.master.messaging.entities.ActionCode;
import com.codeshare.airline.master.messaging.entities.ActionIdentifier;
import com.codeshare.airline.master.messaging.entities.DataElementIdentifier;
import com.codeshare.airline.master.messaging.entities.DistributionChannel;
import com.codeshare.airline.master.messaging.entities.MessagePriority;
import com.codeshare.airline.master.messaging.entities.MessageStatus;
import com.codeshare.airline.master.messaging.entities.RejectReason;
import com.codeshare.airline.master.messaging.entities.StandardMessageIdentifier;
import com.codeshare.airline.master.messaging.repository.ActionCodeRepository;
import com.codeshare.airline.master.messaging.repository.ActionIdentifierRepository;
import com.codeshare.airline.master.messaging.repository.DataElementIdentifierRepository;
import com.codeshare.airline.master.messaging.repository.DistributionChannelRepository;
import com.codeshare.airline.master.messaging.repository.MessagePriorityRepository;
import com.codeshare.airline.master.messaging.repository.MessageStatusRepository;
import com.codeshare.airline.master.messaging.repository.RejectReasonRepository;
import com.codeshare.airline.master.messaging.repository.StandardMessageIdentifierRepository;
import com.codeshare.airline.master.schedule.entities.OperationalSuffix;
import com.codeshare.airline.master.schedule.entities.ScheduleCategory;
import com.codeshare.airline.master.schedule.entities.ScheduleChannel;
import com.codeshare.airline.master.schedule.entities.SchedulePriority;
import com.codeshare.airline.master.schedule.entities.ScheduleSource;
import com.codeshare.airline.master.schedule.entities.ScheduleStatus;
import com.codeshare.airline.master.schedule.entities.ScheduleType;
import com.codeshare.airline.master.schedule.entities.Season;
import com.codeshare.airline.master.schedule.repository.OperationalSuffixRepository;
import com.codeshare.airline.master.schedule.repository.ScheduleCategoryRepository;
import com.codeshare.airline.master.schedule.repository.ScheduleChannelRepository;
import com.codeshare.airline.master.schedule.repository.SchedulePriorityRepository;
import com.codeshare.airline.master.schedule.repository.ScheduleSourceRepository;
import com.codeshare.airline.master.schedule.repository.ScheduleStatusRepository;
import com.codeshare.airline.master.schedule.repository.ScheduleTypeRepository;
import com.codeshare.airline.master.schedule.repository.SeasonRepository;
import com.codeshare.airline.master.terminal.entities.AirportTerminal;
import com.codeshare.airline.master.terminal.entities.PassengerTerminal;
import com.codeshare.airline.master.terminal.entities.TrafficConferenceArea;
import com.codeshare.airline.master.terminal.repository.AirportTerminalRepository;
import com.codeshare.airline.master.terminal.repository.PassengerTerminalRepository;
import com.codeshare.airline.master.terminal.repository.TrafficConferenceAreaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
@Order(500)
@RequiredArgsConstructor
public class ReferenceMasterDataLoader implements CommandLineRunner {

    private static final LocalDate EFFECTIVE_FROM = LocalDate.of(2025, 1, 1);

    private final ScheduleSourceRepository scheduleSourceRepository;
    private final ScheduleChannelRepository scheduleChannelRepository;
    private final ScheduleStatusRepository scheduleStatusRepository;
    private final ScheduleTypeRepository scheduleTypeRepository;
    private final SchedulePriorityRepository schedulePriorityRepository;
    private final ScheduleCategoryRepository scheduleCategoryRepository;
    private final OperationalSuffixRepository operationalSuffixRepository;
    private final SeasonRepository seasonRepository;
    private final StandardMessageIdentifierRepository standardMessageIdentifierRepository;
    private final ActionIdentifierRepository actionIdentifierRepository;
    private final ActionCodeRepository actionCodeRepository;
    private final DataElementIdentifierRepository dataElementIdentifierRepository;
    private final DistributionChannelRepository distributionChannelRepository;
    private final MessagePriorityRepository messagePriorityRepository;
    private final MessageStatusRepository messageStatusRepository;
    private final RejectReasonRepository rejectReasonRepository;
    private final ServiceTypeRepository serviceTypeRepository;
    private final FlightFrequencyRepository flightFrequencyRepository;
    private final FlightSuffixRepository flightSuffixRepository;
    private final TimeModeRepository timeModeRepository;
    private final TrafficRestrictionCodeRepository trafficRestrictionCodeRepository;
    private final TrafficRestrictionQualifierRepository trafficRestrictionQualifierRepository;
    private final ElectronicTicketIndicatorRepository electronicTicketIndicatorRepository;
    private final MealServiceRepository mealServiceRepository;
    private final ReservationBookingDesignatorRepository reservationBookingDesignatorRepository;
    private final ReservationBookingModifierRepository reservationBookingModifierRepository;
    private final SecureFlightIndicatorRepository secureFlightIndicatorRepository;
    private final TrafficConferenceAreaRepository trafficConferenceAreaRepository;
    private final AirportRepository airportRepository;
    private final TimezoneRepository timezoneRepository;
    private final DstRuleRepository dstRuleRepository;
    private final AirportTerminalRepository airportTerminalRepository;
    private final PassengerTerminalRepository passengerTerminalRepository;
    private final AirlineCarrierRepository airlineCarrierRepository;
    private final AirlineAliasRepository airlineAliasRepository;
    private final AirlineBusinessRoleRepository airlineBusinessRoleRepository;
    private final AirlineContactRepository airlineContactRepository;
    private final AllianceRepository allianceRepository;
    private final AllianceMemberRepository allianceMemberRepository;
    private final ObjectMapper objectMapper;
    private final ResourcePatternResolver resourcePatternResolver;

    @Override
    public void run(String... args) throws IOException {
        loadResourceSeedFiles();
    }

    private void loadResourceSeedFiles() throws IOException {
        Resource[] resources = resourcePatternResolver.getResources("classpath*:bootstrap/master-data/*.json");
        List<Resource> orderedResources = new ArrayList<>(List.of(resources));
        orderedResources.sort(Comparator.comparing(Resource::getFilename, Comparator.nullsLast(String::compareTo)));

        for (Resource resource : orderedResources) {
            JsonNode root = objectMapper.readTree(resource.getInputStream());
            JsonNode sections = root.path("sections");
            if (sections.isArray()) {
                for (JsonNode section : sections) {
                    loadSection(section);
                }
            } else {
                loadSection(root);
            }
        }
    }

    private void loadSection(JsonNode section) {
        String type = text(section, "type");
        JsonNode items = section.path("items");
        if (type == null || !items.isArray()) {
            return;
        }

        for (JsonNode item : items) {
            loadItem(type, item);
        }
    }

    private void loadItem(String type, JsonNode item) {
        switch (type) {
            case "schedule-sources" -> scheduleSource(text(item, "code"), text(item, "name"), text(item, "description"));
            case "schedule-channels" -> scheduleChannel(text(item, "code"), text(item, "name"), text(item, "description"));
            case "schedule-statuses" -> scheduleStatus(text(item, "code"), text(item, "name"), text(item, "description"));
            case "schedule-types" -> scheduleType(text(item, "code"), text(item, "name"), text(item, "description"));
            case "schedule-priorities" -> schedulePriority(text(item, "code"), text(item, "name"), integer(item, "level"), text(item, "description"));
            case "schedule-categories" -> scheduleCategory(text(item, "code"), text(item, "name"), text(item, "description"));
            case "operational-suffixes" -> operationalSuffix(text(item, "code"), text(item, "name"), text(item, "description"));
            case "seasons" -> season(text(item, "code"), text(item, "name"), enumValue(SeasonType.class, item, "seasonType"), integer(item, "year"), date(item, "startDate"), date(item, "endDate"), integer(item, "displayOrder"));
            case "standard-message-identifiers" -> standardMessage(text(item, "code"), text(item, "name"), text(item, "description"));
            case "action-identifiers" -> actionIdentifier(text(item, "code"), text(item, "name"), text(item, "description"));
            case "action-codes" -> actionCode(text(item, "code"), text(item, "name"), text(item, "description"));
            case "data-element-identifiers" -> dataElement(text(item, "code"), text(item, "name"), text(item, "scope"), text(item, "description"));
            case "distribution-channels" -> distributionChannel(text(item, "code"), text(item, "name"), text(item, "channelType"), text(item, "protocolType"), text(item, "endpointUrl"));
            case "message-priorities" -> messagePriority(text(item, "code"), text(item, "name"), integer(item, "level"), text(item, "description"));
            case "message-statuses" -> messageStatus(text(item, "code"), text(item, "name"), text(item, "description"));
            case "reject-reasons" -> rejectReason(text(item, "code"), text(item, "name"), text(item, "description"));
            case "service-types" -> serviceType(text(item, "code"), text(item, "name"), text(item, "category"), text(item, "description"), integer(item, "displayOrder"));
            case "time-modes" -> timeMode(text(item, "code"), text(item, "name"), text(item, "description"), integer(item, "displayOrder"));
            case "flight-frequencies" -> flightFrequency(text(item, "code"), text(item, "name"), text(item, "description"), integer(item, "displayOrder"));
            case "flight-suffixes" -> flightSuffix(text(item, "code"), text(item, "name"), text(item, "description"), integer(item, "displayOrder"));
            case "electronic-ticket-indicators" -> electronicTicketIndicator(text(item, "code"), text(item, "name"), text(item, "description"), integer(item, "displayOrder"));
            case "secure-flight-indicators" -> secureFlightIndicator(text(item, "code"), text(item, "name"), text(item, "authority"), text(item, "description"), integer(item, "displayOrder"));
            case "meal-services" -> meal(text(item, "code"), text(item, "name"), text(item, "category"), enumValue(CabinClass.class, item, "cabinClass"), text(item, "description"), integer(item, "displayOrder"));
            case "reservation-booking-designators" -> rbd(text(item, "code"), text(item, "name"), enumValue(CabinClass.class, item, "cabinClass"), text(item, "category"), integer(item, "displayOrder"));
            case "reservation-booking-modifiers" -> rbdModifier(text(item, "code"), text(item, "name"), text(item, "category"), text(item, "description"), integer(item, "displayOrder"));
            case "traffic-restrictions" -> trafficRestriction(text(item, "code"), text(item, "name"), text(item, "category"), text(item, "description"), integer(item, "displayOrder"));
            case "traffic-restriction-qualifiers" -> trafficQualifier(text(item, "restrictionCode"), text(item, "code"), text(item, "name"), text(item, "description"), integer(item, "displayOrder"));
            case "traffic-conference-areas" -> trafficConferenceArea(text(item, "code"), text(item, "name"), text(item, "iataCode"), text(item, "description"));
            case "airport-terminals" -> airportTerminal(text(item, "airport"), text(item, "code"), text(item, "name"), text(item, "iataCode"), text(item, "description"));
            case "passenger-terminals" -> passengerTerminal(text(item, "airport"), text(item, "code"), text(item, "name"), text(item, "terminalType"), bool(item, "international"));
            case "timezone-dst-periods" -> timezoneDst(text(item, "timezone"), dateTime(item, "dstStart"), dateTime(item, "dstEnd"), integer(item, "offsetMinutes"), dateTime(item, "effectiveFrom"), dateTime(item, "effectiveTo"));
            case "alliances" -> alliance(text(item, "code"), text(item, "name"), text(item, "iataCode"), text(item, "website"), date(item, "foundedDate"), integer(item, "displayOrder"));
            case "airline-aliases" -> airlineAlias(text(item, "airline"), text(item, "code"), text(item, "name"), enumValue(AirlineAliasType.class, item, "aliasType"), integer(item, "displayOrder"));
            case "airline-business-roles" -> airlineRole(text(item, "airline"), text(item, "code"), text(item, "name"), enumValue(AirlineRoleScope.class, item, "scope"), enumValue(AirlineRoleCategory.class, item, "category"), integer(item, "displayOrder"));
            case "airline-contacts" -> airlineContact(text(item, "airline"), text(item, "code"), text(item, "name"), text(item, "department"), enumValue(AirlineContactType.class, item, "contactType"), text(item, "email"), text(item, "phone"), bool(item, "available24x7"), integer(item, "displayOrder"));
            case "alliance-members" -> allianceMember(text(item, "alliance"), text(item, "airline"), enumValue(AllianceMembershipType.class, item, "membershipType"), date(item, "joinDate"), bool(item, "primary"), integer(item, "displayOrder"));
            default -> {
            }
        }
    }

    private void trafficQualifier(String restrictionCode, String code, String name, String definition, int order) {
        trafficRestrictionCodeRepository.findByRestrictionCode(restrictionCode)
                .ifPresent(parent -> trafficQualifier(parent, code, name, definition, order));
    }

    private void airlineAlias(String airlineCode, String code, String name, AirlineAliasType type, int order) {
        airlineCarrierRepository.findByIataCode(airlineCode)
                .ifPresent(airline -> airlineAlias(airline, code, name, type, order));
    }

    private void airlineRole(String airlineCode, String code, String name, AirlineRoleScope scope, AirlineRoleCategory category, int order) {
        airlineCarrierRepository.findByIataCode(airlineCode)
                .ifPresent(airline -> airlineRole(airline, code, name, scope, category, order));
    }

    private void airlineContact(String airlineCode, String code, String name, String department, AirlineContactType type, String email, String phone, boolean available24x7, int order) {
        airlineCarrierRepository.findByIataCode(airlineCode)
                .ifPresent(airline -> airlineContact(airline, code, name, department, type, email, phone, available24x7, order));
    }

    private void allianceMember(String allianceCode, String airlineCode, AllianceMembershipType membershipType, LocalDate joinDate, boolean primary, int order) {
        var alliance = allianceRepository.findByAllianceCode(allianceCode);
        var airline = airlineCarrierRepository.findByIataCode(airlineCode);
        if (alliance.isPresent() && airline.isPresent()) {
            allianceMember(alliance.get(), airline.get(), membershipType, joinDate, primary, order);
        }
    }

    private String text(JsonNode node, String field) {
        JsonNode value = node.path(field);
        return value.isMissingNode() || value.isNull() ? null : value.asText();
    }

    private int integer(JsonNode node, String field) {
        JsonNode value = node.path(field);
        return value.isMissingNode() || value.isNull() ? 1 : value.asInt();
    }

    private boolean bool(JsonNode node, String field) {
        JsonNode value = node.path(field);
        return !value.isMissingNode() && !value.isNull() && value.asBoolean();
    }

    private LocalDate date(JsonNode node, String field) {
        String value = text(node, field);
        return value == null || value.isBlank() ? null : LocalDate.parse(value);
    }

    private LocalDateTime dateTime(JsonNode node, String field) {
        String value = text(node, field);
        return value == null || value.isBlank() ? null : LocalDateTime.parse(value);
    }

    private <E extends Enum<E>> E enumValue(Class<E> type, JsonNode node, String field) {
        return Enum.valueOf(type, text(node, field));
    }

    private void scheduleSource(String code, String name, String description) {
        if (scheduleSourceRepository.findBySourceCode(code).isPresent()) return;
        ScheduleSource item = new ScheduleSource();
        item.setSourceCode(code);
        item.setSourceName(name);
        base(item, description);
        scheduleSourceRepository.save(item);
    }

    private void scheduleChannel(String code, String name, String description) {
        if (scheduleChannelRepository.findByChannelCode(code).isPresent()) return;
        ScheduleChannel item = new ScheduleChannel();
        item.setChannelCode(code);
        item.setChannelName(name);
        base(item, description);
        scheduleChannelRepository.save(item);
    }

    private void scheduleStatus(String code, String name, String description) {
        if (scheduleStatusRepository.findByScheduleStatusCode(code).isPresent()) return;
        ScheduleStatus item = new ScheduleStatus();
        item.setScheduleStatusCode(code);
        item.setScheduleStatusName(name);
        base(item, description);
        scheduleStatusRepository.save(item);
    }

    private void scheduleType(String code, String name, String description) {
        if (scheduleTypeRepository.existsByScheduleTypeCode(code)) return;
        ScheduleType item = new ScheduleType();
        item.setScheduleTypeCode(code);
        item.setScheduleTypeName(name);
        base(item, description);
        scheduleTypeRepository.save(item);
    }

    private void schedulePriority(String code, String name, Integer level, String description) {
        if (schedulePriorityRepository.findByPriorityCode(code).isPresent()) return;
        SchedulePriority item = new SchedulePriority();
        item.setPriorityCode(code);
        item.setPriorityName(name);
        item.setPriorityLevel(level);
        base(item, description);
        schedulePriorityRepository.save(item);
    }

    private void scheduleCategory(String code, String name, String description) {
        if (scheduleCategoryRepository.findByCategoryCode(code).isPresent()) return;
        ScheduleCategory item = new ScheduleCategory();
        item.setCategoryCode(code);
        item.setCategoryName(name);
        base(item, description);
        scheduleCategoryRepository.save(item);
    }

    private void operationalSuffix(String code, String name, String description) {
        if (operationalSuffixRepository.findBySuffixCode(code).isPresent()) return;
        OperationalSuffix item = new OperationalSuffix();
        item.setSuffixCode(code);
        item.setSuffixName(name);
        base(item, description);
        operationalSuffixRepository.save(item);
    }

    private void season(String code, String name, SeasonType type, int year, LocalDate start, LocalDate end, int order) {
        if (seasonRepository.existsBySeasonCode(code)) return;
        Season item = new Season();
        item.setSeasonCode(code);
        item.setSeasonName(name);
        item.setSeasonType(type);
        item.setScheduleYear(year);
        item.setSeasonStartDate(start);
        item.setSeasonEndDate(end);
        item.setDisplayOrder(order);
        item.setDescription(name + " IATA scheduling season.");
        item.setEffectiveFrom(start);
        item.setEffectiveTo(end);
        item.setRecordStatus(RecordStatus.ACTIVE);
        seasonRepository.save(item);
    }

    private void standardMessage(String code, String name, String description) {
        if (standardMessageIdentifierRepository.existsByMessageIdentifier(code)) return;
        StandardMessageIdentifier item = new StandardMessageIdentifier();
        item.setMessageIdentifier(code);
        item.setMessageIdentifierName(name);
        base(item, description);
        standardMessageIdentifierRepository.save(item);
    }

    private void actionIdentifier(String code, String name, String description) {
        if (actionIdentifierRepository.existsByActionCode(code)) return;
        ActionIdentifier item = new ActionIdentifier();
        item.setActionCode(code);
        item.setActionName(name);
        base(item, description);
        actionIdentifierRepository.save(item);
    }

    private void actionCode(String code, String name, String description) {
        if (actionCodeRepository.existsByActionCode(code)) return;
        ActionCode item = new ActionCode();
        item.setActionCode(code);
        item.setActionName(name);
        base(item, description);
        actionCodeRepository.save(item);
    }

    private void dataElement(String code, String name, String scope, String description) {
        if (dataElementIdentifierRepository.existsByDeiCode(code)) return;
        DataElementIdentifier item = new DataElementIdentifier();
        item.setDeiCode(code);
        item.setDeiName(name);
        item.setDeiScope(scope);
        base(item, description);
        dataElementIdentifierRepository.save(item);
    }

    private void distributionChannel(String code, String name, String type, String protocol, String endpoint) {
        if (distributionChannelRepository.existsByChannelCode(code)) return;
        DistributionChannel item = new DistributionChannel();
        item.setChannelCode(code);
        item.setChannelName(name);
        item.setChannelType(type);
        item.setProtocolType(protocol);
        item.setEndpointUrl(endpoint);
        item.setAutoSend(Boolean.TRUE);
        item.setRecordStatus(RecordStatus.ACTIVE);
        item.setEffectiveFrom(EFFECTIVE_FROM);
        distributionChannelRepository.save(item);
    }

    private void messagePriority(String code, String name, Integer level, String description) {
        if (messagePriorityRepository.existsByPriorityCode(code)) return;
        MessagePriority item = new MessagePriority();
        item.setPriorityCode(code);
        item.setPriorityName(name);
        item.setPriorityLevel(level);
        base(item, description);
        messagePriorityRepository.save(item);
    }

    private void messageStatus(String code, String name, String description) {
        if (messageStatusRepository.existsByMessageStatusCode(code)) return;
        MessageStatus item = new MessageStatus();
        item.setMessageStatusCode(code);
        item.setMessageStatusName(name);
        base(item, description);
        messageStatusRepository.save(item);
    }

    private void rejectReason(String code, String name, String description) {
        if (rejectReasonRepository.existsByRejectReasonCode(code)) return;
        RejectReason item = new RejectReason();
        item.setRejectReasonCode(code);
        item.setRejectReasonName(name);
        base(item, description);
        rejectReasonRepository.save(item);
    }

    private void serviceType(String code, String name, String category, String definition, int order) {
        if (serviceTypeRepository.existsByServiceTypeCode(code)) return;
        ServiceType item = new ServiceType();
        item.setServiceTypeCode(code);
        item.setServiceTypeName(name);
        item.setCategory(category);
        active(item, definition, order);
        serviceTypeRepository.save(item);
    }

    private void timeMode(String code, String name, String description, int order) {
        if (timeModeRepository.existsByTimeModeCode(code)) return;
        TimeMode item = new TimeMode();
        item.setTimeModeCode(code);
        item.setTimeModeName(name);
        active(item, description, order);
        timeModeRepository.save(item);
    }

    private void flightFrequency(String code, String name, String description, int order) {
        if (flightFrequencyRepository.existsByFrequencyCode(code)) return;
        FlightFrequency item = new FlightFrequency();
        item.setFrequencyCode(code);
        item.setFrequencyName(name);
        active(item, description, order);
        flightFrequencyRepository.save(item);
    }

    private void flightSuffix(String code, String name, String meaning, int order) {
        if (flightSuffixRepository.existsBySuffixCode(code)) return;
        FlightSuffix item = new FlightSuffix();
        item.setSuffixCode(code);
        item.setSuffixName(name);
        item.setSuffixMeaning(meaning);
        active(item, meaning, order);
        flightSuffixRepository.save(item);
    }

    private void electronicTicketIndicator(String code, String name, String description, int order) {
        if (electronicTicketIndicatorRepository.existsByIndicatorCode(code)) return;
        ElectronicTicketIndicator item = new ElectronicTicketIndicator();
        item.setIndicatorCode(code);
        item.setIndicatorName(name);
        active(item, description, order);
        electronicTicketIndicatorRepository.save(item);
    }

    private void secureFlightIndicator(String code, String name, String authority, String meaning, int order) {
        if (secureFlightIndicatorRepository.existsBySecureFlightIndicatorCode(code)) return;
        SecureFlightIndicator item = new SecureFlightIndicator();
        item.setSecureFlightIndicatorCode(code);
        item.setSecureFlightIndicatorName(name);
        item.setRegulatoryAuthority(authority);
        item.setIndicatorMeaning(meaning);
        active(item, meaning, order);
        secureFlightIndicatorRepository.save(item);
    }

    private void meal(String code, String name, String category, CabinClass cabinClass, String definition, int order) {
        if (mealServiceRepository.existsByMealCode(code)) return;
        MealService item = new MealService();
        item.setMealCode(code);
        item.setMealName(name);
        item.setMealCategory(category);
        item.setCabinClass(cabinClass);
        active(item, definition, order);
        mealServiceRepository.save(item);
    }

    private void rbd(String code, String name, CabinClass cabinClass, String category, int order) {
        if (reservationBookingDesignatorRepository.existsByBookingDesignator(code)) return;
        ReservationBookingDesignator item = new ReservationBookingDesignator();
        item.setBookingDesignator(code);
        item.setBookingName(name);
        item.setCabinClass(cabinClass);
        item.setCategory(category);
        active(item, name + " reservation booking designator.", order);
        reservationBookingDesignatorRepository.save(item);
    }

    private void rbdModifier(String code, String name, String category, String definition, int order) {
        if (reservationBookingModifierRepository.existsByModifierCode(code)) return;
        ReservationBookingModifier item = new ReservationBookingModifier();
        item.setModifierCode(code);
        item.setModifierName(name);
        item.setCategory(category);
        active(item, definition, order);
        reservationBookingModifierRepository.save(item);
    }

    private TrafficRestrictionCode trafficRestriction(String code, String name, String category, String definition, int order) {
        return trafficRestrictionCodeRepository.findByRestrictionCode(code).orElseGet(() -> {
            TrafficRestrictionCode item = new TrafficRestrictionCode();
            item.setRestrictionCode(code);
            item.setRestrictionName(name);
            item.setCategory(category);
            active(item, definition, order);
            return trafficRestrictionCodeRepository.save(item);
        });
    }

    private void trafficQualifier(TrafficRestrictionCode restrictionCode, String code, String name, String definition, int order) {
        if (trafficRestrictionQualifierRepository.existsByTrafficRestrictionCode_RestrictionCodeAndQualifierCode(restrictionCode.getRestrictionCode(), code)) return;
        TrafficRestrictionQualifier item = new TrafficRestrictionQualifier();
        item.setTrafficRestrictionCode(restrictionCode);
        item.setQualifierCode(code);
        item.setQualifierName(name);
        active(item, definition, order);
        trafficRestrictionQualifierRepository.save(item);
    }

    private void trafficConferenceArea(String code, String name, String iataCode, String description) {
        if (trafficConferenceAreaRepository.existsByAreaCode(code)) return;
        TrafficConferenceArea item = new TrafficConferenceArea();
        item.setAreaCode(code);
        item.setAreaName(name);
        item.setIataAreaCode(iataCode);
        base(item, description);
        trafficConferenceAreaRepository.save(item);
    }

    private void airportTerminal(String airportCode, String code, String name, String iataCode, String description) {
        if (airportTerminalRepository.existsByTerminalCode(code)) return;
        var airport = airportRepository.findByIataCode(airportCode);
        if (airport.isEmpty()) {
            return;
        }

        AirportTerminal item = new AirportTerminal();
        item.setAirport(airport.get());
        item.setTerminalCode(code);
        item.setTerminalName(name);
        item.setIataTerminalCode(iataCode);
        item.setDescription(description);
        item.setRecordStatus(RecordStatus.ACTIVE);
        item.setEffectiveFrom(EFFECTIVE_FROM);
        airportTerminalRepository.save(item);
    }

    private void passengerTerminal(String airportCode, String code, String name, String type, boolean international) {
        if (passengerTerminalRepository.existsByAirport_IataCodeAndTerminalCode(airportCode, code)) return;
        var airport = airportRepository.findByIataCode(airportCode);
        if (airport.isEmpty()) {
            return;
        }

        PassengerTerminal item = new PassengerTerminal();
        item.setAirport(airport.get());
        item.setTerminalCode(code);
        item.setTerminalName(name);
        item.setTerminalType(type);
        item.setInternationalFlag(international);
        item.setStatus(RecordStatus.ACTIVE);
        passengerTerminalRepository.save(item);
    }

    private void timezoneDst(String timezoneId,
                             LocalDateTime dstStart,
                             LocalDateTime dstEnd,
                             int offsetMinutes,
                             LocalDateTime effectiveFrom,
                             LocalDateTime effectiveTo) {
        if (dstRuleRepository.existsByTimezone_TzIdentifierAndEffectiveFrom(timezoneId, effectiveFrom)) return;
        var timezone = timezoneRepository.findByTzIdentifier(timezoneId);
        if (timezone.isEmpty()) {
            return;
        }

        TimezoneDLS item = new TimezoneDLS();
        item.setTimezone(timezone.get());
        item.setDstStart(dstStart);
        item.setDstEnd(dstEnd);
        item.setDstOffsetMinutes(offsetMinutes);
        item.setEffectiveFrom(effectiveFrom);
        item.setEffectiveTo(effectiveTo);
        item.setRecordStatus(RecordStatus.ACTIVE);
        dstRuleRepository.save(item);
    }

    private Alliance alliance(String code, String name, String iataCode, String website, LocalDate foundedDate, int order) {
        return allianceRepository.findByAllianceCode(code).orElseGet(() -> {
            Alliance item = new Alliance();
            item.setAllianceCode(code);
            item.setAllianceName(name);
            item.setIataCode(iataCode);
            item.setWebsite(website);
            item.setFoundedDate(foundedDate);
            item.setDisplayOrder(order);
            item.setDescription(name + " global airline alliance.");
            item.setRecordStatus(RecordStatus.ACTIVE);
            item.setEffectiveFrom(EFFECTIVE_FROM);
            return allianceRepository.save(item);
        });
    }

    private void airlineAlias(AirlineCarrier airline, String code, String name, AirlineAliasType type, int order) {
        if (airlineAliasRepository.existsByAirline_IataCodeAndAliasCode(airline.getIataCode(), code)) return;
        AirlineAlias item = new AirlineAlias();
        item.setAirline(airline);
        item.setAliasCode(code);
        item.setAliasName(name);
        item.setAliasType(type);
        activeAirline(item, name + " alias for " + airline.getDisplayName() + ".", order);
        airlineAliasRepository.save(item);
    }

    private void airlineRole(AirlineCarrier airline,
                             String code,
                             String name,
                             AirlineRoleScope scope,
                             AirlineRoleCategory category,
                             int order) {
        if (airlineBusinessRoleRepository.existsByAirline_IataCodeAndRoleCode(airline.getIataCode(), code)) return;
        AirlineBusinessRole item = new AirlineBusinessRole();
        item.setAirline(airline);
        item.setRoleCode(code);
        item.setRoleName(name);
        item.setRoleScope(scope);
        item.setRoleCategory(category);
        activeAirline(item, name + " role for airline master and schedule workflows.", order);
        airlineBusinessRoleRepository.save(item);
    }

    private void airlineContact(AirlineCarrier airline,
                                String code,
                                String name,
                                String department,
                                AirlineContactType type,
                                String email,
                                String phone,
                                boolean available24x7,
                                int order) {
        if (airlineContactRepository.existsByAirline_IataCodeAndContactCode(airline.getIataCode(), code)) return;
        AirlineContact item = new AirlineContact();
        item.setAirline(airline);
        item.setContactCode(code);
        item.setContactName(name);
        item.setDesignation("Operations Contact");
        item.setDepartment(department);
        item.setContactType(type);
        item.setPreferredCommunication(CommunicationMethod.EMAIL);
        item.setEmail(email);
        item.setPhone(phone);
        item.setAvailable24x7(available24x7);
        item.setEmergencyContact(available24x7);
        activeAirline(item, department + " contact for " + airline.getDisplayName() + ".", order);
        airlineContactRepository.save(item);
    }

    private void allianceMember(Alliance alliance,
                                AirlineCarrier airline,
                                AllianceMembershipType membershipType,
                                LocalDate joinDate,
                                boolean primary,
                                int order) {
        if (allianceMemberRepository.existsByAlliance_AllianceCodeAndAirline_IataCode(alliance.getAllianceCode(), airline.getIataCode())) return;
        AllianceMember item = new AllianceMember();
        item.setAlliance(alliance);
        item.setAirline(airline);
        item.setMembershipType(membershipType);
        item.setMembershipStatus(AllianceMembershipStatus.ACTIVE);
        item.setJoinDate(joinDate);
        item.setPrimaryMember(primary);
        activeAirline(item, airline.getDisplayName() + " membership in " + alliance.getAllianceName() + ".", order);
        allianceMemberRepository.save(item);
    }

    private void base(ScheduleSource item, String description) { item.setDescription(description); item.setRecordStatus(RecordStatus.ACTIVE); item.setEffectiveFrom(EFFECTIVE_FROM); }
    private void base(ScheduleChannel item, String description) { item.setDescription(description); item.setRecordStatus(RecordStatus.ACTIVE); item.setEffectiveFrom(EFFECTIVE_FROM); }
    private void base(ScheduleStatus item, String description) { item.setDescription(description); item.setRecordStatus(RecordStatus.ACTIVE); item.setEffectiveFrom(EFFECTIVE_FROM); }
    private void base(ScheduleType item, String description) { item.setDescription(description); item.setRecordStatus(RecordStatus.ACTIVE); item.setEffectiveFrom(EFFECTIVE_FROM); }
    private void base(SchedulePriority item, String description) { item.setDescription(description); item.setRecordStatus(RecordStatus.ACTIVE); item.setEffectiveFrom(EFFECTIVE_FROM); }
    private void base(ScheduleCategory item, String description) { item.setDescription(description); item.setRecordStatus(RecordStatus.ACTIVE); item.setEffectiveFrom(EFFECTIVE_FROM); }
    private void base(OperationalSuffix item, String description) { item.setDescription(description); item.setRecordStatus(RecordStatus.ACTIVE); item.setEffectiveFrom(EFFECTIVE_FROM); }
    private void base(StandardMessageIdentifier item, String description) { item.setDescription(description); item.setRecordStatus(RecordStatus.ACTIVE); item.setEffectiveFrom(EFFECTIVE_FROM); }
    private void base(ActionIdentifier item, String description) { item.setDescription(description); item.setRecordStatus(RecordStatus.ACTIVE); item.setEffectiveFrom(EFFECTIVE_FROM); }
    private void base(ActionCode item, String description) { item.setDescription(description); item.setRecordStatus(RecordStatus.ACTIVE); item.setEffectiveFrom(EFFECTIVE_FROM); }
    private void base(DataElementIdentifier item, String description) { item.setDescription(description); item.setRecordStatus(RecordStatus.ACTIVE); item.setEffectiveFrom(EFFECTIVE_FROM); }
    private void base(MessagePriority item, String description) { item.setDescription(description); item.setRecordStatus(RecordStatus.ACTIVE); item.setEffectiveFrom(EFFECTIVE_FROM); }
    private void base(MessageStatus item, String description) { item.setDescription(description); item.setRecordStatus(RecordStatus.ACTIVE); item.setEffectiveFrom(EFFECTIVE_FROM); }
    private void base(RejectReason item, String description) { item.setDescription(description); item.setRecordStatus(RecordStatus.ACTIVE); item.setEffectiveFrom(EFFECTIVE_FROM); }
    private void base(TrafficConferenceArea item, String description) { item.setDescription(description); item.setRecordStatus(RecordStatus.ACTIVE); item.setEffectiveFrom(EFFECTIVE_FROM); }

    private void active(ServiceType item, String description, int order) { item.setDescription(description); item.setIataDefinition(description); item.setDisplayOrder(order); item.setRecordStatus(RecordStatus.ACTIVE); item.setEffectiveFrom(EFFECTIVE_FROM); }
    private void active(TimeMode item, String description, int order) { item.setDescription(description); item.setDisplayOrder(order); item.setRecordStatus(RecordStatus.ACTIVE); item.setEffectiveFrom(EFFECTIVE_FROM); }
    private void active(FlightFrequency item, String description, int order) { item.setDescription(description); item.setDisplayOrder(order); item.setRecordStatus(RecordStatus.ACTIVE); item.setEffectiveFrom(EFFECTIVE_FROM); }
    private void active(FlightSuffix item, String description, int order) { item.setDescription(description); item.setDisplayOrder(order); item.setRecordStatus(RecordStatus.ACTIVE); item.setEffectiveFrom(EFFECTIVE_FROM); }
    private void active(ElectronicTicketIndicator item, String description, int order) { item.setDescription(description); item.setIataDefinition(description); item.setDisplayOrder(order); item.setRecordStatus(RecordStatus.ACTIVE); item.setEffectiveFrom(EFFECTIVE_FROM); }
    private void active(SecureFlightIndicator item, String description, int order) { item.setDescription(description); item.setDisplayOrder(order); item.setRecordStatus(RecordStatus.ACTIVE); item.setEffectiveFrom(EFFECTIVE_FROM); }
    private void active(MealService item, String description, int order) { item.setDescription(description); item.setIataDefinition(description); item.setDisplayOrder(order); item.setRecordStatus(RecordStatus.ACTIVE); item.setEffectiveFrom(EFFECTIVE_FROM); }
    private void active(ReservationBookingDesignator item, String description, int order) { item.setDescription(description); item.setIataDefinition(description); item.setDisplayOrder(order); item.setRecordStatus(RecordStatus.ACTIVE); item.setEffectiveFrom(EFFECTIVE_FROM); }
    private void active(ReservationBookingModifier item, String description, int order) { item.setDescription(description); item.setIataDefinition(description); item.setDisplayOrder(order); item.setRecordStatus(RecordStatus.ACTIVE); item.setEffectiveFrom(EFFECTIVE_FROM); }
    private void active(TrafficRestrictionCode item, String description, int order) { item.setDescription(description); item.setIataDefinition(description); item.setDisplayOrder(order); item.setRecordStatus(RecordStatus.ACTIVE); item.setEffectiveFrom(EFFECTIVE_FROM); }
    private void active(TrafficRestrictionQualifier item, String description, int order) { item.setDescription(description); item.setIataDefinition(description); item.setDisplayOrder(order); item.setRecordStatus(RecordStatus.ACTIVE); item.setEffectiveFrom(EFFECTIVE_FROM); }

    private void activeAirline(AirlineAlias item, String description, int order) { item.setDescription(description); item.setDisplayOrder(order); item.setRecordStatus(RecordStatus.ACTIVE); item.setEffectiveFrom(EFFECTIVE_FROM); }
    private void activeAirline(AirlineBusinessRole item, String description, int order) { item.setDescription(description); item.setDisplayOrder(order); item.setRecordStatus(RecordStatus.ACTIVE); item.setEffectiveFrom(EFFECTIVE_FROM); }
    private void activeAirline(AirlineContact item, String description, int order) { item.setDescription(description); item.setDisplayOrder(order); item.setRecordStatus(RecordStatus.ACTIVE); item.setEffectiveFrom(EFFECTIVE_FROM); }
    private void activeAirline(AllianceMember item, String description, int order) { item.setDescription(description); item.setDisplayOrder(order); item.setRecordStatus(RecordStatus.ACTIVE); item.setEffectiveFrom(EFFECTIVE_FROM); }
}
