package com.codeshare.airline.ssim.inbound.domain.contex;

import com.codeshare.airline.ssim.inbound.domain.enums.SsimProcessingStatus;
import com.codeshare.airline.ssim.inbound.domain.enums.SsimProfile;
import com.codeshare.airline.ssim.inbound.parsing.processor.dto.SsimInboundFileDTO;
import com.codeshare.airline.ssim.inbound.source.SsimSourceFile;
import com.codeshare.airline.ssim.inbound.validation.model.ValidationResult;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SsimIngestionContext {

    // ---- Immutable core ----
    private final SsimSourceFile sourceFile;
    private final SsimInboundFileDTO inboundFile;

    // ---- Processing metadata ----
    @Builder.Default
    private SsimProfile profile = null;

    @Builder.Default
    private ValidationResult structuralResult = null;

    @Builder.Default
    private ValidationResult businessResult = null;

    @Builder.Default
    private SsimProcessingStatus currentStatus =
            SsimProcessingStatus.RECEIVED;

    // ---- Controlled state transition ----
    public SsimIngestionContext withStatus(SsimProcessingStatus status) {
        return SsimIngestionContext.builder()
                .sourceFile(this.sourceFile)
                .inboundFile(this.inboundFile)
                .profile(this.profile)
                .structuralResult(this.structuralResult)
                .businessResult(this.businessResult)
                .currentStatus(status)
                .build();
    }

    public SsimIngestionContext withStructuralResult(ValidationResult result) {
        return SsimIngestionContext.builder()
                .sourceFile(this.sourceFile)
                .inboundFile(this.inboundFile)
                .profile(this.profile)
                .structuralResult(result)
                .businessResult(this.businessResult)
                .currentStatus(this.currentStatus)
                .build();
    }

    public SsimIngestionContext withBusinessResult(ValidationResult result) {
        return SsimIngestionContext.builder()
                .sourceFile(this.sourceFile)
                .inboundFile(this.inboundFile)
                .profile(this.profile)
                .structuralResult(this.structuralResult)
                .businessResult(result)
                .currentStatus(this.currentStatus)
                .build();
    }
}
