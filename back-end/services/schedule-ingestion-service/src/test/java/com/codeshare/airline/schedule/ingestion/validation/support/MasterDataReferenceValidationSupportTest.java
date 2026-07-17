package com.codeshare.airline.schedule.ingestion.validation.support;

import com.codeshare.airline.schedule.ingestion.application.validation.MasterDataReferenceValidationPort;
import com.codeshare.airline.schedule.ingestion.domain.enums.ValidationStage;
import com.codeshare.airline.schedule.ingestion.validation.model.ValidationResult;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MasterDataReferenceValidationSupportTest {

    @Test
    void referenceErrorsAreClassifiedAsValidationStage() {
        MasterDataReferenceValidationPort port = mock(MasterDataReferenceValidationPort.class);
        when(port.airportExists("XXX")).thenReturn(false);

        MasterDataReferenceValidationSupport support = new MasterDataReferenceValidationSupport(port);
        ValidationResult result = new ValidationResult();

        support.validateAirport(result, "XXX", "REF_001", "TYPE_3", "QR1234");

        assertThat(result.getMessages()).singleElement().satisfies(message -> {
            assertThat(message.getRuleCode()).isEqualTo("REF_001");
            assertThat(message.getStage()).isEqualTo(ValidationStage.VALIDATION);
        });
    }
}
