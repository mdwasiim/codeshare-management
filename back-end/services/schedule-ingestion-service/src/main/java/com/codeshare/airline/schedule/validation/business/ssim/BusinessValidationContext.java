package com.codeshare.airline.schedule.validation.business.ssim;

import com.codeshare.airline.schedule.parsing.ssim.dto.Record2Carrier;
import com.codeshare.airline.schedule.parsing.ssim.dto.SsimParsedFile;
import com.codeshare.airline.schedule.validation.model.ValidationMessage;

import java.util.ArrayList;
import java.util.List;

public class BusinessValidationContext {

    private final SsimParsedFile inboundFile;

    private final Record2Carrier ssimInboundCarrier;

    private final List<ValidationMessage> messages = new ArrayList<>();

    public BusinessValidationContext(SsimParsedFile inboundFile) {
        this.inboundFile = inboundFile;
        this.ssimInboundCarrier = inboundFile.getSsimInboundCarrier();
    }

    public void addMessage(ValidationMessage message) {
        messages.add(message);
    }

    public List<ValidationMessage> getMessages() {
        return messages;
    }

    public SsimParsedFile getInboundFile() {
        return inboundFile;
    }

    public Record2Carrier getSsimInboundCarrier() {
        return ssimInboundCarrier;
    }
}
