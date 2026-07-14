package com.agrios.platform.soilwater.api;

import com.agrios.platform.soilwater.domain.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;

public final class SoilWaterDtos {
    private SoilWaterDtos() {}

    public record LaboratoryRequest(
            @NotBlank @Size(max = 80) String code,
            @NotBlank @Size(max = 200) String name,
            @NotBlank String laboratoryType,
            String accreditationReference) {}

    public record LaboratoryResponse(
            UUID id, String code, String name, String laboratoryType,
            String accreditationReference, String status, long version) {
        public static LaboratoryResponse from(LaboratoryEntity value) {
            return new LaboratoryResponse(value.id(), value.code(), value.name(),
                    value.laboratoryType(), value.accreditationReference(),
                    value.status(), value.version());
        }
    }

    public record SoilSampleRequest(
            @NotNull UUID fieldId,
            @NotBlank String sampleCode,
            @NotNull Instant collectedAt,
            @PositiveOrZero BigDecimal collectionDepthCm,
            @NotBlank String collectionMethod,
            String recentInputNotes) {}

    public record WaterSampleRequest(
            @NotNull UUID waterSourceId,
            @NotBlank String sampleCode,
            @NotNull Instant collectedAt,
            @NotBlank String collectionMethod,
            String recentInputNotes) {}

    public record SampleResponse(
            UUID id, SampleType sampleType, UUID fieldId, UUID waterSourceId,
            String sampleCode, Instant collectedAt, BigDecimal collectionDepthCm,
            String collectionMethod, String recentInputNotes,
            String status, long version) {
        public static SampleResponse from(SampleEntity value) {
            return new SampleResponse(value.id(), value.sampleType(),
                    value.fieldId(), value.waterSourceId(), value.sampleCode(),
                    value.collectedAt(), value.collectionDepthCm(),
                    value.collectionMethod(), value.recentInputNotes(),
                    value.status(), value.version());
        }
    }

    public record ResultRequest(
            @NotBlank String parameterCode,
            @NotNull BigDecimal value,
            @NotBlank String unitCode,
            @NotBlank String analyticalMethod,
            String qualityFlag,
            BigDecimal referenceMin,
            BigDecimal referenceMax) {}

    public record TestReportRequest(
            @NotNull UUID sampleId,
            @NotNull UUID laboratoryId,
            @NotBlank String reportNumber,
            @NotNull Instant testedAt,
            String interpretationRuleVersion,
            LocalDate validUntil,
            String notes,
            @NotEmpty List<@Valid ResultRequest> results) {}

    public record ResultResponse(
            UUID id, String parameterCode, BigDecimal value,
            String unitCode, String analyticalMethod, String qualityFlag,
            BigDecimal referenceMin, BigDecimal referenceMax) {
        public static ResultResponse from(TestResultEntity value) {
            return new ResultResponse(value.id(), value.parameterCode(),
                    value.value(), value.unitCode(), value.analyticalMethod(),
                    value.qualityFlag(), value.referenceMin(), value.referenceMax());
        }
    }

    public record TestReportResponse(
            UUID id, UUID sampleId, UUID laboratoryId, String reportNumber,
            Instant testedAt, Instant publishedAt, String qualityStatus,
            String interpretationRuleVersion, LocalDate validUntil,
            String notes, long version, List<ResultResponse> results) {
        public static TestReportResponse from(TestReportEntity value) {
            return new TestReportResponse(value.id(), value.sampleId(),
                    value.laboratoryId(), value.reportNumber(), value.testedAt(),
                    value.publishedAt(), value.qualityStatus(),
                    value.interpretationRuleVersion(), value.validUntil(),
                    value.notes(), value.version(),
                    value.results().stream().map(ResultResponse::from).toList());
        }
    }

    public record ProfileResponse(
            UUID id, String profileType, UUID fieldId, UUID waterSourceId,
            UUID sourceReportId, String summaryJson, String constraintCodesJson,
            Instant effectiveFrom, LocalDate validUntil, boolean current) {
        public static ProfileResponse from(ProfileEntity value) {
            return new ProfileResponse(value.id(), value.profileType(),
                    value.fieldId(), value.waterSourceId(), value.sourceReportId(),
                    value.summaryJson(), value.constraintCodes(),
                    value.effectiveFrom(), value.validUntil(), value.current());
        }
    }
}
