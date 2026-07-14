package com.agrios.platform.knowledge.api;

import com.agrios.platform.knowledge.domain.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.*;

public final class KnowledgeDtos {
    private KnowledgeDtos() {}

    public record CropRequest(
            @NotBlank String code,
            @NotBlank String defaultName,
            String scientificName,
            @NotBlank String cropCategory,
            @Positive Integer durationMinDays,
            @Positive Integer durationMaxDays,
            @NotBlank String evidenceGrade) {
        @AssertTrue(message = "durationMaxDays must be at least durationMinDays")
        public boolean validDuration() {
            return durationMinDays == null || durationMaxDays == null ||
                    durationMaxDays >= durationMinDays;
        }
    }

    public record CropResponse(
            UUID id, String code, String defaultName, String scientificName,
            String cropCategory, Integer durationMinDays, Integer durationMaxDays,
            KnowledgeStatus status, String evidenceGrade, long version) {
        public static CropResponse from(CropEntity value) {
            return new CropResponse(value.id(), value.code(), value.defaultName(),
                    value.scientificName(), value.cropCategory(),
                    value.durationMinDays(), value.durationMaxDays(),
                    value.status(), value.evidenceGrade(), value.version());
        }
    }

    public record VarietyRequest(
            @NotNull UUID cropId,
            @NotBlank String code,
            @NotBlank String defaultName,
            @NotBlank String releaseStatus,
            @Positive Integer durationDays,
            Set<String> seasonCodes,
            Set<String> geographyCodes,
            Set<String> toleranceTraits,
            Set<String> resistanceTraits,
            Map<String, Object> marketTraits) {}

    public record VarietyResponse(
            UUID id, UUID cropId, String code, String defaultName,
            String releaseStatus, Integer durationDays,
            String seasonCodesJson, String geographyCodesJson,
            String toleranceTraitsJson, String resistanceTraitsJson,
            String marketTraitsJson, KnowledgeStatus status, long version) {
        public static VarietyResponse from(VarietyEntity value) {
            return new VarietyResponse(value.id(), value.cropId(), value.code(),
                    value.defaultName(), value.releaseStatus(), value.durationDays(),
                    value.seasonCodes(), value.geographyCodes(),
                    value.toleranceTraits(), value.resistanceTraits(),
                    value.marketTraits(), value.status(), value.version());
        }
    }

    public record RequirementRequest(
            @NotBlank String requirementType,
            BigDecimal minimumValue,
            BigDecimal maximumValue,
            String unitCode,
            boolean hardConstraint,
            Map<String, Object> applicability,
            String evidenceReference) {}

    public record RequirementResponse(
            UUID id, UUID cropId, String requirementType,
            BigDecimal minimumValue, BigDecimal maximumValue,
            String unitCode, boolean hardConstraint,
            String applicabilityJson, String evidenceReference) {
        public static RequirementResponse from(CropRequirementEntity value) {
            return new RequirementResponse(value.id(), value.cropId(),
                    value.requirementType(), value.minimumValue(),
                    value.maximumValue(), value.unitCode(),
                    value.hardConstraint(), value.applicabilityJson(),
                    value.evidenceReference());
        }
    }
}
