package com.codeshare.airline.master.messaging.data;

import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import com.codeshare.airline.master.messaging.entities.ActionIdentifier;
import com.codeshare.airline.master.messaging.repository.ActionIdentifierRepository;
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

        save("NEW", "New Schedule", "Insertion of new flight information", MessageType.SSM);
        save("CNL", "Cancellation", "Cancellation of flight", MessageType.SSM);
        save("TIM", "Time Change", "Change in scheduled time", MessageType.SSM);
        save("EQT", "Equipment Change", "Change in aircraft type", MessageType.SSM);
        save("RIN", "Reinstatement", "Reinstatement of a previously cancelled flight", MessageType.SSM);
        save("REV", "Revision", "Revision of selected schedule fields", MessageType.SSM);
        save("COD", "Codeshare Change", "Change in codeshare disclosure or marketing flight details", MessageType.SSM);
        save("FLT", "Flight Detail Change", "General or combined flight detail change", MessageType.SSM);
        save("CON", "Configuration Change", "Change in aircraft configuration", MessageType.SSM);
        save("RRT", "Routing Change", "Change in routing", MessageType.SSM);
        save("ADM", "Administrative Update", "DEI only change", MessageType.SSM);
    }

    private void save(String code, String name, String desc, MessageType scheduleType) {

        ActionIdentifier entity = new ActionIdentifier();
        entity.setActionCode(code);
        entity.setActionName(name);
        entity.setDescription(desc);
        entity.setMessageType(scheduleType);
        entity.setRecordStatus(RecordStatus.ACTIVE);
        entity.setEffectiveFrom(LocalDate.now());

        repository.save(entity);
    }
}
