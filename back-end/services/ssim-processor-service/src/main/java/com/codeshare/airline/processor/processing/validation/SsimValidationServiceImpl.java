package com.codeshare.airline.processor.validation;


import com.codeshare.airline.processor.model.raw.SsimR1HeaderRecord;
import com.codeshare.airline.processor.model.raw.SsimR3FlightLegRecord;
import com.codeshare.airline.processor.model.raw.SsimR4DateVariationRecord;
import com.codeshare.airline.processor.model.ssim.SsimR5DateVariationContinuation;
import com.codeshare.airline.processor.pipeline.model.SsimLoadContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SsimValidationServiceImpl implements SsimValidationService {

    @Override
    public void validate(SsimLoadContext context) {

        /* =========================================================
         * R1 — HEADER VALIDATION
         * ========================================================= */

        SsimR1HeaderRecord header = context.getHeader();

        if (header == null) {
            throw new IllegalStateException("SSIM validation failed: missing Record-1 header");
        }

        if (header.getPeriodStartDate().isAfter(header.getPeriodEndDate())) {
            throw new IllegalStateException(
                    "SSIM validation failed: schedule period start > end"
            );
        }

        /* =========================================================
         * R3 — FLIGHT LEG VALIDATION
         * ========================================================= */

        List<SsimR3FlightLegRecord> legs = context.getFlightLegs();

        if (legs == null || legs.isEmpty()) {
            throw new IllegalStateException(
                    "SSIM validation failed: no Record-3 flight legs found"
            );
        }

        // ---- Duplicate R3 identity check ----
        Set<String> r3Keys = new HashSet<>();

        for (SsimR3FlightLegRecord leg : legs) {

            String key = buildR3Key(leg);

            if (!r3Keys.add(key)) {
                throw new IllegalStateException(
                        "Duplicate Record-3 flight leg detected: " + key
                );
            }

            // effective dates must be within R1 period
            if (leg.getEffectiveFrom() != null &&
                    leg.getEffectiveFrom().isBefore(header.getPeriodStartDate())) {

                throw new IllegalStateException(
                        "Record-3 effective-from before SSIM period: " + key
                );
            }

            if (leg.getEffectiveTo() != null &&
                    leg.getEffectiveTo().isAfter(header.getPeriodEndDate())) {

                throw new IllegalStateException(
                        "Record-3 effective-to after SSIM period: " + key
                );
            }
        }

        /* =========================================================
         * R4 — DATE VARIATION VALIDATION
         * ========================================================= */

        List<SsimR4DateVariationRecord> r4List = context.getDateVariations();

        for (SsimR4DateVariationRecord r4 : r4List) {

            if (r4.getFlightLeg() == null) {
                throw new IllegalStateException(
                        "Orphan Record-4 detected (no parent Record-3)"
                );
            }

            if (!isValidAction(r4.getActionCode())) {
                throw new IllegalStateException(
                        "Invalid Record-4 action code: " + r4.getActionCode()
                );
            }

            LocalDate date = r4.getVariationDate();

            if (date.isBefore(header.getPeriodStartDate()) ||
                    date.isAfter(header.getPeriodEndDate())) {

                throw new IllegalStateException(
                        "Record-4 variation date outside SSIM period: " + date
                );
            }
        }

        /* =========================================================
         * R5 — CONTINUATION DATE VALIDATION
         * ========================================================= */

        List<SsimR5DateVariationContinuation> r5List =
                context.getDateVariationContinuations();

        for (SsimR5DateVariationContinuation r5 : r5List) {

            if (r5.getParentVariation() == null) {
                throw new IllegalStateException(
                        "Orphan Record-5 detected (no parent Record-4)"
                );
            }

            LocalDate date = r5.getVariationDate();

            if (date.isBefore(header.getPeriodStartDate()) ||
                    date.isAfter(header.getPeriodEndDate())) {

                throw new IllegalStateException(
                        "Record-5 variation date outside SSIM period: " + date
                );
            }
        }

        /* =========================================================
         * FINAL STATUS
         * ========================================================= */

        header.setStatus("VALIDATED");
    }

    /* =========================================================
     * HELPERS
     * ========================================================= */

    private boolean isValidAction(String action) {
        return action != null && (action.equals("A") || action.equals("C") || action.equals("D"));
    }

    private String buildR3Key(SsimR3FlightLegRecord leg) {
        return leg.getAirlineDesignator()
                + leg.getFlightNumber()
                + leg.getFlightSuffix()
                + leg.getItineraryVariation()
                + leg.getLegSequenceNumber();
    }
}
