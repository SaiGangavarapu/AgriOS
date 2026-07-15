package com.agrios.platform.compliance.api;

import com.agrios.platform.compliance.domain.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;

public final class ComplianceDtos {
    private ComplianceDtos() {}

    public record RequirementRequest(
            @NotBlank String requirementCode,
            @NotBlank String requirementTitle,
            String requirementDescription,
            @NotBlank String requirementCategory,
            String evidenceType,
            boolean mandatory,
            @NotBlank String severityIfFailed,
            Map<String,Object> control) {}

    public record StandardRequest(
            @NotBlank String standardCode,
            @NotBlank String standardName,
            @NotBlank String standardType,
            String issuingAuthority,
            String jurisdictionCode,
            String versionLabel,
            LocalDate effectiveFrom,
            LocalDate effectiveUntil,
            List<@Valid RequirementRequest> requirements) {}

    public record ProfileRequest(
            UUID farmerId,
            UUID farmId,
            @NotBlank String profileName,
            @NotBlank String profileType,
            String geographyCode) {}

    public record ObligationRequest(
            @NotNull UUID standardId,
            @NotNull LocalDate applicableFrom,
            LocalDate applicableUntil,
            LocalDate nextAssessmentDue) {}

    public record CertificationApplicationRequest(
            @NotNull UUID complianceProfileId,
            @NotNull UUID standardId,
            @NotBlank String applicationNumber,
            @NotNull LocalDate applicationDate,
            String certificationBody,
            Map<String,Object> requestedScope) {}

    public record InspectionRequest(
            @NotNull UUID complianceProfileId,
            UUID certificationApplicationId,
            @NotBlank String inspectionType,
            @NotNull Instant scheduledAt,
            String inspectorName,
            String inspectorReference,
            String notes) {}

    public record FindingRequest(
            UUID requirementId,
            @NotBlank String findingCode,
            @NotBlank String findingType,
            @NotBlank String description,
            Map<String,Object> evidence,
            boolean correctiveActionRequired,
            LocalDate dueDate) {}

    public record InspectionCompleteRequest(
            @NotBlank String overallResult,
            String notes) {}

    public record SchemeRequest(
            @NotBlank String schemeCode,
            @NotBlank String schemeName,
            @NotBlank String schemeType,
            @NotBlank String authorityName,
            Set<String> geographyCodes,
            Map<String,Object> eligibilityRules,
            Map<String,Object> benefitDefinition,
            String applicationUrl,
            LocalDate validFrom,
            LocalDate validUntil) {}

    public record SchemeAssessmentRequest(
            @NotNull UUID farmerId,
            @NotNull UUID schemeId,
            Map<String,Object> evidenceSnapshot) {}

    public record SchemeAssessmentResponse(
            UUID id, String eligibilityStatus,
            BigDecimal eligibilityScore,
            String reasonCodesJson,
            String missingInformationJson) {
        public static SchemeAssessmentResponse from(
                SchemeEligibilityAssessmentEntity value) {
            return new SchemeAssessmentResponse(
                    value.id(), value.eligibilityStatus(),
                    value.eligibilityScore(), value.reasonCodes(),
                    value.missingInformation());
        }
    }
}
