package com.agrios.platform.advisory.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "expert_review_decision", schema = "advisory")
public class ExpertReviewDecisionEntity {
    @Id private UUID id;
    @Column(nullable = false, unique = true) private UUID reviewCaseId;
    @Column(nullable = false) private UUID expertId;
    @Column(nullable = false) private String decisionCode;
    @Column(nullable = false, columnDefinition = "text") private String decisionSummary;
    @Column(columnDefinition = "text") private String recommendationText;
    private String riskLevel;
    @Column(nullable = false) private boolean followUpRequired;
    private Instant followUpDueAt;
    private UUID advisoryId;
    @Column(nullable = false) private Instant decidedAt;

    protected ExpertReviewDecisionEntity() {}

    public static ExpertReviewDecisionEntity create(
            UUID caseId, UUID expertId, String decisionCode,
            String summary, String recommendation, String riskLevel,
            boolean followUpRequired, Instant followUpDueAt,
            UUID advisoryId) {
        ExpertReviewDecisionEntity value = new ExpertReviewDecisionEntity();
        value.id = UUID.randomUUID();
        value.reviewCaseId = caseId;
        value.expertId = expertId;
        value.decisionCode = decisionCode;
        value.decisionSummary = summary;
        value.recommendationText = recommendation;
        value.riskLevel = riskLevel;
        value.followUpRequired = followUpRequired;
        value.followUpDueAt = followUpDueAt;
        value.advisoryId = advisoryId;
        value.decidedAt = Instant.now();
        return value;
    }
}
