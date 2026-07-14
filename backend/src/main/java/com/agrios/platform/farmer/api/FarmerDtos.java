package com.agrios.platform.farmer.api;

import com.agrios.platform.farmer.domain.*;
import jakarta.validation.constraints.*;
import java.time.Instant;
import java.util.UUID;

public final class FarmerDtos {
    private FarmerDtos() {}

    public record RegisterRequest(
            UUID programmeId,
            @NotBlank @Size(max = 200) String fullName,
            @Size(max = 120) String preferredName,
            @NotBlank @Size(min = 2, max = 10) String preferredLanguage,
            @Pattern(regexp = "^\\+?[1-9][0-9]{7,14}$") String mobileE164,
            @NotBlank @Size(max = 160) String villageName,
            @Size(max = 160) String districtName,
            @Size(max = 160) String stateName,
            @NotBlank @Size(max = 40) String sourceChannel) {}

    public record UpdateRequest(
            @NotBlank @Size(max = 200) String fullName,
            @Size(max = 120) String preferredName,
            @NotBlank @Size(min = 2, max = 10) String preferredLanguage,
            @Pattern(regexp = "^\\+?[1-9][0-9]{7,14}$") String mobileE164,
            @NotBlank @Size(max = 160) String villageName,
            @Size(max = 160) String districtName,
            @Size(max = 160) String stateName,
            @PositiveOrZero long version) {}

    public record VerifyRequest(
            @NotNull VerificationLevel verificationLevel,
            @NotBlank String evidenceType,
            String evidenceReference,
            Instant expiresAt) {}

    public record MergeRequest(
            @NotNull UUID duplicateFarmerId,
            @NotNull UUID canonicalFarmerId) {}

    public record Response(
            UUID id,
            UUID tenantId,
            UUID programmeId,
            String fullName,
            String preferredName,
            String preferredLanguage,
            String mobileE164,
            String villageName,
            String districtName,
            String stateName,
            VerificationLevel verificationLevel,
            FarmerStatus status,
            UUID canonicalFarmerId,
            String sourceChannel,
            long version,
            Instant createdAt,
            Instant updatedAt) {
        public static Response from(FarmerEntity value) {
            return new Response(value.id(), value.tenantId(), value.programmeId(),
                    value.fullName(), value.preferredName(), value.preferredLanguage(),
                    value.mobileE164(), value.villageName(), value.districtName(),
                    value.stateName(), value.verificationLevel(), value.status(),
                    value.canonicalFarmerId(), value.sourceChannel(), value.version(),
                    value.createdAt(), value.updatedAt());
        }
    }
}
