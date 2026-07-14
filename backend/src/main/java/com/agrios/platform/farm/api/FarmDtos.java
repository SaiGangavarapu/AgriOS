package com.agrios.platform.farm.api;

import com.agrios.platform.farm.domain.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public final class FarmDtos {
    private FarmDtos() {}

    public record RegisterFarmRequest(
            UUID programmeId,
            @NotBlank @Size(max = 200) String name,
            @NotNull UUID primaryOperatorFarmerId,
            @NotBlank @Size(max = 60) String farmType,
            @NotBlank @Size(max = 160) String villageName,
            @Size(max = 160) String districtName,
            @Size(max = 160) String stateName) {}

    public record FarmResponse(
            UUID id, UUID tenantId, UUID programmeId, String name,
            UUID primaryOperatorFarmerId, String farmType,
            String villageName, String districtName, String stateName,
            FarmStatus status, long version, Instant createdAt, Instant updatedAt) {
        public static FarmResponse from(FarmEntity value) {
            return new FarmResponse(value.id(), value.tenantId(), value.programmeId(),
                    value.name(), value.primaryOperatorFarmerId(), value.farmType(),
                    value.villageName(), value.districtName(), value.stateName(),
                    value.status(), value.version(), value.createdAt(), value.updatedAt());
        }
    }

    public record RegisterFieldRequest(@NotBlank @Size(max = 200) String name) {}

    public record BoundaryRequest(
            @NotBlank String geoJson,
            @NotBlank String captureMethod,
            @PositiveOrZero BigDecimal accuracyMeters) {}

    public record FieldResponse(
            UUID id, UUID tenantId, UUID farmId, String name,
            Integer currentBoundaryVersionNo, BigDecimal areaHectares,
            FieldStatus status, long version, Instant createdAt, Instant updatedAt) {
        public static FieldResponse from(FieldEntity value) {
            return new FieldResponse(value.id(), value.tenantId(), value.farmId(),
                    value.name(), value.currentBoundaryVersionNo(),
                    value.areaHectares(), value.status(), value.version(),
                    value.createdAt(), value.updatedAt());
        }
    }

    public record BoundaryResponse(
            UUID id, UUID fieldId, int versionNo,
            BigDecimal calculatedAreaHectares,
            String validationStatus, boolean current, Instant capturedAt) {
        public static BoundaryResponse from(FieldBoundaryEntity value) {
            return new BoundaryResponse(value.id(), value.fieldId(), value.versionNo(),
                    value.calculatedAreaHectares(), value.validationStatus(),
                    value.current(), value.capturedAt());
        }
    }

    public record WaterSourceRequest(
            @NotBlank String sourceType,
            @NotBlank @Size(max = 200) String name,
            @NotBlank String reliability) {}

    public record WaterSourceResponse(
            UUID id, UUID farmId, String sourceType, String name,
            String reliability, String status, long version) {
        public static WaterSourceResponse from(WaterSourceEntity value) {
            return new WaterSourceResponse(value.id(), value.farmId(),
                    value.sourceType(), value.name(), value.reliability(),
                    value.status(), value.version());
        }
    }
}
