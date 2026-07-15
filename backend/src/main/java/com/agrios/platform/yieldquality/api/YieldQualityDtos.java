package com.agrios.platform.yieldquality.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

public final class YieldQualityDtos {
    private YieldQualityDtos() {}

    public record YieldRequest(
            @NotNull UUID harvestBatchId,
            @NotNull @DecimalMin("0.0001") BigDecimal fieldAreaHectares,
            BigDecimal expectedYieldPerHectare,
            BigDecimal marketableQuantity,
            BigDecimal rejectedQuantity) {}

    public record ParameterRequest(
            @NotNull UUID cropId,
            @NotBlank String parameterCode,
            @NotBlank String parameterName,
            @NotBlank String valueType,
            String unitCode,
            BigDecimal minimumValue,
            BigDecimal maximumValue,
            boolean required) {}

    public record ResultRequest(
            @NotNull UUID qualityParameterId,
            BigDecimal numericValue,
            String textValue,
            Boolean booleanValue,
            String notes) {
        @AssertTrue(message = "A quality value is required")
        public boolean hasValue() {
            return numericValue != null || textValue != null || booleanValue != null;
        }
    }

    public record AssessmentRequest(
            @NotNull UUID harvestBatchId,
            @NotNull Instant assessedAt,
            @NotBlank String assessorType,
            UUID assessorId,
            String notes,
            @NotEmpty List<@Valid ResultRequest> results) {}

    public record CompleteAssessmentRequest(
            @NotBlank String assignedGrade,
            @NotBlank String marketabilityStatus) {}
}
