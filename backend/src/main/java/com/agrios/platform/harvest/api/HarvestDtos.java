package com.agrios.platform.harvest.api;

import com.agrios.platform.harvest.domain.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.UUID;

public final class HarvestDtos {
    private HarvestDtos() {}

    public record PlanRequest(
            @NotNull UUID cropCycleId,
            @NotNull UUID fieldId,
            @NotNull LocalDate expectedStartDate,
            @NotNull LocalDate expectedEndDate,
            @NotBlank String harvestMethod,
            BigDecimal expectedYieldQuantity,
            String expectedYieldUnit,
            @DecimalMin("0.0") @DecimalMax("1.0") BigDecimal readinessScore,
            @NotBlank String weatherSuitability,
            String notes) {}

    public record BatchRequest(
            @NotBlank String batchCode,
            @NotNull Instant harvestedAt,
            @NotBlank String harvestMethod,
            @NotNull @DecimalMin("0.0001") BigDecimal grossQuantity,
            @DecimalMin("0.0") BigDecimal tareQuantity,
            @NotBlank String quantityUnit,
            @DecimalMin("0.0") @DecimalMax("100.0") BigDecimal moisturePercent,
            @DecimalMin("0.0") BigDecimal damagedQuantity,
            @DecimalMin("0.0") BigDecimal fieldLossQuantity,
            @DecimalMin("0.0") BigDecimal transportLossQuantity,
            String notes) {}

    public record PlanResponse(
            UUID id, UUID cropCycleId, UUID fieldId,
            LocalDate expectedStartDate, LocalDate expectedEndDate,
            String harvestMethod, BigDecimal expectedYieldQuantity,
            String expectedYieldUnit, BigDecimal readinessScore,
            String weatherSuitability, String status, long version) {
        public static PlanResponse from(HarvestPlanEntity value) {
            return new PlanResponse(value.id(), value.cropCycleId(),
                    value.fieldId(), value.expectedStartDate(),
                    value.expectedEndDate(), value.harvestMethod(),
                    value.expectedYieldQuantity(), value.expectedYieldUnit(),
                    value.readinessScore(), value.weatherSuitability(),
                    value.status(), value.version());
        }
    }

    public record BatchResponse(
            UUID id, UUID harvestPlanId, UUID cropCycleId,
            UUID fieldId, String batchCode, Instant harvestedAt,
            BigDecimal grossQuantity, BigDecimal tareQuantity,
            BigDecimal netQuantity, String quantityUnit,
            BigDecimal moisturePercent, BigDecimal damagedQuantity,
            BigDecimal fieldLossQuantity, BigDecimal transportLossQuantity,
            String status, long version) {
        public static BatchResponse from(HarvestBatchEntity value) {
            return new BatchResponse(value.id(), value.harvestPlanId(),
                    value.cropCycleId(), value.fieldId(), value.batchCode(),
                    value.harvestedAt(), value.grossQuantity(),
                    value.tareQuantity(), value.netQuantity(),
                    value.quantityUnit(), value.moisturePercent(),
                    value.damagedQuantity(), value.fieldLossQuantity(),
                    value.transportLossQuantity(), value.status(), value.version());
        }
    }
}
