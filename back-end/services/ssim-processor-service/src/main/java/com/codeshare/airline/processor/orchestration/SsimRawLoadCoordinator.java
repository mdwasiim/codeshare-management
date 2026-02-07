package com.codeshare.airline.processor.orchestration;

import com.codeshare.airline.processor.processing.parsing.RecordRouter;
import com.codeshare.airline.processor.processing.persistence.SsimPersistenceService;
import com.codeshare.airline.processor.pipeline.enm.SsimLoadStatus;
import com.codeshare.airline.processor.pipeline.model.ParsedSsimResult;
import com.codeshare.airline.processor.pipeline.model.SsimLoadContext;
import com.codeshare.airline.processor.pipeline.model.SsimRawFile;
import com.codeshare.airline.processor.processing.validation.SsimValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SsimRawLoadCoordinator {

    private final RecordRouter recordRouter;
    private final SsimPersistenceService persistenceService;
    private final SsimValidationService validationService;

    /**
     * Loads and validates ONE SSIM file (transport-level).
     */
    @Transactional
    public SsimLoadContext load(SsimRawFile rawFile) {

        // 1️⃣ Parse raw SSIM
        ParsedSsimResult parsedResult = recordRouter.route(rawFile);

        // 2️⃣ Persist raw SSIM records (R1–R6)
        SsimLoadContext loadContext = persistenceService.persist(parsedResult);
        persistenceService.updateStatus(loadContext, SsimLoadStatus.LOADED);
        try {
            // 3️⃣ Structural + SSIM validation
            validationService.validate(loadContext);

            // 4️⃣ Transition to VALIDATED
            loadContext = persistenceService.updateStatus(loadContext, SsimLoadStatus.VALIDATED);

            return loadContext;

        } catch (Exception ex) {
            // 5️⃣ Mark load as FAILED with reason
            persistenceService.updateStatus(loadContext, SsimLoadStatus.FAILED);
            throw ex;
        }
    }
}
