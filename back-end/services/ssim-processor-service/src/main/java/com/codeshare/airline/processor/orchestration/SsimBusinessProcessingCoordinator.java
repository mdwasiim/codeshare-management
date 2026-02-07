package com.codeshare.airline.processor.orchestration;

import com.codeshare.airline.processor.processing.expansion.CodeshareExpansionService;
import com.codeshare.airline.processor.processing.expansion.FlightScheduleExpander;
import com.codeshare.airline.processor.processing.expansion.repository.FlightScheduleRepository;
import com.codeshare.airline.processor.model.domain.FlightSchedule;
import com.codeshare.airline.processor.processing.persistence.SsimPersistenceService;
import com.codeshare.airline.processor.pipeline.enm.SsimLoadStatus;
import com.codeshare.airline.processor.pipeline.model.SsimLoadContext;
import com.codeshare.airline.processor.processing.resolution.CodeshareConflictResolver;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SsimBusinessProcessingCoordinator {

    private final FlightScheduleExpander flightScheduleExpander;
    private final CodeshareExpansionService codeshareExpansionService;
    private final CodeshareConflictResolver codeshareConflictResolver;
    private final FlightScheduleRepository flightScheduleRepository;
    private final SsimPersistenceService persistenceService;

    /**
     * Processes a VALIDATED SSIM dataset into business schedules.
     */
    @Transactional
    public void process(SsimLoadContext loadContext) {

        if (loadContext.getStatus() != SsimLoadStatus.VALIDATED) {
            throw new IllegalStateException("SSIM must be VALIDATED before business processing");
        }

        try {
            // 1️⃣ Phase-1: expand OPERATING schedules
            flightScheduleExpander.expand(loadContext);

            loadContext = persistenceService.updateStatus(loadContext, SsimLoadStatus.EXPANDED);

            // 2️⃣ Phase-2: expand MARKETING candidates
            List<FlightSchedule> marketingCandidates = codeshareExpansionService.expand(loadContext);

            // 3️⃣ Group candidates for conflict resolution
            Map<String, List<FlightSchedule>> grouped = marketingCandidates.stream().collect(Collectors.groupingBy(this::conflictKey));

            // 4️⃣ Resolve conflicts
            List<FlightSchedule> resolvedMarketingSchedules = new ArrayList<>();
            for (Map.Entry<String, List<FlightSchedule>> entry : grouped.entrySet()) {
                codeshareConflictResolver.resolve(entry.getValue()).ifPresent(resolvedMarketingSchedules::add);
            }

            // 5️⃣ Persist resolved schedules
            flightScheduleRepository.saveAll(resolvedMarketingSchedules);

            // 6️⃣ Mark SSIM load as COMPLETED
            persistenceService.updateStatus(loadContext, SsimLoadStatus.COMPLETED);

        } catch (Exception ex) {
            persistenceService.updateStatus(loadContext, SsimLoadStatus.FAILED);
            throw ex;
        }
    }

    private String conflictKey(FlightSchedule fs) {
        return fs.getCarrier() + '|'+ fs.getFlightNumber()+ '|'+ fs.getEffectiveFrom();
    }
}

