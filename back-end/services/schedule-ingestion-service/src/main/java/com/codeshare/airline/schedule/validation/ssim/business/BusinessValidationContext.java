package com.codeshare.airline.schedule.validation.ssim.business;

import com.codeshare.airline.schedule.parsing.ssim.dto.Record2Carrier;
import com.codeshare.airline.schedule.parsing.ssim.dto.SsimInboundFileDTO;
import com.codeshare.airline.schedule.validation.ssim.model.ValidationMessage;

import java.util.ArrayList;
import java.util.List;

public class BusinessValidationContext {

    private final SsimInboundFileDTO inboundFile;

    private final Record2Carrier ssimInboundCarrier;

    private final List<ValidationMessage> messages = new ArrayList<>();

    public BusinessValidationContext(SsimInboundFileDTO inboundFile) {
        this.inboundFile = inboundFile;
        this.ssimInboundCarrier = inboundFile.getSsimInboundCarrier();
    }

    public void addMessage(ValidationMessage message) {
        messages.add(message);
    }

    public List<ValidationMessage> getMessages() {
        return messages;
    }

    public SsimInboundFileDTO getInboundFile() {
        return inboundFile;
    }

    public Record2Carrier getSsimInboundCarrier() {
        return ssimInboundCarrier;
    }
}
