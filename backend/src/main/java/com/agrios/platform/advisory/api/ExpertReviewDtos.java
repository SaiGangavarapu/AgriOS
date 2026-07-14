package com.agrios.platform.advisory.api;

import com.agrios.platform.advisory.domain.*;
import jakarta.validation.constraints.*;
import java.time.Instant;
import java.util.*;

public final class ExpertReviewDtos {
    private ExpertReviewDtos() {}

    public record ExpertProfileRequest(
            UUID userAccountId,
            @NotBlank String displayName,
            @NotBlank String expertType,
            String organizationName,
            String qualificationSummary,
            String registrationReference,
            Set<String> geographyCodes,
            Set<String> cropCodes,
            Set<String> languageCodes) {}

    public record CreateCaseRequest(
            @NotBlank String caseType,
            @NotBlank String subjectType,
            @NotNull UUID subjectId,
            UUID farmerId,
            UUID fieldId,
            UUID cropCycleId,
            @NotBlank String priority,
            Instant dueAt,
            @NotBlank String question,
            Map<String,Object> contextSnapshot) {}

    public record AssignRequest(@NotNull UUID expertId) {}
    public record NoteRequest(
            @NotNull UUID expertId,
            @NotBlank String noteType,
            @NotBlank String noteText,
            List<String> evidenceReferences) {}
    public record DecisionRequest(
            @NotNull UUID expertId,
            @NotBlank String decisionCode,
            @NotBlank String decisionSummary,
            String recommendationText,
            String riskLevel,
            boolean followUpRequired,
            Instant followUpDueAt,
            UUID advisoryId) {}

    public record CaseResponse(
            UUID id, UUID farmerId, UUID fieldId, UUID cropCycleId,
            String caseType, String subjectType, UUID subjectId,
            String priority, String status, String question,
            UUID assignedExpertId, long version) {
        public static CaseResponse from(ExpertReviewCaseEntity value) {
            return new CaseResponse(value.id(), value.farmerId(),
                    value.fieldId(), value.cropCycleId(), value.caseType(),
                    value.subjectType(), value.subjectId(), value.priority(),
                    value.status(), value.question(),
                    value.assignedExpertId(), value.version());
        }
    }
}
