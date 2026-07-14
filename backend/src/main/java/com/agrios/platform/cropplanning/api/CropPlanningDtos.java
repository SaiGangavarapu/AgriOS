package com.agrios.platform.cropplanning.api;

import com.agrios.platform.cropplanning.domain.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

public final class CropPlanningDtos {
    private CropPlanningDtos() {}

    public record AssessmentRequest(
            @NotNull UUID fieldId,
            @NotBlank String seasonCode,
            @NotBlank String farmingSystem,
            Set<UUID> cropIds,
            Map<String, Object> assumptions) {}

    public record CandidateResponse(
            UUID id, UUID cropId, UUID varietyId,
            BigDecimal suitabilityScore, BigDecimal confidenceScore,
            boolean hardConstraintFailed, String hardConstraintCodesJson,
            String reasonCodesJson, int rankNo, Integer estimatedDurationDays) {
        public static CandidateResponse from(SuitabilityCandidateEntity value) {
            return new CandidateResponse(value.id(), value.cropId(), value.varietyId(),
                    value.suitabilityScore(), value.confidenceScore(),
                    value.hardConstraintFailed(), value.hardConstraintCodes(),
                    value.reasonCodes(), value.rankNo(), value.estimatedDurationDays());
        }
    }

    public record AssessmentResponse(
            UUID id, UUID fieldId, String seasonCode, String farmingSystem,
            UUID soilProfileId, UUID waterProfileId,
            String assessmentStatus, Instant assessedAt,
            String assumptionsJson, List<CandidateResponse> candidates) {
        public static AssessmentResponse from(SuitabilityAssessmentEntity value) {
            return new AssessmentResponse(value.id(), value.fieldId(),
                    value.seasonCode(), value.farmingSystem(),
                    value.soilProfileId(), value.waterProfileId(),
                    value.assessmentStatus(), value.assessedAt(),
                    value.assumptionsJson(),
                    value.candidates().stream().map(CandidateResponse::from).toList());
        }
    }

    public record CreatePlanRequest(
            @NotNull UUID fieldId,
            @NotNull UUID assessmentId,
            @NotBlank String seasonCode,
            @NotBlank String farmingSystem,
            @NotNull UUID selectedCropId,
            UUID selectedVarietyId,
            @NotNull @DecimalMin("0.0001") BigDecimal plannedAreaHectares) {}

    public record ApprovePlanRequest(String approvalNotes) {}

    public record PlanResponse(
            UUID id, UUID fieldId, UUID assessmentId,
            String seasonCode, String farmingSystem,
            UUID selectedCropId, UUID selectedVarietyId,
            BigDecimal plannedAreaHectares, String status,
            String approvalNotes, Instant approvedAt,
            UUID approvedBy, long version) {
        public static PlanResponse from(CropPlanEntity value) {
            return new PlanResponse(value.id(), value.fieldId(),
                    value.assessmentId(), value.seasonCode(),
                    value.farmingSystem(), value.selectedCropId(),
                    value.selectedVarietyId(), value.plannedAreaHectares(),
                    value.status(), value.approvalNotes(), value.approvedAt(),
                    value.approvedBy(), value.version());
        }
    }
}
