package com.agrios.platform.irrigation.api;

import com.agrios.platform.irrigation.domain.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public final class IrrigationDtos {
    private IrrigationDtos() {}

    public record GeneratePlanRequest(
            @NotNull UUID cropCycleId,
            UUID waterSourceId,
            @NotBlank String irrigationMethod,
            @DecimalMin("0.0001") BigDecimal seasonalWaterRequirementMm,
            @DecimalMin("0.0001") BigDecimal plannedWaterVolumeM3,
            @DecimalMin("0.0") @DecimalMax("100.0") BigDecimal efficiencyPercent) {}

    public record ScheduleRequest(
            @NotNull Instant scheduledAt,
            @NotNull @DecimalMin("0.0001") BigDecimal targetDepthMm,
            BigDecimal plannedVolumeM3,
            String cropStageCode,
            @NotBlank String triggerType,
            BigDecimal triggerThreshold) {}

    public record DeferRequest(@NotNull Instant newScheduledAt) {}
    public record SkipRequest(@NotBlank String reason) {}

    public record ExecutionRequest(
            @NotNull Instant startedAt,
            @DecimalMin("0.0") BigDecimal actualDepthMm,
            @DecimalMin("0.0") BigDecimal actualVolumeM3,
            @PositiveOrZero Integer pumpRuntimeMinutes,
            @DecimalMin("0.0") BigDecimal energyConsumedKwh,
            @NotBlank String executionStatus,
            String notes) {}

    public record PlanResponse(
            UUID id, UUID cropCycleId, UUID waterSourceId,
            UUID waterProfileId, String irrigationMethod,
            String status, BigDecimal seasonalWaterRequirementMm,
            BigDecimal plannedWaterVolumeM3,
            BigDecimal efficiencyPercent, String recommendationBasisJson,
            String warningsJson, long version) {
        public static PlanResponse from(IrrigationPlanEntity value) {
            return new PlanResponse(value.id(), value.cropCycleId(),
                    value.waterSourceId(), value.waterProfileId(),
                    value.irrigationMethod(), value.status(),
                    value.seasonalWaterRequirementMm(),
                    value.plannedWaterVolumeM3(),
                    value.efficiencyPercent(),
                    value.recommendationBasis(), value.warnings(),
                    value.version());
        }
    }

    public record ScheduleResponse(
            UUID id, UUID irrigationPlanId, UUID cropCycleId,
            Instant scheduledAt, BigDecimal targetDepthMm,
            BigDecimal plannedVolumeM3, String cropStageCode,
            String triggerType, BigDecimal triggerThreshold,
            String status, String skipReason, long version) {
        public static ScheduleResponse from(IrrigationScheduleEntity value) {
            return new ScheduleResponse(value.id(), value.irrigationPlanId(),
                    value.cropCycleId(), value.scheduledAt(),
                    value.targetDepthMm(), value.plannedVolumeM3(),
                    value.cropStageCode(), value.triggerType(),
                    value.triggerThreshold(), value.status(),
                    value.skipReason(), value.version());
        }
    }

    public record AccountingResponse(
            BigDecimal rainfallEffectiveMm,
            BigDecimal irrigationAppliedMm,
            BigDecimal irrigationVolumeM3,
            BigDecimal estimatedCropDemandMm,
            BigDecimal estimatedDeficitMm) {
        public static AccountingResponse from(WaterAccountingEntity value) {
            return new AccountingResponse(value.rainfallEffectiveMm(),
                    value.irrigationAppliedMm(), value.irrigationVolumeM3(),
                    value.estimatedCropDemandMm(), value.estimatedDeficitMm());
        }
    }
}
