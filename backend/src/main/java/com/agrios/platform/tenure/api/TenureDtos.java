package com.agrios.platform.tenure.api;

import com.agrios.platform.tenure.domain.TenureEntity;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.UUID;

public final class TenureDtos {
    private TenureDtos() {}

    public record CreateRequest(
            @NotBlank String tenureType,
            @NotNull UUID cultivatorFarmerId,
            @NotNull LocalDate validFrom,
            LocalDate validTo,
            String evidenceReference) {
        @AssertTrue(message = "validTo must not be before validFrom")
        public boolean validDates() {
            return validTo == null || !validTo.isBefore(validFrom);
        }
    }

    public record EndRequest(@NotNull LocalDate endDate) {}

    public record Response(
            UUID id, UUID fieldId, String tenureType, UUID cultivatorFarmerId,
            LocalDate validFrom, LocalDate validTo, String status,
            String evidenceReference, long version) {
        public static Response from(TenureEntity value) {
            return new Response(value.id(), value.fieldId(), value.tenureType(),
                    value.cultivatorFarmerId(), value.validFrom(), value.validTo(),
                    value.status(), value.evidenceReference(), value.version());
        }
    }
}
