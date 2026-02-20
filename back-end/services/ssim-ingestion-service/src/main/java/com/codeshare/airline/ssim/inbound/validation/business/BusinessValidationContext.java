package com.codeshare.airline.ssim.inbound.validation.business;

import com.codeshare.airline.ssim.inbound.parsing.processor.dto.SsimInboundCarrierDTO;
import com.codeshare.airline.ssim.inbound.parsing.processor.dto.SsimInboundFileDTO;
import com.codeshare.airline.ssim.inbound.validation.model.ValidationMessage;

import java.util.ArrayList;
import java.util.List;

public class BusinessValidationContext {

    private final SsimInboundFileDTO inboundFile;

    private final SsimInboundCarrierDTO ssimInboundCarrier;

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

    public SsimInboundCarrierDTO getSsimInboundCarrier() {
        return ssimInboundCarrier;
    }
}
