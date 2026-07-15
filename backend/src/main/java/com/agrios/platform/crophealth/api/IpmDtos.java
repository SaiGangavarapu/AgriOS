package com.agrios.platform.crophealth.api;

import com.agrios.platform.crophealth.domain.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;

public final class IpmDtos {
    private IpmDtos() {}

    public record ActionRequest(
            @Min(1) int actionSequence,
            @NotBlank String actionCategory,
            @NotBlank String actionName,
            String actionDescription,
            String triggerCondition,
            String cropStageCode,
            LocalDate plannedDate,
            String productName,
            BigDecimal dosage,
            String dosageUnit,
            String applicationMethod,
            Integer preHarvestIntervalDays,
            Integer reentryIntervalHours,
            boolean pollinatorWarning,
            BigDecimal waterBodyBufferMeters) {}

    public record GenerateRequest(
            @NotNull UUID cropCycleId,
            @NotNull UUID fieldId,
            UUID pestId,
            UUID diseaseId,
            @NotBlank String planType,
            @NotBlank String strategySummary,
            List<String> preventionMeasures,
            List<String> monitoringMeasures,
            List<String> nonChemicalMeasures,
            boolean chemicalLastResort,
            List<String> warnings,
            List<@Valid ActionRequest> actions) {}

    public record ApproveRequest(String approvalNotes) {}

    public record ExecuteRequest(
            @NotNull UUID ipmActionId,
            @NotNull Instant executedAt,
            BigDecimal executedQuantity,
            String quantityUnit,
            BigDecimal coveredAreaHectares,
            @NotBlank String weatherCheckStatus,
            String observedResult,
            String notes) {}

    public record PlanResponse(
            UUID id, UUID cropCycleId, UUID fieldId,
            UUID pestId, UUID diseaseId, String planType,
            String status, String strategySummary,
            String preventionMeasuresJson, String monitoringMeasuresJson,
            String nonChemicalMeasuresJson, String warningsJson,
            long version) {
        public static PlanResponse from(IpmPlanEntity value) {
            return new PlanResponse(value.id(), value.cropCycleId(),
                    value.fieldId(), value.pestId(), value.diseaseId(),
                    value.planType(), value.status(), value.strategySummary(),
                    value.preventionMeasures(), value.monitoringMeasures(),
                    value.nonChemicalMeasures(), value.warnings(), value.version());
        }
    }
}
