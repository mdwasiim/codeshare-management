package com.codeshare.airline.data.ssim.utils.data;

import com.codeshare.airline.core.enums.common.Status;
import com.codeshare.airline.data.ssim.eitities.ActionIdentifier;
import com.codeshare.airline.data.ssim.repository.ActionIdentifierRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class ActionIdentifierDataLoader {

    private final ActionIdentifierRepository repository;

    @PostConstruct
    public void load() {

        if (repository.count() > 0) return;

        save("NEW", "New Schedule", "Insertion of new flight information", "BOTH");
        save("CNL", "Cancellation", "Cancellation of flight", "BOTH");
        save("TIM", "Time Change", "Change in scheduled time", "BOTH");
        save("EQT", "Equipment Change", "Change in aircraft type", "BOTH");
        save("CON", "Configuration Change", "Change in aircraft configuration", "BOTH");
        save("RRT", "Routing Change", "Change in routing", "BOTH");
        save("ADM", "Administrative Update", "DEI only change", "BOTH");
    }

    private void save(String code, String name, String desc, String messageType) {

        ActionIdentifier entity = new ActionIdentifier();
        entity.setActionCode(code);
        entity.setActionName(name);
        entity.setDescription(desc);
        entity.setApplicableMessageType(messageType);
        entity.setStatusCode(Status.ACTIVE);
        entity.setEffectiveFrom(LocalDate.now());

        repository.save(entity);
    }
}