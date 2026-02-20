package com.codeshare.airline.data.ssim.utils.data;

import com.codeshare.airline.core.enums.common.Status;
import com.codeshare.airline.data.ssim.eitities.MessageType;
import com.codeshare.airline.data.ssim.repository.MessageTypeRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class MessageTypeDataLoader {

    private final MessageTypeRepository repository;

    @PostConstruct
    public void load() {

        if (repository.count() > 0) return;

        save("ASM", "Adhoc Schedule Message",
                "Used for single date schedule changes");

        save("SSM", "Standard Schedule Message",
                "Used for seasonal schedule updates");

        save("SCR", "Slot Clearance Request",
                "Airport coordination request message");

        save("SHL", "Slot Historical List",
                "Slot coordination response message");
    }

    private void save(String code, String name, String description) {

        MessageType entity = new MessageType();
        entity.setMessageTypeCode(code);
        entity.setMessageTypeName(name);
        entity.setDescription(description);
        entity.setStatusCode(Status.ACTIVE);
        entity.setEffectiveFrom(LocalDate.now());

        repository.save(entity);
    }
}