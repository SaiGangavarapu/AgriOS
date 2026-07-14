package com.agrios.platform.advisory.api;

import com.agrios.platform.advisory.domain.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

public final class AdvisoryDtos {
    private AdvisoryDtos() {}

    public record ActionRequest(
            @NotBlank String actionCode,
            @NotBlank String actionLabel,
            String actionDescription,
            Instant dueAt,
            @NotBlank String actionPriority,
            String taskTemplateType) {}

    public record CreateRequest(
            UUID farmerId,
            UUID farmId,
            UUID fieldId,
            UUID cropCycleId,
            @NotBlank String advisoryType,
            @NotBlank String priority,
            @NotBlank String title,
            @NotBlank String summary,
            String detailedGuidance,
            @NotBlank String sourceType,
            UUID sourceReferenceId,
            @NotBlank String language,
            @NotNull Instant validFrom,
            Instant validUntil,
            @DecimalMin("0.0") @DecimalMax("1.0") BigDecimal confidenceScore,
            List<String> reasonCodes,
            Map<String,Object> evidenceSnapshot,
            List<@Valid ActionRequest> actions) {}

    public record FeedbackRequest(
            @NotNull UUID farmerId,
            @NotBlank String feedbackType,
            @Min(1) @Max(5) Integer rating,
            String comments) {}

    public record Response(
            UUID id, UUID farmerId, UUID farmId, UUID fieldId,
            UUID cropCycleId, String advisoryType, String priority,
            String title, String summary, String detailedGuidance,
            String sourceType, String language, Instant validFrom,
            Instant validUntil, String status, BigDecimal confidenceScore,
            String reasonCodesJson, String evidenceSnapshotJson, long version) {
        public static Response from(AdvisoryEntity value) {
            return new Response(value.id(), value.farmerId(), value.farmId(),
                    value.fieldId(), value.cropCycleId(), value.advisoryType(),
                    value.priority(), value.title(), value.summary(),
                    value.detailedGuidance(), value.sourceType(),
                    value.language(), value.validFrom(), value.validUntil(),
                    value.status(), value.confidenceScore(),
                    value.reasonCodes(), value.evidenceSnapshot(), value.version());
        }
    }
}
