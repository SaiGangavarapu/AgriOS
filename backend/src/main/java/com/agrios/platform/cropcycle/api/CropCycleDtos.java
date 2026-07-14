package com.agrios.platform.cropcycle.api;

import com.agrios.platform.cropcycle.domain.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.UUID;

public final class CropCycleDtos {
    private CropCycleDtos() {}

    public record CreateRequest(@NotNull UUID cropPlanId) {}
    public record SowingRequest(@NotNull LocalDate sowingDate) {}
    public record StageRequest(
            @NotBlank String stageCode,
            @NotNull CropCycleStatus lifecycleStatus,
            @NotNull Instant observedAt,
            @NotBlank String sourceType,
            @DecimalMin("0.0") @DecimalMax("1.0") BigDecimal confidenceScore,
            String notes) {}
    public record LossRequest(
            @NotBlank String lossType,
            @NotNull @DecimalMin("0.0001") BigDecimal affectedAreaHectares,
            @DecimalMin("0.0") @DecimalMax("100.0") BigDecimal estimatedLossPercent,
            @NotBlank String causeCode,
            @NotNull Instant observedAt,
            String notes,
            boolean totalLoss) {}
    public record CloseRequest(
            BigDecimal harvestedQuantity,
            String harvestedUnit,
            BigDecimal marketableQuantity,
            BigDecimal retainedSeedQuantity,
            BigDecimal householdUseQuantity,
            String closingNotes) {}

    public record Response(
            UUID id, UUID cropPlanId, UUID fieldId, UUID cropId, UUID varietyId,
            String seasonCode, BigDecimal plannedAreaHectares,
            CropCycleStatus status, String currentStageCode,
            Instant activatedAt, LocalDate sowingDate,
            Instant closedAt, String closureNotes, long version) {
        public static Response from(CropCycleEntity value) {
            return new Response(value.id(), value.cropPlanId(), value.fieldId(),
                    value.cropId(), value.varietyId(), value.seasonCode(),
                    value.plannedAreaHectares(), value.status(),
                    value.currentStageCode(), value.activatedAt(),
                    value.sowingDate(), value.closedAt(),
                    value.closureNotes(), value.version());
        }
    }
}
