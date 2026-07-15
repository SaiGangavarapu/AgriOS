package com.agrios.platform.finance.api;

import com.agrios.platform.finance.domain.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

public final class FinanceDtos {
    private FinanceDtos() {}

    public record FinancialEventRequest(
            UUID farmerId,
            @NotBlank String eventType,
            @NotBlank String sourceModule,
            @NotBlank String sourceReferenceType,
            UUID sourceReferenceId,
            @NotNull LocalDate eventDate,
            @NotBlank String currencyCode,
            @NotNull @DecimalMin("0.0") BigDecimal amount,
            @NotBlank String direction,
            @NotBlank String description,
            UUID fieldId,
            UUID cropCycleId,
            String costCenterCode,
            @NotBlank String idempotencyKey,
            Map<String,Object> payload) {}

    public record CostCenterRequest(
            @NotBlank String code,
            @NotBlank String name,
            @NotBlank String category) {}

    public record BudgetLineRequest(
            UUID costCenterId,
            String incomeCategory,
            @NotNull @DecimalMin("0.0") BigDecimal plannedAmount,
            String notes) {}

    public record BudgetRequest(
            @NotNull UUID farmerId,
            UUID cropCycleId,
            UUID fieldId,
            @NotBlank String budgetName,
            @NotNull LocalDate periodStart,
            @NotNull LocalDate periodEnd,
            @NotBlank String currencyCode,
            List<@Valid BudgetLineRequest> lines) {}

    public record ProfitabilityRequest(
            @NotNull UUID farmerId,
            UUID fieldId,
            UUID cropCycleId,
            @NotNull LocalDate periodStart,
            @NotNull LocalDate periodEnd,
            BigDecimal allocatedOverhead,
            BigDecimal depreciation,
            BigDecimal financeCost,
            BigDecimal productionQuantity,
            String productionUnit) {}

    public record FinancialEventResponse(
            UUID id, UUID farmerId, String eventType,
            String sourceModule, LocalDate eventDate,
            BigDecimal amount, String direction,
            UUID fieldId, UUID cropCycleId,
            String costCenterCode, String status) {
        public static FinancialEventResponse from(FinancialEventEntity value) {
            return new FinancialEventResponse(
                    value.id(), value.farmerId(), value.eventType(),
                    value.sourceModule(), value.eventDate(), value.amount(),
                    value.direction(), value.fieldId(), value.cropCycleId(),
                    value.costCenterCode(), value.status());
        }
    }

    public record ProfitabilityResponse(
            UUID id, BigDecimal revenue, BigDecimal operatingCost,
            BigDecimal netProfit, BigDecimal roiPercent,
            BigDecimal costPerUnit) {
        public static ProfitabilityResponse from(ProfitabilitySnapshotEntity value) {
            return new ProfitabilityResponse(
                    value.id(), value.revenue(), value.operatingCost(),
                    value.netProfit(), value.roiPercent(), value.costPerUnit());
        }
    }
}
