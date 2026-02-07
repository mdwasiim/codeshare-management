package com.codeshare.airline.processor.validation;

import com.codeshare.airline.processor.model.ssim.SsimAppendixHCodeshare;
import com.codeshare.airline.processor.model.raw.SsimR1HeaderRecord;
import com.codeshare.airline.processor.model.raw.SsimR3FlightLegRecord;
import com.codeshare.airline.processor.persistence.repository.SsimAppendixHCodeshareRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppendixHValidationService {

    private final SsimAppendixHCodeshareRepository appendixHRepository;

    public void validate(SsimR1HeaderRecord header) {

        List<SsimAppendixHCodeshare> records =
                appendixHRepository.findByOperatingLeg_SsimR1Header(header);

        Map<SsimR3FlightLegRecord, List<SsimAppendixHCodeshare>> byLeg =
                records.stream()
                        .collect(Collectors.groupingBy(
                                SsimAppendixHCodeshare::getOperatingLeg
                        ));

        for (var entry : byLeg.entrySet()) {
            validatePerOperatingLeg(entry.getKey(), entry.getValue());
        }
    }

    private void validatePerOperatingLeg(
            SsimR3FlightLegRecord leg,
            List<SsimAppendixHCodeshare> codeshares
    ) {

        // Rule B — unique marketing flight
        Set<String> marketingKeys = new HashSet<>();

        for (var cs : codeshares) {
            String key = cs.getMarketingCarrier()
                    + cs.getMarketingFlightNumber();

            if (!marketingKeys.add(key)) {
                throw new IllegalStateException(
                        "Duplicate marketing flight in Appendix-H: "
                                + key + " for operating leg "
                                + leg.getFlightNumber()
                );
            }
        }

        // Rule C — priority order validation
        List<Integer> priorities =
                codeshares.stream()
                        .map(SsimAppendixHCodeshare::getPriorityOrder)
                        .sorted()
                        .toList();

        for (int i = 0; i < priorities.size(); i++) {
            if (priorities.get(i) != i + 1) {
                throw new IllegalStateException(
                        "Invalid priority sequence for operating leg "
                                + leg.getFlightNumber()
                );
            }
        }

        // Rule D — disclosure indicator sanity
        for (var cs : codeshares) {
            String d = cs.getDisclosureIndicator();
            if (d != null && !d.isBlank() && !"Y".equals(d) && !"N".equals(d)) {
                throw new IllegalStateException(
                        "Invalid disclosure indicator: " + d
                );
            }
        }
    }
}
