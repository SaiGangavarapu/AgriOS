package com.agrios.platform.crophealth.api;

import com.agrios.platform.crophealth.domain.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;

public final class CropHealthDtos {
    private CropHealthDtos() {}

    public record CatalogRequest(
            @NotBlank String code,
            @NotBlank String commonName,
            String scientificName,
            @NotBlank String category,
            String causalAgent,
            Set<String> affectedCropCodes,
            List<String> symptoms,
            List<String> favorableConditions,
            String notes,
            @NotBlank String evidenceGrade) {}

    public record ObservationRequest(
            @NotBlank String observationType,
            UUID pestId,
            UUID diseaseId,
            String observedSymptom,
            @PositiveOrZero Integer affectedPlantCount,
            @DecimalMin("0.0") @DecimalMax("100.0") BigDecimal incidencePercent,
            @DecimalMin("0.0") @DecimalMax("100.0") BigDecimal severityPercent,
            BigDecimal populationCount,
            String populationUnit,
            String cropStageCode,
            @DecimalMin("0.0") @DecimalMax("1.0") BigDecimal confidenceScore,
            Map<String,Object> evidence) {}

    public record ScoutingRequest(
            @NotNull UUID fieldId,
            @NotNull UUID cropCycleId,
            @NotNull Instant scoutedAt,
            @NotBlank String scoutType,
            UUID scoutId,
            @NotBlank String samplingMethod,
            @DecimalMin("0.0001") BigDecimal sampleAreaHectares,
            @Positive Integer plantCount,
            String notes,
            Map<String,Object> weatherSnapshot,
            List<@Valid ObservationRequest> observations) {}

    public record HealthAssessmentRequest(
            @NotNull UUID fieldId,
            @NotNull UUID cropCycleId,
            @NotNull LocalDate assessmentDate) {}

    public record OutbreakRequest(
            @NotNull UUID fieldId,
            @NotNull UUID cropCycleId,
            UUID pestId,
            UUID diseaseId,
            @NotBlank String outbreakType,
            @NotNull Instant detectedAt,
            @NotBlank String severity,
            BigDecimal affectedAreaHectares,
            BigDecimal incidencePercent,
            UUID sourceObservationId) {}

    public record HealthAssessmentResponse(
            UUID id, String healthStatus, BigDecimal compositeScore,
            BigDecimal confidenceScore, String reasonCodesJson) {
        public static HealthAssessmentResponse from(CropHealthAssessmentEntity value) {
            return new HealthAssessmentResponse(value.id(), value.healthStatus(),
                    value.compositeScore(), value.confidenceScore(), value.reasonCodes());
        }
    }
}
