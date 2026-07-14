package com.agrios.platform.nutrient.api;

import com.agrios.platform.nutrient.domain.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;

public final class NutrientDtos {
    private NutrientDtos() {}

    public record PlanItemRequest(
            @NotBlank String nutrientCode,
            @NotBlank String sourceCategory,
            @NotBlank String sourceName,
            @NotNull @DecimalMin("0.0001") BigDecimal plannedQuantity,
            @NotBlank String quantityUnit,
            BigDecimal nutrientContentPercent,
            BigDecimal plannedNutrientQuantity,
            LocalDate plannedApplicationDate,
            String cropStageCode,
            @NotBlank String applicationMethod,
            Integer splitNo,
            Integer safetyIntervalDays,
            String notes) {}

    public record GeneratePlanRequest(
            @NotNull UUID cropCycleId,
            @NotNull FarmingApproach farmingApproach,
            BigDecimal targetYield,
            String targetYieldUnit,
            List<@Valid PlanItemRequest> items) {}

    public record ApproveRequest(String approvalNotes) {}

    public record ApplicationRequest(
            UUID nutrientPlanItemId,
            @NotNull Instant appliedAt,
            @NotBlank String sourceCategory,
            @NotBlank String sourceName,
            @NotNull @DecimalMin("0.0001") BigDecimal appliedQuantity,
            @NotBlank String quantityUnit,
            @NotBlank String applicationMethod,
            @DecimalMin("0.0001") BigDecimal areaHectares,
            @NotBlank String weatherCheckStatus,
            String notes) {}

    public record PlanResponse(
            UUID id, UUID cropCycleId, UUID soilProfileId,
            FarmingApproach farmingApproach, BigDecimal targetYield,
            String targetYieldUnit, String status,
            String recommendationBasisJson, String warningsJson,
            String approvalNotes, Instant approvedAt,
            UUID approvedBy, long version) {
        public static PlanResponse from(NutrientPlanEntity value) {
            return new PlanResponse(value.id(), value.cropCycleId(),
                    value.soilProfileId(), value.farmingApproach(),
                    value.targetYield(), value.targetYieldUnit(),
                    value.status(), value.recommendationBasis(),
                    value.warnings(), value.approvalNotes(),
                    value.approvedAt(), value.approvedBy(), value.version());
        }
    }

    public record BudgetResponse(
            String nutrientCode, BigDecimal plannedQuantity,
            BigDecimal appliedQuantity, BigDecimal estimatedCropUptake,
            BigDecimal estimatedBalance, String quantityUnit) {
        public static BudgetResponse from(NutrientBudgetEntity value) {
            return new BudgetResponse(value.nutrientCode(),
                    value.plannedQuantity(), value.appliedQuantity(),
                    value.estimatedCropUptake(), value.estimatedBalance(),
                    value.quantityUnit());
        }
    }
}
