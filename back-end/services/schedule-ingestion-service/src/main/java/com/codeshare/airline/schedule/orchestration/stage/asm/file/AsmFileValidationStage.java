package com.codeshare.airline.schedule.orchestration.stage.asm.file;

import com.codeshare.airline.schedule.domain.common.ScheduleProfile;
import com.codeshare.airline.schedule.domain.context.AsmIngestionContext;
import com.codeshare.airline.schedule.orchestration.stage.FileValidationStage;
import com.codeshare.airline.schedule.orchestration.stage.asm.file.extension.AsmFileExtensionValidation;
import com.codeshare.airline.schedule.orchestration.stage.asm.file.profile.AsmProfileValidationStage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AsmFileValidationStage implements FileValidationStage<AsmIngestionContext> {

    private final AsmFileExtensionValidation defaultAsmFileExtensionStage;
    private final AsmProfileValidationStage asmProfileValidationStage;

    @Override
    public void execute(AsmIngestionContext context) {

        validateExtension(context);

        detectProfile(context);
    }

    private void validateExtension(AsmIngestionContext context) {

        defaultAsmFileExtensionStage.validate(context.getSourceFile());
    }

    private void detectProfile(AsmIngestionContext context) {

        ScheduleProfile profile =
                context.getSourceFile()
                        .withStream(asmProfileValidationStage::detect);

        context.setProfile(profile);
    }
}