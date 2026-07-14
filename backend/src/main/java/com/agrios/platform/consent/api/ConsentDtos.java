package com.agrios.platform.consent.api;

import com.agrios.platform.consent.domain.ConsentEntity;
import jakarta.validation.constraints.*;
import java.time.Instant;
import java.util.*;

public final class ConsentDtos {
    private ConsentDtos() {}

    public record GrantRequest(
            @NotNull UUID farmerId,
            @NotBlank String purposeCode,
            @NotBlank String recipientType,
            @NotBlank String recipientId,
            @NotEmpty Set<String> dataCategories,
            @NotNull Map<String, Object> scope,
            @NotBlank String policyVersion,
            @NotBlank String language,
            @NotNull Instant validFrom,
            Instant validUntil) {
        @AssertTrue(message = "validUntil must be after validFrom")
        public boolean validDates() {
            return validUntil == null || validUntil.isAfter(validFrom);
        }
    }

    public record RevokeRequest(@NotBlank String reason) {}

    public record CheckRequest(
            @NotNull UUID farmerId,
            @NotBlank String purposeCode,
            @NotBlank String recipientType,
            @NotBlank String recipientId) {}

    public record CheckResponse(boolean authorized, UUID consentId, String reason) {}

    public record Response(
            UUID id, UUID farmerId, String purposeCode,
            String recipientType, String recipientId,
            String dataCategoriesJson, String scopeJson,
            String policyVersion, String language,
            Instant validFrom, Instant validUntil,
            String status, Instant grantedAt,
            Instant revokedAt, String revokedReason,
            long version) {
        public static Response from(ConsentEntity value) {
            return new Response(value.id(), value.farmerId(), value.purposeCode(),
                    value.recipientType(), value.recipientId(),
                    value.dataCategories(), value.scopeJson(),
                    value.policyVersion(), value.language(),
                    value.validFrom(), value.validUntil(), value.status(),
                    value.grantedAt(), value.revokedAt(),
                    value.revokedReason(), value.version());
        }
    }
}
